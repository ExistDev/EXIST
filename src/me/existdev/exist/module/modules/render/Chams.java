package me.existdev.exist.module.modules.render;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class Chams extends Module {

	public Chams() {
		super("Chams", 0, Category.Render);
	}

	@EventTarget
	public void onEvent(EventPreMotionUpdates event) {
		GL11.glEnable(32823);
		GL11.glPolygonOffset(1.0f, -1000000.0f);
	}

	@EventTarget
	public void onPostEvent(EventPreMotionUpdates event) {
		GL11.glDisable(32823);
		GL11.glPolygonOffset(1.0f, 1000000.0f);
	}

	public boolean isValidTarget(Object e) {
		if (!(e instanceof EntityAnimal) && !(e instanceof EntityMob) && !(e instanceof EntityPlayer)
				&& !(e instanceof EntityItem))
			return false;
		if (e instanceof EntityPlayerSP && e instanceof EntityPlayer) {
			return true;
		}
		return true;
	}
}
