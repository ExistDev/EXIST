package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventMove;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.MovementUtils;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;

public class Speed extends Module {

	double horizontalSpeed = 0.018;
	TimeHelper time;
	private int level;
	private double delay1 = 0, delay2 = 0, delay3;
	ArrayList<String> Modes = new ArrayList<>();
	public int value;

	public Speed() {
		super("Speed", 0, Category.Movement);
		Modes.add("AACY-Port");
		Modes.add("AACBhop");
		Modes.add("NewAACBhop");
		Modes.add("AACTPhop");
		Modes.add("AAC1.9.10");
		Modes.add("Mineplex");
		Modes.add("NCPY-Port");
		Modes.add("NewNCP");
		Modes.add("NCPLowhop");
		time = new TimeHelper();
		this.value = 0;
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AACBhop", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (mc.gameSettings.keyBindJump.pressed) {
			return;
		}
		if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating()) {
				return;
			}
		}
		// AAC
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NewAACBhop")) {
			NewAACBhop();
			this.setMode("NewAACBhop");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACBhop")) {
			AACBhop();
			this.setMode("AACBhop");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACTPhop")) {
			AACTPhop();
			this.setMode("AAC3.1.5 : TPhop");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC1.9.10")) {
			AAC1910();
			setMode("AAC1.9.10");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACY-Port")) {
			this.setMode("AACY-Port");
		}
		// NCP
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCPLowhop")) {
			NCPLowhop();
			setMode("NCPLowhop");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCPY-Port")) {
			NCPYPort();
			this.setMode("NCPY-Port");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NewNCP")) {
			this.setMode("NewNCP");
		}
		// MinePlex
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Mineplex")) {
			Mineplex();
			this.setMode("Mineplex");
		}
		super.onUpdate();
	}

	public void AAC1910() {
		if (!mc.thePlayer.isMoving()) {
			return;
		}
		if (mc.thePlayer.onGround) {
			mc.thePlayer.jump();
		}
		setSpeed(0.35F);
	}

	public void NCPLowhop() {
		if (!mc.thePlayer.isMoving()) {
			return;
		}
		if (mc.thePlayer.onGround) {
			setSpeed(0.27);
			mc.timer.timerSpeed = 1.2F;
			mc.thePlayer.addVelocity(0, 0.3, 0);
			mc.thePlayer.onGround = false;
		} else {
			setSpeed(0.15);
			mc.thePlayer.addVelocity(0, -0.05, 0);
			mc.thePlayer.onGround = true;
		}
	}

	// NCPY-Port Method

	private void NCPYPort() {
		if (!mc.thePlayer.isMoving()) {
			return;
		}
		if (mc.thePlayer.onGround) {
			mc.thePlayer.jump();
		} else {
			mc.thePlayer.motionY = -0.8;
		}
	}

	@EventTarget
	public void onPost(EventPreMotionUpdates event) {
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCPY-Port")) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACY-Port")) {
			return;
		}
		this.mc.thePlayer.cameraPitch = 0.0F;
		BlockPos below = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.5D, this.mc.thePlayer.posZ);
		if (!(this.mc.theWorld.getBlockState(below).getBlock() instanceof BlockAir)
				&& !this.mc.thePlayer.isBlocking()) {
			this.mc.thePlayer.posY = (double) (below.getY() + 1);
		}

	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates event) {
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACY-Port") && !this.mc.thePlayer.isBlocking()) {
			BlockPos bp = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 2.0D, this.mc.thePlayer.posZ);
			if (this.mc.gameSettings.keyBindForward.pressed && !this.mc.thePlayer.isInWater()) {
				BlockPos below = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D,
						this.mc.thePlayer.posZ);
				if (this.mc.theWorld.getBlockState(bp).getBlock() == Blocks.air) {
					if (this.mc.thePlayer.isCollidedVertically) {
						this.mc.thePlayer.jump();
					} else if (!(this.mc.theWorld.getBlockState(below).getBlock() instanceof BlockAir)) {
						this.mc.thePlayer.motionY = -0.2149999886751175D;
					}
				} else {
					this.mc.thePlayer.motionX *= 1.7D;
					this.mc.thePlayer.motionZ *= 1.7D;
					this.mc.thePlayer.jump();
					--this.mc.thePlayer.motionY;
				}
			}
		} else if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCPY-Port")) {
			if (this.mc.thePlayer.onGround) {
				this.mc.thePlayer.jump();
			} else {
				this.mc.thePlayer.motionY = -0.42D;
			}
		}

	}

	// NewAACBhop Method

	private void NewAACBhop() {
		if (!mc.thePlayer.isMoving()) {
			return;
		}
		if (mc.thePlayer.onGround) {
			setSpeed(0.7F);
			mc.thePlayer.motionY = 0.3851F;
		}
		setSpeed(0.27F);
	}

	// AACBhop Method

	public void AACBhop() {
		if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating()) {
				return;
			}
		}
		if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
			mc.thePlayer.jump();
			mc.thePlayer.motionY = 0.3851F;
			mc.timer.timerSpeed = 1.05F;
			mc.thePlayer.motionX *= 1.01F;
			mc.thePlayer.motionZ *= 1.01F;
		}
	}

	// AACTPhop Method

	public void AACTPhop() {
		this.setSpeed(this.getSpeed());
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0)
					yaw += ((forward > 0.0) ? -45 : 45);
				else if (strafe < 0.0)
					yaw += ((forward > 0.0) ? 45 : -45);

				strafe = 0.0;

				if (forward > 0.0)
					forward = 1.0;
				else if (forward < 0.0)
					forward = -1.0;
			}
			if (canSpeed()) {
				this.setSpeed(this.getSpeed() + horizontalSpeed);
				mc.gameSettings.keyBindJump.pressed = true;
				mc.thePlayer.jumpMovementFactor = 3;
				mc.thePlayer.moveStrafing = 2;
			}
			if (mc.thePlayer.fallDistance >= 0.3) {
				mc.timer.timerSpeed = 2.23F;
			} else {
				mc.timer.timerSpeed = 1.0F;
			}
		}
	}

	// NewNCP Method

	@EventTarget
	public void NewNCP(EventMove event) {
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NewNCP")) {
			return;
		}
		if (!this.MovementInput()) {
			level = 1;
			time.reset();
			return;
		}
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
			if (forward > 0.0F)
				forward = 1.0F;
			else if (forward < 0.0F)
				forward = -1.0F;
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		this.NewNCP(event, forward, strafe, mx, mz);
	}

	public void NewNCP(EventMove e, float forward, float strafe, double mx, double mz) {
		if (mc.thePlayer.isCollidedHorizontally)
			return;
		double m = 0.455;
		double m2 = 0.31;
		final int amplifier = 0;
		double boost = 1;
		switch (amplifier) {
		case 1:
			boost = 1.2;
			break;
		case 2:
			boost = 1.4;
			break;
		case 3:
			boost = 2.8;
			break;
		case 4:
			boost = 3.4;
			break;
		}

		if (mc.thePlayer.ticksExisted % 4 == 0) {
			mc.timer.timerSpeed = 3F;
		} else {
			mc.timer.timerSpeed = 1.0F;
		}
		if (mc.thePlayer.fallDistance > 1.4 && mc.thePlayer.fallDistance < 2)
			e.y = mc.thePlayer.motionY = -5;
		if (delay2 == 1) {
			e.y = mc.thePlayer.motionY = -5;
			e.x = (forward * m * mx + strafe * m * mz);
			e.z = (forward * m * mz - strafe * m * mx);
		} else if (delay2 == 2) {
			e.x = (forward * m2 * mx + strafe * m2 * mz);
			e.z = (forward * m2 * mz - strafe * m2 * mx);
			delay2 = 0;
		}
		e.x *= boost;
		e.z *= boost;
	}

	// Mineplex Method

	public void Mineplex() {
		if (mc.thePlayer.isMoving()) {
			mc.thePlayer.motionX *= 1.39;
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4, mc.thePlayer.posZ);
			mc.thePlayer.motionZ *= 1.39;
			mc.thePlayer.onGround = true;
		} else {
			mc.thePlayer.motionX *= 0;
			mc.thePlayer.motionX *= 0;
		}
	}

	@Override
	public void onEnable() {
		time.reset();
		level = 1;
		value = 0;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		delay1 = delay2 = delay3 = 0;
		mc.timer.timerSpeed = 1f;
		level = 0;
		time.reset();
		value = 0;
		mc.gameSettings.keyBindJump.pressed = false;
		super.onDisable();
	}

	public boolean canSpeed() {
		if (this.isMoving() && mc.thePlayer.onGround) {
			return true;
		}
		return false;
	}

	public float getDirection() {
		float yaw = mc.thePlayer.rotationYawHead;
		float forward = mc.thePlayer.moveForward;
		float strafe = mc.thePlayer.moveStrafing;
		yaw += (forward < 0.0F ? 180 : 0);
		if (strafe < 0.0F) {
			yaw += (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
		}
		if (strafe > 0.0F) {
			yaw -= (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
		}
		return yaw * 0.017453292F;
	}

	public double square(double in) {
		return in * in;
	}

	public double getSpeed() {
		return Math.sqrt(square(mc.thePlayer.motionX) + square(mc.thePlayer.motionZ));
	}

	public void setSpeed(double speed) {
		mc.thePlayer.motionX = (-net.minecraft.util.MathHelper.sin(getDirection()) * speed);
		mc.thePlayer.motionZ = (net.minecraft.util.MathHelper.cos(getDirection()) * speed);
	}

	public boolean isMoving() {
		return (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F);
	}

	public boolean MovementInput() {
		return (mc.gameSettings.keyBindForward.getIsKeyPressed() || mc.gameSettings.keyBindBack.getIsKeyPressed()
				|| mc.gameSettings.keyBindLeft.getIsKeyPressed() || mc.gameSettings.keyBindRight.getIsKeyPressed()
				|| ((mc.gameSettings.keyBindSneak.getIsKeyPressed() && !mc.thePlayer.isCollidedVertically)
						|| mc.gameSettings.keyBindJump.getIsKeyPressed()));
	}
}
