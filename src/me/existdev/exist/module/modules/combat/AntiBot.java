package me.existdev.exist.module.modules.combat;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class AntiBot extends Module {

	ArrayList<String> Modes = new ArrayList<>();

	public AntiBot() {
		super("AntiBot", 0, Category.Combat);
		Modes.add("Mineplex");
		Modes.add("WatchDog");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "Hypixel", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("WatchDog")) {
			this.setMode("WatchDog");
			watchdog();
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Mineplex")) {
			this.setMode("Mineplex");

		}

		super.onUpdate();
	}

	public void hypixel() {
		for (Object entity : mc.theWorld.loadedEntityList) {
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			if (entityPlayer != null && entityPlayer != mc.thePlayer) {
				if (entityPlayer.getDisplayName().getFormattedText().equalsIgnoreCase(entityPlayer.getName() + "Åòr")
						&& !mc.thePlayer.getDisplayName().getFormattedText()
								.equalsIgnoreCase(mc.thePlayer.getName() + "Åòr")) {
					entityPlayer.setInvisible(true);
				}
			}
		}
	}

	public void watchdog() {
		for (Object entity : mc.theWorld.loadedEntityList) {
			if ((((Entity) entity).isInvisible()) && (entity != mc.thePlayer)) {
				((Entity) entity).setInvisible(true);
			}
		}
	}

	@EventTarget
	public void receivePacket(EventPacket e) {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Mineplex")) {
			for (Object entity : mc.theWorld.loadedEntityList) {
				e.recievecancel = false;
				if ((e.getPacket() instanceof S0CPacketSpawnPlayer)) {
					S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) e.getPacket();
					double posX = packet.func_148942_f() / 32.0D;
					double posY = packet.func_148949_g() / 32.0D;
					double posZ = packet.func_148946_h() / 32.0D;

					double difX = mc.thePlayer.posX - posX;
					double difY = mc.thePlayer.posY - posY;
					double difZ = mc.thePlayer.posZ - posZ;

					double dist = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
					if ((dist <= 17.0D) && (posX != mc.thePlayer.posX) && (posY != mc.thePlayer.posY)
							&& (posZ != mc.thePlayer.posZ)) {
						e.recievecancel = true;
					}
				}
			}
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
