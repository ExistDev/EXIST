package me.existdev.exist.module.modules.combat;

import java.util.ArrayList;
import java.util.Random;

import io.netty.util.Timer;
import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.TimerUtils;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.optifine.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class ChestSteal extends Module {

	ArrayList<String> Modes = new ArrayList<>();

	private BlockData blockData = null;
	private int posX;
	private int posY;
	private int posZ;
	private TimeHelper time;
	private TimeHelper invTime;
	private boolean clickBlock;
	private ArrayList<BlockPos> emptiedChests = new ArrayList();
	private ArrayList<BlockPos> clickedChests = new ArrayList();
	private static TimeHelper timer;
	int delay;
	int delay2;

	public ChestSteal() {
		super("ChestSteal", 0, Category.Combat);
		Modes.add("Priority");
		Modes.add("All");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "Priority", Modes));
		Exist.settingManager.addSetting(new Setting(this, "ChestAura", false));
		this.timer = new TimeHelper();
		this.time = new TimeHelper();
		this.invTime = new TimeHelper();
	}

	@Override
	public void onUpdate() {
		GuiChest chest;
		if (!this.isToggled()) {
			return;
		}
		++this.delay;
		++this.delay2;
		if (mc.currentScreen instanceof GuiChest
				&& (this.chestIsEmpty(chest = (GuiChest) mc.currentScreen) || inventoryIsFull())) {
			mc.thePlayer.closeScreen();
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("All")) {
			setMode("All");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Priority")) {
			setMode("Priority");
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("All")
				|| Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Priority")) {
			this.clickBlock = false;
			if (mc.currentScreen == null && !(mc.currentScreen instanceof GuiChest) && this.delay2 >= 3) {
				int radius;
				int y = radius = 30;
				while (y >= -radius) {
					int x = -radius;
					while (x < radius) {
						int z = -radius;
						while (z < radius) {
							this.posX = (int) Math.floor(mc.thePlayer.posX) + x;
							this.posY = (int) Math.floor(mc.thePlayer.posY) + y;
							this.posZ = (int) Math.floor(mc.thePlayer.posZ) + z;
							if (mc.thePlayer.getDistanceSq(mc.thePlayer.posX + (double) x,
									mc.thePlayer.posY + (double) y, mc.thePlayer.posZ + (double) z) <= 16.0) {
								Block block = this.getBlock(this.posX, this.posY, this.posZ);
								boolean blockChecks = block == Blocks.chest || block == Blocks.trapped_chest;
								blockChecks = blockChecks && this.canSeeBlock(this.posX, this.posY, this.posZ)
										&& !(block instanceof BlockAir);
								boolean bl = blockChecks = blockChecks
										&& (block.getBlockHardness(mc.theWorld, BlockPos.ORIGIN) != -1.0f
												|| mc.playerController.isInCreativeMode());
								if (blockChecks && mc.currentScreen == null) {
									this.clickBlock = true;
									this.blockData = new BlockData(new BlockPos(this.posX, this.posY, this.posZ),
											this.getFacing(new BlockPos(this.posX, this.posY, this.posZ)));
									if (!this.clickedChests.contains(this.blockData.position)) {
										float[] angles = this.getFacingRotations(this.posX, this.posY, this.posZ,
												this.blockData.face);
										float yaw = angles[0];
										float pitch = angles[1] + 10.0f;
										boolean silent = true;
										Entity var2 = mc.func_175606_aa();
										EnumFacing var3 = var2.func_174811_aO();
										int var4 = var3.getIndex();
										if (silent && Exist.settingManager.getSetting(this, "ChestAura").getBooleanValue()) {
											mc.thePlayer.sendQueue
													.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch,
															mc.thePlayer.onGround));
											mc.thePlayer.swingItem();
											mc.thePlayer.sendQueue.addToSendQueue(
													new C08PacketPlayerBlockPlacement(this.blockData.position, var4,
															mc.thePlayer.getHeldItem(), (float) mc.thePlayer.posX,
															(float) mc.thePlayer.posY, (float) mc.thePlayer.posZ));
										}
										this.time.reset();
										this.clickedChests.add(this.blockData.position);
										this.delay2 = 0;
										return;
									}
								}
							}
							++z;
						}
						++x;
					}
					--y;
				}
			}
		}
		if (mc.currentScreen instanceof GuiChest && !this.inventoryIsFull()
				&& !this.chestIsEmpty(chest = (GuiChest) mc.currentScreen)) {
			this.chestSlots.clear();
			this.findChestSlots(chest);
			if (!this.chestSlots.isEmpty()) {
				Random random = new Random();
				int randomSlot = random.nextInt(this.chestSlots.size());
				if (this.delay >= 2) {
					mc.playerController.windowClick(chest.inventorySlots.windowId, this.chestSlots.get(randomSlot), 0,
							1, mc.thePlayer);
					this.delay = 0;
				}
			} else {
				mc.thePlayer.closeScreen();
			}
		}
		super.onUpdate();
	}

	public boolean isContainerEmpty(Container container) {
		boolean temp = true;
		int i = 0;
		int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
		while (i < slotAmount) {
			if (container.getSlot(i).getHasStack()) {
				temp = false;
			}
			++i;
		}
		return temp;
	}

	@Override
	public void onEnable() {
		this.delay = 20;
		this.delay2 = 20;
		this.emptiedChests.clear();
		this.clickedChests.clear();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	private class BlockData {
		public BlockPos position;
		public EnumFacing face;

		public BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}

	static final class SwitchEnumFacing {
		static final int[] field_178907_a = new int[EnumFacing.values().length];

		static {
			try {
				SwitchEnumFacing.field_178907_a[EnumFacing.NORTH.ordinal()] = 1;
			} catch (NoSuchFieldError var0) {
				// empty catch block
			}
			try {
				SwitchEnumFacing.field_178907_a[EnumFacing.SOUTH.ordinal()] = 2;
			} catch (NoSuchFieldError var0_1) {
				// empty catch block
			}
			try {
				SwitchEnumFacing.field_178907_a[EnumFacing.WEST.ordinal()] = 3;
			} catch (NoSuchFieldError var0_2) {
				// empty catch block
			}
			try {
				SwitchEnumFacing.field_178907_a[EnumFacing.EAST.ordinal()] = 4;
			} catch (NoSuchFieldError var0_3) {
				// empty catch block
			}
		}

		SwitchEnumFacing() {
		}
	}

	public boolean inventoryIsFull() {
		int i = 9;
		while (i < 45) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack == null) {
				return false;
			}
			++i;
		}
		return true;
	}

	public boolean chestIsEmpty(GuiChest chest) {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return false;
		}
		int i = 0;
		while (i < chest.lowerChestInventory.getSizeInventory()) {
			ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);
			if (stack != null) {
				return false;
			}
			++i;
		}
		return true;
	}

	public ArrayList<Integer> chestSlots = new ArrayList();

	public void findChestSlots(GuiChest chest) {
		int i = 0;
		while (i < chest.lowerChestInventory.getSizeInventory()) {
			ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);
			if (stack != null
					&& (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("All")
							|| this.stackIsImportant(stack))) {
				this.chestSlots.add(i);
			}
			++i;
		}
	}

	public float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
		EntitySnowball temp = new EntitySnowball(mc.theWorld);
		temp.posX = (double) x + 0.5;
		temp.posY = (double) y + 0.5;
		temp.posZ = (double) z + 0.5;
		temp.posX += (double) facing.getDirectionVec().getX() * 0.25;
		temp.posY += (double) facing.getDirectionVec().getY() * 0.25;
		temp.posZ += (double) facing.getDirectionVec().getZ() * 0.25;
		return this.getAngles(temp);
	}

	public float[] getAngles(Entity e) {
		return new float[] { this.getYawChangeToEntity(e) + mc.thePlayer.rotationYaw,
				this.getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch };
	}

	public float getYawChangeToEntity(Entity entity) {
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))
				: (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))
						: Math.toDegrees(-Math.atan(deltaX / deltaZ)));
		return MathHelper.wrapAngleTo180_float(-mc.thePlayer.rotationYaw - (float) yawToEntity);
	}

	public float getPitchChangeToEntity(Entity entity) {
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double deltaY = entity.posY - 1.6 + (double) entity.getEyeHeight() - mc.thePlayer.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
	}

	public boolean stackIsImportant(ItemStack stack) {
		boolean valid = false;
		if (stack.getItem() instanceof ItemSword) {
			valid = true;
		}
		if (stack.getItem() instanceof ItemBlock) {
			valid = true;
		}
		if (stack.getItem() instanceof ItemAxe) {
			valid = true;
		}
		if (stack.getItem() instanceof ItemArmor) {
			valid = true;
		}
		if (stack.getItem() instanceof ItemPotion) {
			valid = true;
		}
		if (stack.getItem() instanceof ItemFood) {
			valid = true;
		}
		if (stack.getItem() instanceof ItemBow) {
			valid = true;
		}
		if (stack.getItem() instanceof ItemEnderPearl) {
			valid = true;
		}
		stack.getItem();
		if (Item.getIdFromItem(stack.getItem()) == 262) {
			valid = true;
		}
		return valid;
	}

	public boolean canSeeBlock(int x, int y, int z) {
		if (this.getFacing(new BlockPos(x, y, z)) != null) {
			return true;
		}
		return false;
	}

	public EnumFacing getFacing(BlockPos pos) {
		EnumFacing[] orderedValues;
		EnumFacing[] arrenumFacing = orderedValues = new EnumFacing[] { EnumFacing.UP, EnumFacing.NORTH,
				EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN };
		int n = arrenumFacing.length;
		int n2 = 0;
		while (n2 < n) {
			EnumFacing facing = arrenumFacing[n2];
			EntitySnowball temp = new EntitySnowball(mc.theWorld);
			temp.posX = (double) pos.getX() + 0.5;
			temp.posY = (double) pos.getY() + 0.5;
			temp.posZ = (double) pos.getZ() + 0.5;
			temp.posX += (double) facing.getDirectionVec().getX() * 0.5;
			temp.posY += (double) facing.getDirectionVec().getY() * 0.5;
			temp.posZ += (double) facing.getDirectionVec().getZ() * 0.5;
			if (mc.thePlayer.canEntityBeSeen(temp)) {
				return facing;
			}
			++n2;
		}
		return null;
	}

	public static Block getBlock(int x, int y, int z) {
		return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
}
