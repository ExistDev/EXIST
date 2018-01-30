package me.existdev.exist.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BedFucker extends Module {

	private int xOffset;
	private int zOffset;
	private int yOffset;

	private TimeHelper time = new TimeHelper();

	public BedFucker() {
		super("BedFucker", 0, Category.World);
		Exist.settingManager.addSetting(new Setting(this, "Range", 5, 0, 15, true));
	}

	@EventTarget
	public void onPreMotion(EventPreMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}
		int reach = (int) Exist.settingManager.getSetting(this, "Range").getCurrentValue();
		for (this.xOffset = -reach; this.xOffset < reach; ++this.xOffset) {
			for (this.zOffset = -reach; this.zOffset < reach; ++this.zOffset) {
				for (this.yOffset = reach; this.yOffset > -reach; --this.yOffset) {
					double x = mc.thePlayer.posX + (double) this.xOffset;
					double y = mc.thePlayer.posY + (double) this.yOffset;
					double z = mc.thePlayer.posZ + (double) this.zOffset;
					int id = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());

					if (id == 26 || id == 122) {

						float[] rotations = this.getBlockRotations(new BlockPos(x, y, z));

						e.setYaw(rotations[0]);
						e.setPitch(rotations[1]);
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

	private void destoryBlock(BlockPos pos) {
		mc.thePlayer.sendQueue.addToSendQueue(
				new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, mc.thePlayer.func_174811_aO()));
		mc.thePlayer.sendQueue.addToSendQueue(
				new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, mc.thePlayer.func_174811_aO()));

	}

	public float[] getBlockRotations(BlockPos block) {
		EntitySnowball p_70625_1_ = new EntitySnowball(mc.theWorld, block.getX(), block.getY(), block.getZ());
		double var4 = p_70625_1_.posX - mc.thePlayer.posX;
		double var8 = p_70625_1_.posZ - mc.thePlayer.posZ;

		double var6;

		var6 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0D
				- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());

		double var141 = (double) MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
		float var13 = (float) (-(Math.atan2(var6, var141) * 180.0D / Math.PI));
		float pitch = updateRotation1(mc.thePlayer.rotationPitch, var13, 1000);
		float yaw = updateRotation1(mc.thePlayer.rotationYaw, var12, 1000);
		return new float[] { var12, var13 };

	}

	private static float updateRotation1(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
		if (var4 > p_70663_3_) {
			var4 = p_70663_3_;
		}

		if (var4 < -p_70663_3_) {
			var4 = -p_70663_3_;
		}

		return p_70663_1_ + var4;
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		int reach = (int) Exist.settingManager.getSetting(this, "Range").getCurrentValue();
		for (this.xOffset = -reach; this.xOffset < reach; ++this.xOffset) {
			for (this.zOffset = -reach; this.zOffset < reach; ++this.zOffset) {
				for (this.yOffset = reach; this.yOffset > -reach; --this.yOffset) {
					double x = mc.thePlayer.posX + (double) this.xOffset;
					double y = mc.thePlayer.posY + (double) this.yOffset;
					double z = mc.thePlayer.posZ + (double) this.zOffset;
					int id = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());

					if (id == 26 || id == 122) {
						if (time.hasReached(100L)) {
							this.destoryBlock(new BlockPos(x, y, z));
							mc.thePlayer.swingItem();

						}
					}
				}
			}
		}
	}
}
