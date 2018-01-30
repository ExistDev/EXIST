package me.existdev.exist.command;

import java.util.HashMap;
import java.util.Map;

import me.existdev.exist.Exist;
import me.existdev.exist.command.commands.BindCommand;
import me.existdev.exist.command.commands.HelpCommand;
import me.existdev.exist.command.commands.PresetCommand;
import me.existdev.exist.command.commands.ToggleCommand;

public class CommandManager {

	private HashMap<String[], Command> commands;

	private String prefix;

	public CommandManager() {
		commands = new HashMap();
		prefix = "-";
	}

	public void loadCommands() {

		commands.put(new String[] { "help" }, new HelpCommand());
		commands.put(new String[] { "bind" }, new BindCommand());
		commands.put(new String[] { "toggle", "t" }, new ToggleCommand());
		commands.put(new String[] { "preset", "pre" }, new PresetCommand());
	}

	public boolean processCommand(String rawMessage) {
		if (!rawMessage.startsWith(prefix)) {
			return false;
		}

		boolean safe = rawMessage.split(prefix).length > 1;

		if (safe) {
			String beheaded = rawMessage.split(prefix)[1];

			String[] args = beheaded.split(" ");

			Command command = getCommand(args[0]);

			if (command != null) {

				if (!command.run(args)) {
					Exist.tellPlayer(command.usage());
				}
			}

			else {
				Exist.tellPlayer("That command does not exist!");
			}
		}

		else {
			Exist.tellPlayer("Try -help to get all commands!");
		}

		return true;
	}

	private Command getCommand(String name) {
		for (Map.Entry entry : commands.entrySet()) {
			String[] key = (String[]) entry.getKey();
			for (String s : key) {
				if (s.equalsIgnoreCase(name)) {
					return (Command) entry.getValue();
				}
			}

		}
		return null;
	}

	public HashMap<String[], Command> getCommands() {
		return commands;
	}

}
