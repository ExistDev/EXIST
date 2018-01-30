package me.existdev.exist.module.modules.combat;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.movement.Speed;
import me.existdev.exist.setting.Setting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {

	ArrayList<String> Mode = new ArrayList<>();

	public Velocity() {
		super("Velocity", 0, Category.Combat);
		Mode.add("Hypixel");
		Mode.add("AAC");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "Hypixel", Mode));

	}

	@EventTarget
	public void onUpdate(EventPacket e) {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")
				&& this.isToggled()) {
			this.setMode("AAC");
			if (Exist.getModules(Speed.class).isToggled()) {
				return;
			}
			if (!mc.thePlayer.onGround) {
				return;
			}
			if (!mc.gameSettings.keyBindJump.pressed) {
				if (mc.thePlayer.hurtTime > 0) {
					mc.thePlayer.onGround = true;
				}
			} else {
				if (mc.thePlayer.hurtTime > 0) {
					mc.thePlayer.motionX *= 0.35;
					mc.thePlayer.motionZ *= 0.35;

				}
			}
		}

		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")
				&& this.isToggled()) {
			this.setMode("Hypixel");
			hypixel(e);
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

	// Hypixel method
	public void hypixel(EventPacket e) {
		if (e.getPacket() instanceof S12PacketEntityVelocity
				&& ((S12PacketEntityVelocity) e.getPacket()).func_149412_c() == this.mc.thePlayer.getEntityId()) {
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
			packet.field_149415_b = (int) (packet.field_149415_b * 0);
			packet.field_149416_c = (int) (packet.field_149416_c * 0);
			packet.field_149414_d = (int) (packet.field_149414_d * 0);
			if (packet.field_149415_b == 0 && packet.field_149416_c == 0 && packet.field_149414_d == 0)
				e.setCancelled(true);
		}
		if (e.getPacket() instanceof S27PacketExplosion) {
			S27PacketExplosion packetExplosion = (S27PacketExplosion) e.getPacket();
			packetExplosion.field_149152_f = packetExplosion.field_149153_g = packetExplosion.field_149159_h = 0;
		}
	}
}
