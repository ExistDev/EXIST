package me.existdev.exist.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventPreMotionUpdates extends EventCancellable implements Event{

	private float yaw, pitch;
	private double x;
	public double y;
	private double z;
	private boolean ground;
	EventType type;

	public EventPreMotionUpdates(float yaw, float pitch, double x, double y, double z, boolean ground, EventType type) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ground = ground;
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public boolean isGround() {
		return ground;
	}

	public void setGround(boolean ground) {
		this.ground = ground;
	}

	public enum EventType {
		PRE, POST;
	}
}
