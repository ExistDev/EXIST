package me.existdev.exist.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import net.minecraft.item.ItemFood;

public class WaterSpeed extends Module {

	public WaterSpeed() {
		super("WaterSpeed", 0, Category.Movement);
	}

	@Override
	public void onUpdate() {
		if(!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating() && mc.thePlayer.onGround) {
				return;
			}
		}
		this.setMode("AAC");
		super.onUpdate();
	}
}
