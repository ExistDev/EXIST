package me.existdev.exist.events;

import com.darkmagician6.eventapi.events.Event;

public class EventRender2D implements Event {

	public int width, height;

	public EventRender2D(int width, int height) {
		this.width = width;
		this.height = height;
	}

}
