package me.existdev.exist.gui.altchanger;

import java.util.ArrayList;
import java.util.List;

public class AltManager {
	private List<Alt> alts;
	private Alt lastAlt;

	public Alt getLastAlt() {
		return this.lastAlt;
	}

	public void setLastAlt(Alt alt) {
		this.lastAlt = alt;
	}

	public void setupAlts() {
		this.alts = new ArrayList();
	}

	public List<Alt> getAlts() {
		return this.alts;
	}
}
