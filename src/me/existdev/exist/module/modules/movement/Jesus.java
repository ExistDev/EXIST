package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Jesus extends Module {

	public boolean sink = false;
	ArrayList<String> Modes = new ArrayList<>();
	private int delay = 0;
	TimeHelper ticks = new TimeHelper();

	public Jesus() {
		super("Jesus", 0, Category.Movement);
		Modes.add("NCPFast");
		Modes.add("NCP");
		Modes.add("AAC");
		Modes.add("Solid");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "NCPFast", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		String option = Exist.settingManager.getSetting(this, "Mode").getCurrentOption();
		if (option.equalsIgnoreCase("AAC")) {
			if (this.mc.thePlayer.isInWater()) {
				this.mc.thePlayer.motionX *= 1.13D;
				this.mc.thePlayer.motionZ *= 1.13D;
				this.mc.thePlayer.motionY += 0.03D;
			}
		} else {
			BlockPos bp = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ);
			if (this.mc.theWorld.getBlockState(bp).getBlock() == Blocks.water && this.ticks.isDelayComplete(377L)) {
				this.mc.thePlayer.motionY = 0.2805D;
				this.ticks.reset();
			}
		}
		if (option.equalsIgnoreCase("NCPFast")) {
			this.setMode("NCPFast");
			if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
					.equalsIgnoreCase("AAC")) {
				if (mc.thePlayer.isEating() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood) {
					return;
				}
			}
			if (Jesus.isInLiquid()) {
				boolean bl2 = this.sink = !this.sink;
				double d2 = this.mc.thePlayer.isSneaking() ? -0.12
						: (this.mc.gameSettings.keyBindJump.pressed ? 0.4
								: (this.mc.thePlayer.motionY = this.sink ? 0.05 : 0.12));
				setSpeed(0.271);
				if (!mc.thePlayer.isMoving()) {
					this.mc.thePlayer.motionX = 0.0;
					this.mc.thePlayer.motionZ = 0.0;
				}
			}
		}
		if (option.equalsIgnoreCase("NCP")) {
			this.setMode("NCP");
			if (Exist.settingManager.getSetting(Exist.getModules(NoSlowdown.class), "Mode").getCurrentOption()
					.equalsIgnoreCase("AAC")) {
				if (mc.thePlayer.isEating()) {
					return;
				}
			}
			if (Jesus.isInLiquid()) {
				boolean bl2 = this.sink = !this.sink;
				double d2 = this.mc.thePlayer.isSneaking() ? -0.12
						: (this.mc.gameSettings.keyBindJump.pressed ? 0.4
								: (this.mc.thePlayer.motionY = this.sink ? 0.05 : 0.12));
				if (!mc.thePlayer.isMoving()) {
					this.mc.thePlayer.motionX = 0.0;
					this.mc.thePlayer.motionZ = 0.0;
				}
			}
		}
		if (option.equalsIgnoreCase("Solid")) {
			this.setMode("Solid");
			if (isInLiquid()) {
				if (!mc.gameSettings.keyBindSneak.pressed) {
					mc.thePlayer.motionY = 0.085D;
				}
			}
		}
		super.onUpdate();
	}

	public class TimeHelper {
		private long lastMs;

		public boolean isDelayComplete(long delay) {
			return System.currentTimeMillis() - this.lastMs > delay;
		}

		public void reset() {
			this.lastMs = System.currentTimeMillis();
		}

		public long getLastMs() {
			return this.lastMs;
		}

		public void setLastMs(int i) {
			this.lastMs = System.currentTimeMillis() + (long) i;
		}
	}

	@EventTarget
	public void onPacketSend(EventPacket event) {
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Solid")) {
			if ((event.getPacket() instanceof C03PacketPlayer)) {
				C03PacketPlayer player = (C03PacketPlayer) event.getPacket();
				if ((isOnLiquid())) {
					this.delay += 1;
					if (this.delay >= 4) {
						player.y -= 0.001;
						delay = 0;
					}
				}
			}
		}
	}

	public static boolean isInLiquid() {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return false;
		}
		boolean inLiquid = false;
		int y2 = (int) Minecraft.getMinecraft().thePlayer.boundingBox.minY;
		int x2 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX);
		while (x2 < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1) {
			int z2 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
			while (z2 < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1) {
				Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
				if (block != null && !(block instanceof BlockAir)) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					inLiquid = true;
				}
				++z2;
			}
			++x2;
		}
		return inLiquid;
	}

	public static boolean isOnLiquid() {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return false;
		}
		boolean onLiquid = false;
		int y2 = (int) Minecraft.getMinecraft().thePlayer.boundingBox.offset((double) 0.0, (double) -0.01,
				(double) 0.0).minY;
		int x2 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX);
		while (x2 < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1) {
			int z2 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
			while (z2 < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1) {
				Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
				if (block != null && !(block instanceof BlockAir)) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					onLiquid = true;
				}
				++z2;
			}
			++x2;
		}
		return onLiquid;
	}

	public static Block getBlock(int x, int y, int z) {
		return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
}
