package me.existdev.exist.module.modules.player;

import me.existdev.exist.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {

	public FastEat() {
		super("FastEat", 0, Category.Player);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		mc.rightClickDelayTimer = 1;
		if (mc.thePlayer.isUsingItem()) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.01D, mc.thePlayer.posZ, false));
		}
		super.onUpdate();
	}
}
