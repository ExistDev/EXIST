package me.existdev.exist.module.modules.movement;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventBlock;
import me.existdev.exist.events.EventMove;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.events.EventPreMotionUpdates.EventType;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSnow;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Flight extends Module {

	ArrayList<String> Modes = new ArrayList<>();
	TimeHelper time;
	private int level;
	private double delay1 = 0, delay2 = 0, delay3;

	boolean lemon = true;

	public Flight() {
		super("Flight", 0, Category.Movement);
		Modes.add("Hypixel");
		Modes.add("NCP");
		Modes.add("Fast");
		Modes.add("AAC");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "Hypixel", Modes));
		time = new TimeHelper();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
			hypixel();
			mc.thePlayer.motionY = 0.005f;
			this.setMode("Hypixel");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
			NCP();
			mc.thePlayer.motionY = 0.005f;
			this.setMode("NCP");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Fast")) {
			this.setMode("Fast");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			this.setMode("AAC");
		}
		super.onUpdate();
	}

	@EventTarget
	public void onUpdate(EventBlock event) {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Fast")) {
			if (!mc.gameSettings.keyBindSneak.pressed && mc.thePlayer.posY > event.getPos().getY()) {
				if (isBlockValid(event.getBlock()))
					event.setAabb(new AxisAlignedBB(event.getPos(), event.getPos().add(2.0, 1.0, 2.0)));
			}
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			return;
		}
		if (!mc.gameSettings.keyBindJump.pressed && mc.thePlayer.fallDistance >= 0.1D
				&& mc.thePlayer.fallDistance < 4.0D) {
			if (isBlockValid(event.getBlock()))
				event.setAabb(new AxisAlignedBB(event.getPos(), event.getPos().add(1.0, -0.1, 1.0)));
		}

	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			if (this.isToggled() && event.getType() == EventType.PRE
					&& isBlockValid(mc.theWorld.getBlockState(mc.thePlayer.getPosition().add(0, -0.1, 0)).getBlock())
					&& isBlockValid(mc.theWorld.getBlockState(mc.thePlayer.getPosition()).getBlock())) {
				if (!mc.gameSettings.keyBindJump.pressed && mc.thePlayer.fallDistance >= 0.1D
						&& mc.thePlayer.onGround) {
					setSpeed(0.75);
				}
				if (mc.thePlayer.hurtTime > 0) {
					if (mc.thePlayer.onGround) {
						mc.thePlayer.addVelocity(0, 0.5, 0);
						mc.thePlayer.onGround = true;
					}
				}
				setSpeed(0.27);
				mc.thePlayer.onGround = false;
			}
		}
	}

	@EventTarget
	public void onEvent(EventPacket event) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			return;
		}
		if (event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer p = (C03PacketPlayer) event.getPacket();
			if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && this.isToggled()) {
				double speed = 0.00000001D;
				float f = getDirection();
				this.setPacketPlayerData(p, p.getPositionX() - MathHelper.sin(f) * speed, p.getPositionY(),
						p.getPositionZ() + MathHelper.cos(f) * speed, p.getYaw(), p.getPitch(), mc.thePlayer.onGround);
			}
		}
	}

	@EventTarget
	public void Fast(EventMove event) {
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Fast")) {
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
		this.Fast(event, forward, strafe, mx, mz);
	}

	public float getDirection() {
		float var1 = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.thePlayer.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.thePlayer.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;

		return var1;
	}

	public Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			Class superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw e;
			} else {
				return getField(superClass, fieldName);
			}
		}
	}

	public void setPacketPlayerData(C03PacketPlayer p, double x, double y, double z, float yaw, float pitch,
			boolean onGround) {
		try {
			this.getField(p.getClass(), "field_149479_a").setDouble(p, x);
		} catch (Exception e) {
			try {
				this.getField(p.getClass(), "x").setDouble(p, x);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void Fast(EventMove e, float forward, float strafe, double mx, double mz) {
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
			mc.timer.timerSpeed = 1F;
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

	public void NCP() {
		if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating()) {
				return;
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
			mc.thePlayer.motionY = 0;
			if (mc.gameSettings.keyBindSneak.pressed) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0400000001, mc.thePlayer.posZ);
			} else if (mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0400000001, mc.thePlayer.posZ);
			}
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000000001, mc.thePlayer.posZ);
		}
		mc.thePlayer.onGround = true;
		mc.thePlayer.setSprinting(false);
	}

	public void hypixel() {
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
			mc.thePlayer.motionY = 0;
			if (mc.thePlayer.ticksExisted % 3 == 0) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY - 0.0000000001, mc.thePlayer.posZ, true));
			}
			if (mc.gameSettings.keyBindSneak.pressed) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0400000001, mc.thePlayer.posZ);
			} else if (mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0400000001, mc.thePlayer.posZ);
			}
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000000001, mc.thePlayer.posZ);
		}
		mc.thePlayer.onGround = true;
		mc.thePlayer.setSprinting(false);
	}

	public boolean MovementInput() {
		return (mc.gameSettings.keyBindForward.getIsKeyPressed() || mc.gameSettings.keyBindBack.getIsKeyPressed()
				|| mc.gameSettings.keyBindLeft.getIsKeyPressed() || mc.gameSettings.keyBindRight.getIsKeyPressed()
				|| ((mc.gameSettings.keyBindSneak.getIsKeyPressed() && !mc.thePlayer.isCollidedVertically)
						|| mc.gameSettings.keyBindJump.getIsKeyPressed()));
	}

	public boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper
					.floor_double(mc.thePlayer.boundingBox.maxY) + 1; ++y) {
				for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper
						.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
					AxisAlignedBB boundingBox;
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (block == null || block instanceof BlockAir
							|| (boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
									mc.theWorld.getBlockState(new BlockPos(x, y, z)))) == null
							|| !mc.thePlayer.boundingBox.intersectsWith(boundingBox))
						continue;
					return true;
				}
			}
		}
		return false;
	}

	private boolean isBlockValid(Block block) {
		return block instanceof BlockAir;
	}

}
