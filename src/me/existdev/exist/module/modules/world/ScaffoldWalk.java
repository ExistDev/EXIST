package me.existdev.exist.module.modules.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ScaffoldWalk extends Module {

	public EnumFacing enumFacing;
	public BlockPos pos;
	int delay;
	BlockData scaffoldHelper;
	public static int placedblocks = 0;
	public static boolean looked = false;
	private List<Block> invalid = Arrays.asList(new Block[] { Blocks.air, Blocks.water, Blocks.fire,
			Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table,
			Blocks.chest, Blocks.ender_chest, Blocks.gravel });
	private Timer timer = new Timer();
	private Timer timerMotion = new Timer();
	private Timer timerMotion2 = new Timer();
	private BlockData blockData;
	boolean placing;

	public ScaffoldWalk() {
		super("ScaffoldWalk", 0, Category.World);
		Exist.settingManager.addSetting(new Setting(this, "Delay", 5.0D, 1.0D, 20.0D, true));
		Exist.settingManager.addSetting(new Setting(this, "Sprint", false));
		Exist.settingManager.addSetting(new Setting(this, "Sneak", false));
		Exist.settingManager.addSetting(new Setting(this, "NoSwing", false));
		Exist.settingManager.addSetting(new Setting(this, "Count", false));
		Exist.settingManager.addSetting(new Setting(this, "NCP", false));
		Exist.settingManager.addSetting(new Setting(this, "Switch", false));
	}

	@Override
	public void onEnable() {
		if (mc.thePlayer != null && mc.theWorld != null) {
			this.scaffoldHelper = null;
			this.delay = 0;
			placedblocks = 0;
		}
		super.onEnable();
	}

	@Override
	public void onDisable() {
		this.scaffoldHelper = null;
		if (Exist.settingManager.getSetting(this, "Sneak").getBooleanValue()) {
			mc.gameSettings.keyBindSneak.pressed = false;
		}
		placedblocks = 0;
		mc.timer.timerSpeed = 1.0F;
		super.onDisable();
	}

	@EventTarget
	public void onPreMotionUpdate(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "NCP").getBooleanValue()) {
			return;
		}
		if (this.isToggled() && !this.looked) {
			this.looked = true;
			float nextYaw = 0.0F;
			float nextPitch = 0.0F;
			if (mc.thePlayer.func_174811_aO() == EnumFacing.WEST) {
				nextYaw = 0.0F;
			}
			if (mc.thePlayer.func_174811_aO() == EnumFacing.EAST) {
				nextYaw = -180.0F;
			}
			if (mc.thePlayer.func_174811_aO() == EnumFacing.SOUTH) {
				nextYaw = -90.0F;
			}
			if (mc.thePlayer.func_174811_aO() == EnumFacing.NORTH) {
				nextYaw = 90.0F;
			}
			nextPitch = 82.8F;
			if (mc.thePlayer.isSneaking()) {
				nextPitch = 82.5F;
			}
			C03PacketPlayer.yaw = nextYaw;
			event.setYaw(nextYaw);
			C03PacketPlayer.pitch = nextPitch;
			event.setPitch(nextPitch);
		}
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}

		if (Exist.settingManager.getSetting(this, "NCP").getBooleanValue()) {
			this.setMode("NCP");
			return;
		}

		if (Exist.settingManager.getSetting(this, "Sneak").getBooleanValue()) {
			mc.gameSettings.keyBindSneak.pressed = false;
		}
		if (!Exist.settingManager.getSetting(this, "Sprint").getBooleanValue()) {
			mc.thePlayer.setSprinting(false);
		}
		this.setMode("AAC");

		BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
		if (mc.thePlayer.getCurrentEquippedItem() != null
				&& mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock
				&& mc.theWorld.getBlockState(pos) != null && mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
			this.scaffoldHelper = this.getBlock(pos);
			if (this.scaffoldHelper != null) {
				++this.delay;
				if ((double) this.delay == Exist.settingManager.getSetting(this, "Delay").getCurrentValue()) {
					mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
							this.scaffoldHelper.blockPos, this.scaffoldHelper.enumFacing,
							new Vec3((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()));
					if (Exist.settingManager.getSetting(this, "NoSwing").getBooleanValue()) {
						mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
					} else {
						mc.thePlayer.swingItem();
					}
					if (mc.gameSettings.keyBindJump.pressed) {
						mc.thePlayer.rotationPitch = 90F;
						mc.timer.timerSpeed = 1.13F;
					} else {
						mc.timer.timerSpeed = 1.0F;
					}
					if (mc.gameSettings.keyBindJump.pressed && mc.thePlayer.onGround) {
						mc.thePlayer.motionY = 0.3851F;

					}

					++placedblocks;
					if (Exist.settingManager.getSetting(this, "Sneak").getBooleanValue()) {
						mc.gameSettings.keyBindSneak.pressed = true;
					}

					this.delay = 0;
				}
			}
		}
	}

	@EventTarget
	public void onPacket(EventPacket event) {
		String direction = mc.func_175606_aa().func_174811_aO().name();
		if (Exist.settingManager.getSetting(this, "NCP").getBooleanValue()) {
			return;
		}
		if (event.getPacket() instanceof Packet) {
			Packet packet = event.getPacket();
			if (packet instanceof C03PacketPlayer) {
				C03PacketPlayer p = (C03PacketPlayer) packet;
				C03PacketPlayer.pitch = 82.0F;
				if (direction.equalsIgnoreCase("NORTH")) {
					C03PacketPlayer.yaw = 360.0F;
				} else if (direction.equalsIgnoreCase("SOUTH")) {
					C03PacketPlayer.yaw = 180.0F;
				} else if (direction.equalsIgnoreCase("WEST")) {
					C03PacketPlayer.yaw = 270.0F;
				} else if (direction.equalsIgnoreCase("EAST")) {
					C03PacketPlayer.yaw = 90.0F;
				}
			}
		}
	}

	// NCP

	@EventTarget
	public void onPre(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "NCP").getBooleanValue()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Sprint").getBooleanValue()) {
			mc.thePlayer.setSprinting(false);
		}
		blockData = null;

		if (!mc.thePlayer.isSneaking()) {
			BlockPos blockBelow1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
			if (mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {

				blockData = Exist.settingManager.getSetting(this, "NCP").getBooleanValue()
						? getBlockData(blockBelow1, invalid) : getBlockData1(blockBelow1);
				if (blockData != null) {
					float yaw = aimAtLocation((double) blockData.blockPos.getX(), (double) blockData.blockPos.getY(),
							(double) blockData.blockPos.getZ(), blockData.enumFacing)[0];
					float pitch = aimAtLocation((double) blockData.blockPos.getX(), (double) blockData.blockPos.getY(),
							(double) blockData.blockPos.getZ(), blockData.enumFacing)[1];

					event.setYaw(yaw);
					event.setPitch(pitch);
				}
			}
		}
	}

	@EventTarget
	public void onPost(EventPreMotionUpdates post) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "NCP").getBooleanValue()) {
			return;
		}
		if (blockData != null) {
			if (!(Exist.settingManager.getSetting(this, "Switch").getBooleanValue() ? this.getBlockAmount() != 0
					: (mc.thePlayer.getCurrentEquippedItem() != null
							&& mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)))
				return;
			if (timer.hasTimeElapsed(1000)) {
				mc.rightClickDelayTimer = 0;
				final String playerPos = new StringBuilder(String.valueOf(mc.thePlayer.posY)).toString();
				if (mc.thePlayer.movementInput.jump) {
					mc.thePlayer.motionY = 0.42;
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionZ = 0;
					if (timerMotion.hasTimeElapsed(1500)) {
						mc.thePlayer.motionY = -0.28;
						timerMotion.reset();
						if (timerMotion.hasTimeElapsed(2)) {
							mc.thePlayer.motionY = 0.42;
						}
					}
				}

				int heldItem = mc.thePlayer.inventory.currentItem;
				BlockPos bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
				for (int i = 0; i < 9; i++) {
					if (mc.thePlayer.inventory.getStackInSlot(i) != null
							&& mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0
							&& mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock
							&& Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
						mc.thePlayer.sendQueue
								.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
						break;
					}
				}
				if (Exist.settingManager.getSetting(this, "NCP").getBooleanValue()) {
					if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
							blockData.blockPos, blockData.enumFacing,
							new Vec3(
									(double) blockData.blockPos.getX() + rando05(0)
											+ blockData.enumFacing.getDirectionVec().getX() * rando05(1),
									(double) blockData.blockPos.getY() + rando05(2)
											+ blockData.enumFacing.getDirectionVec().getY() * rando05(3),
									(double) blockData.blockPos.getZ() + rando05(4)
											+ blockData.enumFacing.getDirectionVec().getZ() * rando05(5))))
						if (Exist.settingManager.getSetting(this, "NoSwing").getBooleanValue()) {
							mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
						} else {
							mc.thePlayer.swingItem();
						}
				} else {
					if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
							blockData.blockPos, blockData.enumFacing, new Vec3((double) blockData.blockPos.getX(),
									(double) blockData.blockPos.getY(), (double) blockData.blockPos.getZ())))
						if (Exist.settingManager.getSetting(this, "NoSwing").getBooleanValue()) {
							mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
						} else {
							mc.thePlayer.swingItem();
						}
				}
				// mc.thePlayer.inventory.currentItem = heldItem;
				if (Exist.settingManager.getSetting(this, "Switch").getBooleanValue())
					mc.thePlayer.sendQueue
							.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = heldItem));
				mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
			}
		}
	}

	public static float rando05(long seed) {
		seed = System.currentTimeMillis() + seed;
		return 0.30000000000f + (new Random(seed).nextInt(70000000) / 100000000.000000000000f) + 0.00000001458745f;
	}

	// Helper

	public BlockData getBlock(BlockPos pos) {
		if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		} else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		} else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		} else {
			return mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air
					? new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null;
		}
	}

	public BlockData getBlockData1(BlockPos pos) {
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}
		final BlockPos add = pos.add(-1, 0, 0);
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
			return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
			return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
			return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
		}
		final BlockPos add2 = pos.add(1, 0, 0);
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
			return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
			return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
		}
		final BlockPos add3 = pos.add(0, 0, -1);
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
			return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
			return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
		}
		final BlockPos add4 = pos.add(0, 0, 1);
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
			return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
			return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!invalid.contains(Minecraft.getMinecraft().theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
			return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockData blockData = null;

		return blockData;
	}

	private BlockData getBlockData(BlockPos pos, List list) {
		return !list.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())
				? new BlockData(pos.add(0, -1, 0), EnumFacing.UP)
				: (!list.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())
						? new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST)
						: (!list.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())
								? new BlockData(pos.add(1, 0, 0), EnumFacing.WEST)
								: (!list.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())
										? new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH)
										: (!list.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())
												? new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null))));
	}

	public class BlockData {
		public BlockPos blockPos;
		public EnumFacing enumFacing;

		public BlockData(BlockPos blockPos, EnumFacing enumFacing) {
			this.blockPos = blockPos;
			this.enumFacing = enumFacing;
		}
	}

	private int getBlockAmount() {
		int n = 0;
		for (int i = 9; i < 45; ++i) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock
					&& ((ItemBlock) stack.getItem()).getBlock().isCollidable()) {
				n += stack.stackSize;
			}
		}
		return n;
	}

	private float[] aimAtLocation(double x, double y, double z, EnumFacing facing) {
		EntitySnowball temp = new EntitySnowball(mc.theWorld);
		temp.posX = x + 0.5D;
		temp.posY = y - 2.70352523530000001D;
		temp.posZ = z + 0.5D;
		temp.posX += (double) facing.getDirectionVec().getX() * 0.25D;
		temp.posY += (double) facing.getDirectionVec().getY() * 0.25D;
		temp.posZ += (double) facing.getDirectionVec().getZ() * 0.25D;
		return aimAtLocation(temp.posX, temp.posY, temp.posZ);
	}

	private float[] aimAtLocation(double positionX, double positionY, double positionZ) {
		double x = positionX - mc.thePlayer.posX;
		double y = positionY - mc.thePlayer.posY;
		double z = positionZ - mc.thePlayer.posZ;
		double distance = (double) MathHelper.sqrt_double(x * x + z * z);
		return new float[] { (float) (Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F,
				(float) (-(Math.atan2(y, distance) * 180.0D / 3.141592653589793D)) };
	}

	public final class Timer {
		private long time;

		public Timer() {
			this.time = System.nanoTime() / 1000000L;
		}

		public boolean hasTimeElapsed(final long time, final boolean reset) {
			if (this.time() >= time) {
				if (reset)
					this.reset();
				return true;
			}
			return false;
		}

		public boolean hasTimeElapsed(final long time) {
			if (this.time() >= time)
				return true;
			return false;
		}

		public boolean hasTicksElapsed(int ticks) {
			if (this.time() >= (1000 / ticks) - 50)
				return true;
			return false;
		}

		public boolean hasTicksElapsed(int time, int ticks) {
			if (this.time() >= (time / ticks) - 50)
				return true;
			return false;
		}

		public long time() {
			return System.nanoTime() / 1000000L - this.time;
		}

		public void reset() {
			this.time = System.nanoTime() / 1000000L;
		}
	}
}
