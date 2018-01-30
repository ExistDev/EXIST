package me.existdev.exist.events;

import com.darkmagician6.eventapi.events.Event;

public class EventKeyboard implements Event {
	
	private int k;

	public EventKeyboard(int k) {
		this.k = k;
	}

	public int getKey() {
		return this.k;
	}

}
