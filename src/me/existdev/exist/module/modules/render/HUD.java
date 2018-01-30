package me.existdev.exist.module.modules.render;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventManager;

import me.existdev.exist.Exist;
import me.existdev.exist.gui.TabGUI;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;

public class HUD extends Module {

	TabGUI tabGui;

	public HUD() {
		super("HUD", 0, Category.Render);
		tabGui = new TabGUI();
	}

	@Override
	public void onEnable() {
		EventManager.register(tabGui);
		super.onEnable();
	}

	@Override
	public void onDisable() {
		EventManager.unregister(tabGui);
		super.onDisable();
	}

}
