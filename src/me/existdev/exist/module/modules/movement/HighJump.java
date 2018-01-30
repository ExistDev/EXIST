package me.existdev.exist.module.modules.movement;

import me.existdev.exist.module.Module;

public class HighJump extends Module {

	public HighJump() {
		super("HighJump", 0, Category.Movement);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (mc.thePlayer.onGround) {
			mc.thePlayer.jump();
			mc.thePlayer.motionY *= 1.32D;
		}
		super.onUpdate();
	}

}
