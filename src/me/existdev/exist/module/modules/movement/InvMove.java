package me.existdev.exist.module.modules.movement;

import java.util.Objects;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;

public class InvMove extends Module {

	public InvMove() {
		super("InvMove", 0, Category.Movement);
	}

}
