package com.chattriggers.ctjs.handlers;

import com.chattriggers.ctjs.commands.Command;
import lombok.Getter;

import java.util.ArrayList;

public class CommandHandler {
    @Getter
    private ArrayList<Command> commandList = new ArrayList<>();
}
