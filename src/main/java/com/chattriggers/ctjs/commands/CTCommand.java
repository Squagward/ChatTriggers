package com.chattriggers.ctjs.commands;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.loader.ScriptLoader;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import javax.script.ScriptEngine;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CTCommand extends CommandBase {
    private ScriptEngine scriptEngine;
    private ScriptLoader scriptLoader;

    public CTCommand() {
        this.scriptEngine = CTJS.getInstance().getScriptEngine();
        this.scriptLoader = CTJS.getInstance().getScriptLoader();
    }

    @Override
    public String getCommandName() {
        return "chattriggers";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ct <reload/files/console>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("ct");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case("reload"):
                case("load"):
                    TriggerType.WORLD_UNLOAD.triggerAll();
                    CTJS.getInstance().initMain(false);
                    ChatLib.chat(EnumChatFormatting.RED + "Reloaded js files");
                    TriggerType.WORLD_LOAD.triggerAll();
                    break;
                case("files"):
                    openFileLocation();
                    break;
                case("console"):
                    Console.getConsole().showConsole(true);
                    Console.clear();
                    break;
                default:
                    ChatLib.chat(EnumChatFormatting.RED + getCommandUsage(sender));
                    break;
            }
        } else {
            ChatLib.chat(EnumChatFormatting.RED + getCommandUsage(sender));
        }
    }


    // Open the folder containing all of ChatTrigger's files
    private void openFileLocation() {
        try {
            Desktop.getDesktop().open(new File("./mods/ChatTriggers"));
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
            ChatLib.chat(EnumChatFormatting.RED + "Could not open file location");
        }
    }
}
