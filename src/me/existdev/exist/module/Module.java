package me.existdev.exist.module;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.module.modules.render.HUD;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;

public class Module {

	public Minecraft mc = Minecraft.getMinecraft();
	public Category category;
	public boolean toggled;
	public String name;
	public String displayname;
	public String mode;
	public int bind;
	protected int modeInt;
	String color;

	public int slize = 0;

	public Module(String name, int bind, Category category) {
		this.mode = "";
		this.name = name;
		this.displayname = name;
		;
		this.bind = bind;
		this.category = category;
		this.toggled = false;
	}

	public void toggle() {
		toggled = !toggled;
		if (toggled) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public void onUpdate() {
	}

	public void onRender() {
	}

	public void onEnable() {
		slize = 0;
		EventManager.register(this);
		mc.getSoundHandler()
				.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1F));
	}

	public void onDisable() {
		slize = 0;
		EventManager.unregister(this);
		mc.getSoundHandler()
				.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1F));
	}

	public void setSpeed(double speed) {
		EntityPlayerSP player = mc.thePlayer;
		double yaw = (double) player.rotationYaw;
		boolean isMoving = player.moveForward != 0.0F || player.moveStrafing != 0.0F;
		boolean isMovingForward = player.moveForward > 0.0F;
		boolean isMovingBackward = player.moveForward < 0.0F;
		boolean isMovingRight = player.moveStrafing > 0.0F;
		boolean isMovingLeft = player.moveStrafing < 0.0F;
		boolean isMovingSideways = isMovingLeft || isMovingRight;
		boolean isMovingStraight = isMovingForward || isMovingBackward;
		if (isMoving) {
			if (isMovingForward && !isMovingSideways) {
				yaw += 0.0D;
			} else if (isMovingBackward && !isMovingSideways) {
				yaw += 180.0D;
			} else if (isMovingForward && isMovingLeft) {
				yaw += 45.0D;
			} else if (isMovingForward) {
				yaw -= 45.0D;
			} else if (!isMovingStraight && isMovingLeft) {
				yaw += 90.0D;
			} else if (!isMovingStraight && isMovingRight) {
				yaw -= 90.0D;
			} else if (isMovingBackward && isMovingLeft) {
				yaw += 135.0D;
			} else if (isMovingBackward) {
				yaw -= 135.0D;
			}

			yaw = Math.toRadians(yaw);
			player.motionX = -Math.sin(yaw) * speed;
			player.motionZ = Math.cos(yaw) * speed;
		}

	}

	public ArrayList<Setting> getSettings() {
		final ArrayList<Setting> settings = new ArrayList<>();

		for (final Field field : getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);

				final Object o = field.get(this);

				if (o instanceof Setting)
					settings.add((Setting) o);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return settings;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isToggled() {
		return toggled;
	}

	public String getDisplayName() {
		return displayname + ChatFormatting.GRAY + " " + getMode();
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public enum Category {
		Combat, Movement, Player, Render, World, Gui;
	}

	public int getBind() {
		return bind;
	}

	public void setBind(int bind) {
		this.bind = bind;
	}
}
