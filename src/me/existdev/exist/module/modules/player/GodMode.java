package me.existdev.exist.module.modules.player;

import me.existdev.exist.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class GodMode extends Module {

	public GodMode() {
		super("GodMode", 0, Category.Player);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (mc.thePlayer.isSneaking()) {
			mc.thePlayer.sendQueue.netManager.sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY -= 3.53535829E8D, mc.thePlayer.posZ, true));
		}
		super.onUpdate();
	}

}
