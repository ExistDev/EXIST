package me.existdev.exist.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Tower extends Module {

	public Tower() {
		super("Tower", 0, Category.World);
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}
		BlockPos playerBlock = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY,
				mc.thePlayer.posZ);
		if (mc.gameSettings.keyBindJump.pressed && mc.theWorld.isAirBlock(playerBlock.add(0, -1, 0))) {
			mc.thePlayer.sendQueue.addToSendQueue(new C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90.0F, true));
			this.jump();
			if (this.isValidBlock(playerBlock.add(0, -2, 0))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.UP);
			} else if (this.isValidBlock(playerBlock.add(-1, -1, 0))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
			} else if (this.isValidBlock(playerBlock.add(1, -1, 0))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
			} else if (this.isValidBlock(playerBlock.add(0, -1, -1))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
			} else if (this.isValidBlock(playerBlock.add(0, -1, 1))) {
				this.place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
			}
		}
	}

	public void jump() {
		mc.thePlayer.motionY = 0.11999998688697815D;
	}

	private boolean isValidBlock(BlockPos pos) {
		Block b = mc.theWorld.getBlockState(pos).getBlock();
		mc.thePlayer.setSneaking(true);
		return !(b instanceof BlockLiquid) && b.getMaterial() != Material.air;
	}

	public void place(BlockPos pos, EnumFacing face) {
		if (face == EnumFacing.UP) {
			pos = pos.add(0, -1, 0);
		} else if (face == EnumFacing.NORTH) {
			pos = pos.add(0, 0, 1);
		} else if (face == EnumFacing.SOUTH) {
			pos = pos.add(0, 0, -1);
		} else if (face == EnumFacing.EAST) {
			pos = pos.add(-1, 0, 0);
		} else if (face == EnumFacing.WEST) {
			pos = pos.add(1, 0, 0);
		}

		if (Minecraft.thePlayer.getHeldItem() != null
				&& Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
			Minecraft.thePlayer.swingItem();
			mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld,
					Minecraft.thePlayer.getHeldItem(), pos, face, new Vec3(0.1D, 0.1D, 0.1D));
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.rightClickDelayTimer = 6;
		super.onDisable();
	}
}
