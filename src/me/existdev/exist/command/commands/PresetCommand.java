package me.existdev.exist.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.command.Command;
import me.existdev.exist.module.modules.combat.KillAura;
import me.existdev.exist.module.modules.combat.Velocity;
import me.existdev.exist.module.modules.movement.NoSlowdown;
import me.existdev.exist.module.modules.movement.Speed;
import me.existdev.exist.module.modules.movement.Step;
import me.existdev.exist.module.modules.player.NoFall;
import me.existdev.exist.module.modules.world.ScaffoldWalk;

public class PresetCommand implements Command {

	@Override
	public boolean run(String[] args) {
		if (args.length == 2) {
			if (!args[1].equalsIgnoreCase("NCP") && !args[1].equalsIgnoreCase("AAC")) {
				Exist.tellPlayer(ChatFormatting.RED + "Error! " + ChatFormatting.WHITE + "Preset " + ChatFormatting.AQUA
						+ args[1].toUpperCase() + ChatFormatting.RED + " does not exist.");
				return true;
			}
			if (args[1].equalsIgnoreCase("NCP")) {
				NCP();
			}
			if (args[1].equalsIgnoreCase("AAC")) {
				AAC();
			}

			Exist.tellPlayer("Preset" + ChatFormatting.GREEN + " [" + args[1].toUpperCase() + "] " + ChatFormatting.RESET
					+ "was loaded! ");
			return true;
		}
		return false;

	}

	@Override
	public String usage() {
		return " -preset [AAC / NCP]";
	}

	public void NCP() {
		// KillAura
		Exist.settingManager.getSetting(Exist.getModules(KillAura.class), "Lock").setBooleanValue(true);
		// Scaffold
		Exist.settingManager.getSetting(Exist.getModules(ScaffoldWalk.class), "NCP").setBooleanValue(true);
		Exist.settingManager.getSetting(Exist.getModules(ScaffoldWalk.class), "Sprint").setBooleanValue(true);
		// Speed
		Exist.settingManager.getSetting(Exist.getModules(Speed.class), "Mode").setCurrentOption("NewAACBhop");
		// Velocity
		Exist.settingManager.getSetting(Exist.getModules(Velocity.class), "Mode").setCurrentOption("Hypixel");
		// NoSlowdown
		Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").setCurrentOption("NCP");
		// Step
		Exist.settingManager.getSetting(Exist.getModules(Step.class), "Mode").setCurrentOption("Normal");

	}

	public void AAC() {
		// KillAura
		Exist.settingManager.getSetting(Exist.getModules(KillAura.class), "Mode").setCurrentOption("AAC");
		// Scaffold
		Exist.settingManager.getSetting(Exist.getModules(ScaffoldWalk.class), "NCP").setBooleanValue(false);
		Exist.settingManager.getSetting(Exist.getModules(ScaffoldWalk.class), "Switch").setBooleanValue(false);
		Exist.settingManager.getSetting(Exist.getModules(ScaffoldWalk.class), "Sprint").setBooleanValue(false);
		Exist.settingManager.getSetting(Exist.getModules(ScaffoldWalk.class), "Delay").setCurrentValue(5);
		// Speed
		Exist.settingManager.getSetting(Exist.getModules(Speed.class), "Mode").setCurrentOption("NewAACBhop");
		// Velocity
		Exist.settingManager.getSetting(Exist.getModules(Velocity.class), "Mode").setCurrentOption("AAC");
		// NoSlowdown
		Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").setCurrentOption("AAC");
		// Step
		Exist.settingManager.getSetting(Exist.getModules(Step.class), "Mode").setCurrentOption("AAC");
	}
}
