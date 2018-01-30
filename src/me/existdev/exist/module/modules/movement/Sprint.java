package me.existdev.exist.module.modules.movement;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.world.ScaffoldWalk;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", 0, Category.Movement);
	}

	@Override
	public void onUpdate() {
		if(!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(Exist.getModules(ScaffoldWalk.class), "Sprint").getBooleanValue()
				&& Exist.getModules(ScaffoldWalk.class).isToggled()) {
			return;
		}
		if (mc.thePlayer.isMoving()) {
			mc.thePlayer.setSprinting(true);
		}
		super.onUpdate();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.thePlayer.setSprinting(false);
		super.onDisable();
	}
}
