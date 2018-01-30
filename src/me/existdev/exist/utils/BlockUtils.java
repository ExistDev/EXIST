package me.existdev.exist.utils;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockUtils {

	public BlockPos position;
	public EnumFacing face;

	public BlockUtils(BlockPos position, EnumFacing face) {
		this.position = position;
		this.face = face;
	}

	public static BlockUtils getBlockData(BlockPos pos, List list) {
		return !list.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())
				? new BlockUtils(pos.add(0, -1, 0), EnumFacing.UP)
				: (!list.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())
						? new BlockUtils(pos.add(-1, 0, 0), EnumFacing.EAST)
						: (!list.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())
								? new BlockUtils(pos.add(1, 0, 0), EnumFacing.WEST)
								: (!list.contains(
										Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())
												? new BlockUtils(pos.add(0, 0, -1), EnumFacing.SOUTH)
												: (!list.contains(Minecraft.getMinecraft().theWorld
														.getBlockState(pos.add(0, 0, 1)).getBlock())
																? new BlockUtils(pos.add(0, 0, 1), EnumFacing.NORTH)
																: null))));
	}
}
