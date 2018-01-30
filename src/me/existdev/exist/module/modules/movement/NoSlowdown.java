package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;

public class NoSlowdown extends Module {

	ArrayList<String> Modes = new ArrayList<>();
	static Minecraft mc = Minecraft.getMinecraft();

	public NoSlowdown() {
		super("NoSlowdown", 0, Category.Movement);
		Modes.add("NCP");
		Modes.add("AAC");
		Modes.add("Hypixel");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			this.setMode("AAC");
			if (mc.thePlayer.isBlocking()) {
				mc.thePlayer.sendQueue.addToSendQueue(
						new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating() && mc.thePlayer.onGround) {
				setSpeed(0.08);
			}
			if (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow
					&& mc.thePlayer.onGround) {
				setSpeed(0.08);
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
			this.setMode("NCP");
			if (mc.thePlayer.isBlocking()) {
				mc.thePlayer.sendQueue.addToSendQueue(
						new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
			this.setMode("Hypixel");
			if (!this.SlowDown()) {
				return;
			}
			MovementInput movementInput = mc.thePlayer.movementInput;
			float forward = MovementInput.moveForward;
			float strafe = MovementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if (forward == 0.0f && strafe == 0.0f) {
				strafe = 0.0f;
				forward = 0.0f;
			} else if (forward != 0.0f) {
				if (strafe >= 1.0f) {
					yaw += (float) (forward > 0.0f ? -45 : 45);
					strafe = 0.0f;
				} else if (strafe <= -1.0f) {
					yaw += (float) (forward > 0.0f ? 45 : -45);
					strafe = 0.0f;
				}
				if (forward > 0.0f) {
					forward = 1.0f;
				} else if (forward < 0.0f) {
					forward = -1.0f;
				}
			}
			double moveSpeed = this.getBaseMoveSpeed();
			double mx = Math.cos(Math.toRadians(yaw + 90.0f));
			double mz = Math.sin(Math.toRadians(yaw + 90.0f));
			double motionX = (double) forward * moveSpeed * mx + (double) strafe * moveSpeed * mz;
			double motionZ = (double) forward * moveSpeed * mz - (double) strafe * moveSpeed * mx;
			mc.thePlayer.motionX = (double) forward * moveSpeed * mx + (double) strafe * moveSpeed * mz;
			mc.thePlayer.motionZ = (double) forward * moveSpeed * mz - (double) strafe * moveSpeed * mx;
		}
		super.onUpdate();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.16;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
		}
		return baseSpeed;
	}

	public static boolean SlowDown() {
		boolean ShouldNoSlow = false;
		if (getBlockAtPosC(mc.thePlayer, 0.30000001192092896, 0.10000000149011612,
				0.30000001192092896) instanceof BlockSoulSand
				&& getBlockAtPosC(mc.thePlayer, -0.30000001192092896, 0.10000000149011612,
						-0.30000001192092896) instanceof BlockSoulSand) {
			ShouldNoSlow = true;
		}
		if (mc.thePlayer.isInWeb) {
			ShouldNoSlow = true;
		}
		if (mc.thePlayer.isInWater()) {
			ShouldNoSlow = false;
		}
		return ShouldNoSlow;
	}

	public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
		return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
	}

	public static Block getBlock(BlockPos pos) {
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	}

}
