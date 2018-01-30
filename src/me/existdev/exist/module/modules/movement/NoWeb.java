package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.item.ItemFood;

public class NoWeb extends Module {

	ArrayList<String> Modes = new ArrayList<>();

	public NoWeb() {
		super("NoWeb", 0, Category.Movement);
		Exist.settingManager.addSetting(new Setting(this, "WebUp", 10, 0, 20, true));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating() && mc.thePlayer.onGround) {
				return;
			}
		}
		setMode("AAC");
		super.onUpdate();
	}
}
