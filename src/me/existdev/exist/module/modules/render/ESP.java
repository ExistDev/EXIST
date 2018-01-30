package me.existdev.exist.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Module {

	ArrayList<String> Modes = new ArrayList();

	public ESP() {
		super("ESP", 0, Category.Render);
		Modes.add("CSGO");
		Modes.add("Box");
		Modes.add("CSGOLine");
		Modes.add("Outline");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "CSGO", Modes));
	}

	@Override
	public void onUpdate() {
		if (Exist.settingManager.getSetting(Exist.getModules(ESP.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("Box")) {
			setMode("Box");
		}
		if (Exist.settingManager.getSetting(Exist.getModules(ESP.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("CSGO")) {
			setMode("CSGO");
		}
		if (Exist.settingManager.getSetting(Exist.getModules(ESP.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("CSGOLine")) {
			setMode("CSGOLine");
		}
		if (Exist.settingManager.getSetting(Exist.getModules(ESP.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("Outline")) {
			setMode("Outline");
		}
	}
}
