package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action;

import com.chattriggers.ctjs.minecraft.wrappers.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class ClickAction extends Action {
    /**
     * The type of click (REQUIRED)
     *
     * @param clickType the new click type
     */
    @Setter @Getter
    private ClickType clickType;

    /**
     * Whether the click should act as if shift is being held (defaults to false)
     *
     * @param holdingShift to hold shift or not
     */
    @Setter @Getter
    private boolean holdingShift = false;

    /**
     * Whether the click should act as if an item is being held
     * (defaults to whether there actually is an item in the hand)
     *
     * @param itemInHand to be holding an item or not
     */
    @Setter @Getter
    private boolean itemInHand = Player.getPlayer().inventory.getCurrentItem() == null;

    /**
     * Whether the click should try to pick up all items of said type in the inventory (essentially double clicking)
     * (defaults to whether there actually is an item in the hand)
     *
     * @param pickupAll to pickup all items of the same type
     */
    @Setter @Getter
    private boolean pickupAll = false;

    /**
     * The slot to click on (-999 if outside gui)
     *
     * @param slot the slot
     * @param windowId the current window's id
     */
    public ClickAction(int slot, int windowId) {
        super(slot, windowId);
    }

    /**
     * Sets the type of click.
     * Possible values are: LEFT, RIGHT, MIDDLE
     *
     * @param clickType the click type
     * @return the current Action for method chaining
     */
    public ClickAction setClickString(String clickType) {
        this.clickType = ClickType.valueOf(clickType.toUpperCase());
        return this;
    }

    @Override
    public void complete() {
        net.minecraft.inventory.ClickType type;

        if (this.clickType == ClickType.MIDDLE) {
            type = net.minecraft.inventory.ClickType.CLONE;
        } else if (slot == -999 && !this.itemInHand) {
            type = net.minecraft.inventory.ClickType.THROW;
        } else if (this.holdingShift) {
            type = net.minecraft.inventory.ClickType.QUICK_MOVE;
        } else if (pickupAll) {
            type = net.minecraft.inventory.ClickType.PICKUP_ALL;
        } else {
            type = net.minecraft.inventory.ClickType.PICKUP;
        }

        doClick(this.clickType.getButton(), type);
    }

    public enum ClickType {
        LEFT(0), RIGHT(1), MIDDLE(2);

        @Getter
        private int button;

        ClickType(int button) {
            this.button = button;
        }
    }
}
