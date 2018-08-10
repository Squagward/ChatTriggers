package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.MathLib;
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class ConfigColor extends ConfigOption {
    public Color value = null;
    private Color defaultValue;

    private transient GuiButton redButton, greenButton, blueButton;
    private transient boolean redHeld, blueHeld, greenHeld;

    ConfigColor(ConfigColor previous, String name, Color defaultValue, int x, int y) {
        super(ConfigOption.Type.COLOR);

        this.name = name;
        this.defaultValue = defaultValue;

        if (previous == null)
            this.value = this.defaultValue;
        else
            this.value = previous.value;

        this.x = x;
        this.y = y;
    }

    @Override
    public void init() {
        super.init();

        this.redHeld = false;
        this.blueHeld = false;
        this.greenHeld = false;

        int middle = Renderer.screen.getWidth() / 2;

        this.redButton = new GuiButton(
                0,
                (int) MathLib.map(this.value.getRed(), 0, 255, middle - 100 + this.x, middle + 52 + this.x),
                this.y + 15,
                5,
                10,
                ""
        );

        this.greenButton = new GuiButton(
                0,
                (int) MathLib.map(this.value.getGreen(), 0, 255, middle - 100 + this.x, middle + 52 + this.x),
                this.y + 30,
                5,
                10,
                ""
        );

        this.blueButton = new GuiButton(
                0,
                (int) MathLib.map(this.value.getBlue(), 0, 255, middle - 100 + this.x, middle + 52 + this.x),
                this.y + 45,
                5,
                10,
                ""
        );
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (this.hidden) return;

        int middle = Renderer.screen.getWidth() / 2;

        Renderer.rectangle(0x80000000, middle - 105 + this.x, this.y - 5, 210, 65)
                .setShadow(0xd0000000, 3, 3)
                .draw();
        Renderer.text(this.name, middle - 100 + this.x, this.y).draw();

        // red slider
        Renderer.rectangle(0xffaa0000, middle - 100 + this.x, this.y + 19, 155, 3)
                .setOutline(0xff000000, 1)
                .draw();
        this.redButton.x = (int) MathLib.map(this.value.getRed(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
        this.redButton.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks);

        // green slider
        Renderer.rectangle(0xff008800, middle - 100 + this.x, this.y + 34, 155, 3)
                .setOutline(0xff000000, 1)
                .draw();
        this.greenButton.x = (int) MathLib.map(this.value.getGreen(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
        this.greenButton.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks);

        // blue slider
        Renderer.rectangle(0xff0000cc, middle - 100 + this.x, this.y + 49, 155, 3)
                .setOutline(0xff000000, 1)
                .draw();
        this.blueButton.x = (int) MathLib.map(this.value.getBlue(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
        this.blueButton.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks);

        // color preview
        Renderer.rectangle(this.value.getRGB(), middle + this.x + 60, this.y + 15, 40, 40)
                .setOutline(0xff000000, 1)
                .draw();

        handleHeldButtons(mouseX, middle);

        super.draw(mouseX, mouseY, partialTicks);
    }

    private void handleHeldButtons(int mouseX, int middle) {
        if (this.redHeld) {
            this.redButton.x = mouseX - 1;
            limitHeldButton(this.redButton);
            this.value = new Color(
                    (int) MathLib.map(this.redButton.x, middle - 100 + this.x, middle + 52 + this.x, 0, 255),
                    this.value.getGreen(),
                    this.value.getBlue()
            );
        }
        if (this.greenHeld) {
            this.greenButton.x = mouseX - 1;
            limitHeldButton(this.greenButton);
            this.value = new Color(
                    this.value.getRed(),
                    (int) MathLib.map(this.greenButton.x, middle - 100 + this.x, middle + 52 + this.x, 0, 255),
                    this.value.getBlue()
            );
        }
        if (this.blueHeld) {
            this.blueButton.x = mouseX - 1;
            limitHeldButton(this.blueButton);
            this.value = new Color(
                    this.value.getRed(),
                    this.value.getGreen(),
                    (int) MathLib.map(this.blueButton.x, middle - 100 + this.x, middle + 52 + this.x, 0, 255)
            );
        }
    }

    private void limitHeldButton(GuiButton button) {
        if (button.x < Renderer.screen.getWidth() / 2 - 100 + this.x)
            button.x = Renderer.screen.getWidth() / 2 - 100 + this.x;
        if (button.x > Renderer.screen.getWidth() / 2 + 52 + this.x)
            button.x = Renderer.screen.getWidth() / 2 + 52 + this.x;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (this.hidden) return;

        if (this.redButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.redHeld = true;
            this.redButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
        if (this.greenButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.greenHeld = true;
            this.greenButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
        if (this.blueButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.blueHeld = true;
            this.blueButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }

        if (this.resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = this.defaultValue;
            int middle = Renderer.screen.getWidth() / 2;
            this.redButton.x = (int) MathLib.map(this.value.getRed(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
            this.greenButton.x = (int) MathLib.map(this.value.getGreen(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
            this.blueButton.x = (int) MathLib.map(this.value.getBlue(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
            this.resetButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
    }

    @Override
    public void mouseReleased() {
        this.redHeld = false;
        this.blueHeld = false;
        this.greenHeld = false;
    }
}
