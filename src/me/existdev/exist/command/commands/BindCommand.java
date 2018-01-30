package me.existdev.exist.command.commands;

import org.lwjgl.input.Keyboard;

import me.existdev.exist.Exist;
import me.existdev.exist.command.Command;
import me.existdev.exist.module.Module;

public class BindCommand implements Command {

	@Override
	public boolean run(String[] args) {
		if (args.length == 3) {

			Module m = Exist.getModuleByName(args[1]);
			if (m == null)
				return false;
			m.setBind(Keyboard.getKeyIndex(args[2].toUpperCase()));
			Exist.tellPlayer(m.getName() + " has been bound to " + args[2].toUpperCase() + ".");
			return true;
		}
		return false;
	}

	@Override
	public String usage() {
		return " -bind [module] [key]";
	}

}
