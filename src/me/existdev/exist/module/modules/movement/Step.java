package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Step extends Module {
	
	private ArrayList<String> Modes = new ArrayList<>();

	public Step() {
		super("Step", 0, Category.Movement);
		Modes.add("AAC");
		Modes.add("Normal");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.getModules(Speed.class).isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")
				&& this.isToggled()) {
			this.setMode("AAC");
			if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
					.equalsIgnoreCase("AAC")) {
				if (mc.thePlayer.isEating() && mc.thePlayer.onGround) {
					return;
				}
			}
			if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.4;
			}
			if (mc.thePlayer.hurtTime > 0) {
				return;
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Normal")
				&& this.isToggled()) {
			this.setMode("Normal");
			if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.38;
			}
		}
		super.onUpdate();
	}

	public Block getBlock(final double offset) {
		return this.getBlock(mc.thePlayer.getEntityBoundingBox().offset(0.0, offset, 0.0));
	}

	public Block getBlock(final AxisAlignedBB bb) {
		final int y = (int) bb.minY;
		for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
			for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
				final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null) {
					return block;
				}
			}
		}
		return null;
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		super.onDisable();
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
}
