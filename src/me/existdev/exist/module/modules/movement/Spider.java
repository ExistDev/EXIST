package me.existdev.exist.module.modules.movement;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.base.Equivalence.Wrapper;

import me.existdev.exist.events.EventBlock;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class Spider extends Module {

	private int state = 0;
	private TimeHelper timer = new TimeHelper();

	public Spider() {
		super("Spider", 0, Category.Movement);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}
		setMode("AAC");
		if (e.getType() == EventPreMotionUpdates.EventType.PRE)
			if (mc.thePlayer.motionY < 0 || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				state = 1;
		if (e.getType() != EventPreMotionUpdates.EventType.POST)
			return;

		if (mc.thePlayer.isCollidedHorizontally && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return;
		}
		if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
			if (mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.4001D;
			}
		}
	}

	@EventTarget
	public void onEvent(EventPacket event) {
		if (!this.isToggled()) {
			return;
		}
		if (event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer p = (C03PacketPlayer) event.getPacket();
			if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && this.isToggled() && state == 1) {
				double speed = 0.00000001D;
				float f = getDirection();
				this.setPacketPlayerData(p, p.getPositionX() - MathHelper.sin(f) * speed, p.getPositionY(),
						p.getPositionZ() + MathHelper.cos(f) * speed, p.getYaw(), p.getPitch(), mc.thePlayer.onGround);
				state = 2;
			}
		}
	}

	@EventTarget
	public void onCollide(EventBlock event) {
		if (!this.isToggled()) {
			return;
		}
		if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
			if (this.isToggled() && event.getPos().getY() < mc.thePlayer.posY)
				event.setAabb(new AxisAlignedBB(event.getPos(), event.getPos().add(1, 1, 1)));
		}
	}

	public float getDirection() {
		float var1 = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.thePlayer.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.thePlayer.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;

		return var1;
	}

	public Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			Class superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw e;
			} else {
				return getField(superClass, fieldName);
			}
		}
	}

	public void setPacketPlayerData(C03PacketPlayer p, double x, double y, double z, float yaw, float pitch,
			boolean onGround) {
		try {
			this.getField(p.getClass(), "field_149479_a").setDouble(p, x);
		} catch (Exception e) {
			try {
				this.getField(p.getClass(), "x").setDouble(p, x);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		try {
			this.getField(p.getClass(), "field_149477_b").setDouble(p, y);
		} catch (Exception e) {
			try {
				this.getField(p.getClass(), "y").setDouble(p, y);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			this.getField(p.getClass(), "field_149478_c").setDouble(p, z);
		} catch (Exception e) {
			try {
				this.getField(p.getClass(), "z").setDouble(p, z);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			this.getField(p.getClass(), "field_149476_e").setFloat(p, yaw);
		} catch (Exception e) {
			try {
				this.getField(p.getClass(), "yaw").setFloat(p, yaw);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			this.getField(p.getClass(), "field_149473_f").setFloat(p, pitch);
		} catch (Exception e) {
			try {
				this.getField(p.getClass(), "pitch").setFloat(p, pitch);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			this.getField(p.getClass(), "field_149474_g").setBoolean(p, onGround);
		} catch (Exception e) {
			try {
				this.getField(p.getClass(), "onGround").setBoolean(p, onGround);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
