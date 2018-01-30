package me.existdev.exist.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.events.EventPreMotionUpdates.EventType;
import me.existdev.exist.module.Module;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.ItemFood;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class FastStair extends Module {

	public FastStair() {
		super("FastStair", 0, Category.Movement);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
				.equalsIgnoreCase("AAC")) {
			if (mc.thePlayer.isEating() && mc.thePlayer.onGround) {
				return;
			}
		}
		setMode("AAC");
		super.onUpdate();
	}

	private float speed = 0.0f;

	@EventTarget
	public void onMove(EventPreMotionUpdates e) {
		if (!this.isToggled())
			return;
		if (e.getType() != EventType.PRE)
			return;

		BlockPos pos = new BlockPos(Math.floor(mc.thePlayer.posX), Math.ceil(mc.thePlayer.posY),
				Math.floor(mc.thePlayer.posZ));

		if ((mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockStairs || mc.theWorld
				.getBlockState(mc.thePlayer.getPosition().add(0, -0.5, 0)).getBlock() instanceof BlockSlab)
				&& mc.thePlayer.onGround) {
			if (mc.thePlayer.movementInput.moveForward > 0) {
				if (mc.theWorld.getBlockState(pos.add(0, -0.5, 0)).getBlock() instanceof BlockSlab)
					speed += 0.025;
				else
					speed += 0.075;
				if (speed > 0.95f)
					speed = 0.95f;
			}
		} else {
			if (mc.thePlayer.onGround)
				speed -= 0.05;
			else {
				speed -= 0.33;
			}

			if (speed < 0f)
				speed = 0f;
		}
		if (mc.thePlayer.movementInput.moveForward == 0)
			speed = 0;

		if (speed != 0 && mc.thePlayer.onGround) {
			float f = getDirection();
			mc.thePlayer.motionX -= (double) (MathHelper.sin(f) * speed);
			mc.thePlayer.motionZ += (double) (MathHelper.cos(f) * speed);
		}
	}

	private float getDirection() {
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

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
