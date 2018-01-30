package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Glide extends Module {

	ArrayList<String> Modes = new ArrayList<>();
	private double startY;
	private int time = 0;
	private double value;

	public Glide() {
		super("Glide", 0, Category.Movement);
		Modes.add("HAC");
		Modes.add("AAC1.9.10");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(Exist.getModules(Glide.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("HAC")) {
			setMode("HAC");
		}
		if (Exist.settingManager.getSetting(Exist.getModules(Glide.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC1.9.10")) {
			setMode("AAC1.9.10");
		}
		super.onUpdate();
	}

	@Override
	public void onEnable() {
		this.startY = mc.thePlayer.posY + 10.0;
		this.value = 0.25;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(Exist.getModules(Glide.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC1.9.10")) {
			return;
		}
		mc.thePlayer.motionY = 0.0;
		if (mc.gameSettings.keyBindJump.isPressed()) {
			this.startY += 1.0;
			mc.thePlayer.setPosition(mc.thePlayer.posX, this.startY, mc.thePlayer.posZ);
		}
		if (mc.gameSettings.keyBindSneak.isPressed()) {
			this.startY -= 1.0;
			mc.thePlayer.setPosition(mc.thePlayer.posX, this.startY, mc.thePlayer.posZ);
		}
		if (mc.thePlayer.moveForward == 0.0f || mc.thePlayer.isCollidedHorizontally) {
			mc.thePlayer.jumpMovementFactor = 0.02f;
			mc.timer.timerSpeed = 1.0f;
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionZ = 0.0;
			return;
		}
		--this.time;
		if (this.time <= 0) {
			this.time = 3;
			this.setSpeed(0.24f);
		} else {
			this.setSpeed(0.5f);
		}
	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(Exist.getModules(Glide.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC1.9.10")) {
			return;
		}
		if (e.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer c = (C03PacketPlayer) e.getPacket();
			c.field_149474_g = true;
			c.y = this.startY + this.value;
			this.value = -this.value;
			if (this.time > 60) {
				this.time = 0;
			}
		}
	}
}
