package me.existdev.exist.command.commands;

import me.existdev.exist.Exist;
import me.existdev.exist.command.Command;

public class HelpCommand implements Command {

	@Override
	public boolean run(String[] args) {
		Exist.tellPlayer("Here are the list of commands:");
		for(Command c : Exist.commandManager.getCommands().values()) {
			Exist.tellPlayer(c.usage());
		}
		return true;
	}

	@Override
	public String usage() {
		return " -help";
	}

}
