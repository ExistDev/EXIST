package me.existdev.exist.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.events.EventPreMotionUpdates.EventType;
import me.existdev.exist.module.Module;
import net.minecraft.block.BlockSlime;
import net.minecraft.util.BlockPos;

public class SlimeJump extends Module {

	public SlimeJump() {
		super("SlimeJump", 0, Category.Movement);
	}

	@EventTarget
	public void onMove(EventPreMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}
		if (e.getType() != EventType.PRE) {
			return;
		}
		BlockPos pos = new BlockPos(Math.floor(mc.thePlayer.posX), Math.ceil(mc.thePlayer.posY),
				Math.floor(mc.thePlayer.posZ));
		if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockSlime && mc.thePlayer.onGround) {
			mc.thePlayer.motionY = 1.5;
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
}
