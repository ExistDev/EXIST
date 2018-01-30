package me.existdev.exist.module.modules.player;

import me.existdev.exist.module.Module;

public class AntiCactus extends Module {

	public AntiCactus() {
		super("AntiCactus", 0, Category.Player);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		setMode("AAC");
		super.onUpdate();
	}

}
