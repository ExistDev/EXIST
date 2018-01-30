package me.existdev.exist.module.modules.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventMove;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class Longjump extends Module {

	TimeHelper time;
	private int stage;
	private double boost;
	private double moveSpeed;
	private double lastDist;
	ArrayList<String> Modes = new ArrayList<>();

	public Longjump() {
		super("Longjump", 0, Category.Movement);
		Modes.add("Cubecraft");
		Modes.add("Hypixel");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "Cubecraft", Modes));
		Exist.settingManager.addSetting(new Setting(this, "Boost(Cubecraft)", 7, 2, 20, false));
		time = new TimeHelper();
		this.moveSpeed = 0.2873D;
		this.boost = 3.0D;
	}

	@EventTarget
	public void onUpdate(EventMove e) {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Cubecraft")) {
			this.setMode("Cubecraft");
			Cubecraft();
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
			this.setMode("Hypixel");
			Hypixel(e);
		}
	}

	private void Hypixel(EventMove event) {
		if ((mc.thePlayer.moveStrafing == 0.0F) && (mc.thePlayer.moveForward == 0.0F)) {
			this.stage = 1;
		}
		if (round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == round(0.93D, 3)) {
			mc.thePlayer.motionY -= 0.01D;
			event.y -= 0.01D;
		}
		if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))
				&& (mc.thePlayer.onGround) && (mc.thePlayer.isCollidedVertically) && (mc.thePlayer.motionY < 0.0D)) {
			this.stage = 2;
			this.moveSpeed = (this.boost * getBaseMoveSpeed() - 0.01D);
		} else if (this.stage == 2) {
			this.stage = 3;
			mc.thePlayer.motionY = 0.41764345D;
			event.y = 0.41764345D;
			this.moveSpeed *= 2.149802D;
		} else if (this.stage == 3) {
			this.stage = 4;
			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference);
		} else {
			if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
					mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0)
					|| (mc.thePlayer.isCollidedVertically)) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
		} else if (forward != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (forward > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (forward > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}
			if (forward > 0.0F) {
				forward = 1.0F;
			} else if (forward < 0.0F) {
				forward = -1.0F;
			}
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
		event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
	}

	@EventTarget
	private void onUpdate(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
			return;
		}
			boolean speedy = mc.thePlayer.isPotionActive(Potion.moveSpeed);
			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}

	public void Cubecraft() {
		if (mc.thePlayer.isMoving()) {
			if (mc.thePlayer.onGround) {
				mc.thePlayer.jump();
				mc.thePlayer.motionY = 0.4;
			} else {
				double yaw = mc.thePlayer.rotationYawHead;

				if (mc.gameSettings.keyBindBack.pressed && !mc.gameSettings.keyBindForward.pressed) {
					yaw -= 180;
				}
				yaw = Math.toRadians(yaw);
				final double dX = -Math.sin(yaw)
						* Exist.settingManager.getSetting(this, "Boost(Cubecraft)").getCurrentValue();
				final double dZ = Math.cos(yaw)
						* Exist.settingManager.getSetting(this, "Boost(Cubecraft)").getCurrentValue();

				if (time.hasReached(40L)) {
					mc.thePlayer.motionX = dX * 0.65;
					mc.thePlayer.motionZ = dZ * 0.65;
					time.reset();
				}
			}
		}
	}

	public double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	@Override
	public void onEnable() {
		if (mc.thePlayer != null) {
			this.moveSpeed = getBaseMoveSpeed();
		}
		this.lastDist = 0.0D;
		this.stage = 1;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1F;
		super.onDisable();
	}
}
