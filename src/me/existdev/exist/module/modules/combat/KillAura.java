package me.existdev.exist.module.modules.combat;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.events.EventPreMotionUpdates.EventType;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;

public class KillAura extends Module {

	ArrayList<String> Modes = new ArrayList<>();
	TimeHelper time = new TimeHelper();

	private boolean isDead;
	private Entity currentEntity;
	public static List<EntityLivingBase> entities;
	public static EntityLivingBase target;

	public KillAura() {
		super("KillAura", 0, Category.Combat);
		Modes.add("Switch");
		Modes.add("AAC");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", Modes));
		Exist.settingManager.addSetting(new Setting(this, "Range", 4.1D, 0.1D, 100.0D, false));
		Exist.settingManager.addSetting(new Setting(this, "Delay", 30.0D, 10.0D, 100.0D, true));
		Exist.settingManager.addSetting(new Setting(this, "Swing", true));
		this.entities = new ArrayList<EntityLivingBase>();
	}

	@EventTarget
	public void AAC(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			return;
		}
		setMode("AAC");
		List list = mc.theWorld.loadedEntityList;
		for (int k = 0; k < list.size(); k++) {
			if (((Entity) list.get(k)).getName() != mc.thePlayer.getName()) {
				Entity entityplayer = (Entity) list.get(1);
				entityplayer = (Entity) list.get(k);
				if (isValid(entityplayer)) {
					if (entityplayer.isDead) {
						this.isDead = true;
					} else {
						this.isDead = false;
					}
					if (!this.isDead) {
						this.currentEntity = entityplayer;
					}
					mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(currentEntity, Action.ATTACK));
					mc.effectRenderer.func_178926_a(this.currentEntity, EnumParticleTypes.CRIT);
					if (Exist.settingManager.getSetting(this, "Swing").getBooleanValue()) {
						mc.thePlayer.swingItem();
					} else {
						mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
					}
				}
			}
		}
	}

	@EventTarget
	public void Switch(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Switch")) {
			return;
		}
		setMode("Switch");
		if (event.getType() == EventType.PRE) {
			if (this.entities.isEmpty()) {
				for (final Object object : Minecraft.theWorld.loadedEntityList) {
					if (object instanceof EntityLivingBase) {
						final EntityLivingBase entity = (EntityLivingBase) object;
						if (!isValid(entity)) {
							continue;
						}
						this.entities.add(entity);
					}
				}
			}
			if (!this.entities.isEmpty()) {
				double distance = Double.MAX_VALUE;
				EntityLivingBase entity = null;
				for (int i = 0; i < this.entities.size(); ++i) {
					final EntityLivingBase e = this.entities.get(i);
					if (!isValid(e)) {
						this.entities.remove(e);
					}
					if (e.getDistanceToEntity(mc.thePlayer) < distance
							&& e.getDistanceToEntity(mc.thePlayer) < Exist.settingManager.getSetting(this, "Range")
									.getCurrentValue()) {
						entity = e;
						distance = e.getDistanceToEntity(mc.thePlayer);
					}
				}
				this.target = entity;
			}
			if (isValid(this.target)) {
				event.setYaw(getRotations(this.target)[0]);
				event.setPitch(getRotations(this.target)[1]);
			}
		} else if (isValid(this.target)) {
			float speed = (float) Exist.settingManager.getSetting(this, "Delay").getCurrentValue() * 2;

			float kill = (float) (1000.0D / speed);

			if (this.target != null && this.time.hasReached((long) kill)) {
				attack(this.target);
				this.entities.remove(this.target);
				this.target = null;
				this.time.reset();
			}
		}
	}

	private boolean isValid(Entity e) {
		float f = mc.thePlayer.getDistanceToEntity(e);

		if (e instanceof EntityLivingBase && f <= Exist.settingManager.getSetting(this, "Range").getCurrentValue()
				&& time.hasReached((long) Exist.settingManager.getSetting(this, "Delay").getCurrentValue()) && !e.isDead
				&& !mc.thePlayer.isDead && !e.isInvisible()) {
			time.reset();
			return true;
		} else {
			return false;
		}
	}

	public boolean isValid(final EntityLivingBase entity) {
		return entity != null && entity.isEntityAlive() && this.isEntityInFov(entity, 360) && entity != mc.thePlayer
				&& ((entity instanceof EntityPlayer) || (entity instanceof EntityAnimal)
						|| (entity instanceof EntityMob))
				&& entity.getDistanceToEntity(mc.thePlayer) <= (mc.thePlayer.canEntityBeSeen(entity)
						? Exist.settingManager.getSetting(this, "Range").getCurrentValue() : 3.0)
				&& (!entity.isInvisible());
	}

	public boolean isEntityInFov(final EntityLivingBase entity, double angle) {
		angle *= 0.5;
		final double angleDifference = getAngleDifference(mc.thePlayer.rotationYaw, getRotations(entity)[0]);
		return (angleDifference > 0.0 && angleDifference < angle)
				|| (-angle < angleDifference && angleDifference < 0.0);
	}

	public static float getAngleDifference(float direction, float rotationYaw) {
		float phi = Math.abs(rotationYaw - direction) % 360.0F;
		float distance = phi > 180.0F ? 360.0F - phi : phi;
		return distance;
	}

	public void attack(final EntityLivingBase entity) {
		mc.thePlayer.swingItem();
		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
	}

	public static float[] getRotations(Entity entity) {
		if (entity == null) {
			return null;
		}
		double diffX = entity.posX - Minecraft.thePlayer.posX;
		double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
		double diffY;
		if ((entity instanceof EntityPlayer)) {
			EntityPlayer elb = (EntityPlayer) entity;
			diffY = elb.posY + (elb.getEyeHeight() - 0.4D)
					- (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D
					- (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
		}
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

		return new float[] { yaw, pitch };
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
