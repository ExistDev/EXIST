package me.existdev.exist.gui.particle;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;

import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class Particle extends Gui {

	public int x, y, size, color;

	public int realx, realy;

	private boolean down;
	private boolean right;

	private boolean lines = false;
	
	private boolean circle = false;

	private Random rdm1 = new Random();

	public Particle(int x, int y, int size, int color, boolean down, boolean right, boolean lines) {
		this.x = x;
		this.y = y;
		this.lines = false;
		this.size = 2;
		this.color = color;
		this.realy = y;
		this.realx = x;

		this.circle = true;

		this.down = down;
		this.right = right;

	}
	public Particle(int x, int y, int size, int color, boolean down, boolean right) {
		this.x = x;
		this.y = y;
		this.lines = false;
		this.size = 2;
		this.color = color;
		this.realy = y;
		this.realx = x;

		this.circle = true;

		this.down = down;
		this.right = right;

	}

	public void draw(int mouseX, int mouseY) {

		if (isHovered(mouseX, mouseY)) {
			for (Particle p : getParticlesNearly()) {
			}
		}

		RenderHelper.drawFullCircle(x, y, size, Integer.MAX_VALUE);
		if (down) {

			if (y < GuiScreen.height) {
				y++;
			} else if (y == GuiScreen.height) {
				down = false;
			}
		} else {
			if (y > 0) {
				y--;
			} else if (y == 0) {
				down = true;
			}
		}

		if (right) {
			if (x < GuiScreen.width) {
				x++;
			} else if (x == GuiScreen.width) {
				right = false;
			}
		} else {
			if (x > 0) {
				x--;
			} else if (x == 0) {
				right = true;
			}
		}
	}

	private void save() {
		int rdm = java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 5);

		if (down) {

			if (y < realy + rdm) {
				y++;
			} else if (y == realy + rdm) {
				down = false;
			}
		} else {
			if (y > realy - rdm) {
				y--;
			} else if (y == realy - rdm) {
				down = true;
			}
		}

		if (right) {
			if (x < realx + rdm) {
				x++;
			} else if (x == realx + rdm) {
				right = false;
			}
		} else {
			if (x > realx - rdm) {
				x--;
			} else {
				right = true;
			}
		}

	}

	private Minecraft mc = Minecraft.getMinecraft();

	public ArrayList<Particle> getParticlesNearly() {
		ArrayList<Particle> parts = Lists.newArrayList();

		int reach = 100;

		if (mc.currentScreen != null) {
			for (Particle p : mc.currentScreen.particles) {
				if (this.x - p.x < reach && this.x - p.x > -reach) {
					if (this.y - p.y < reach && this.y - p.y > -reach) {

						parts.add(p);
					}
				}
			}
		}

		return parts;
	}

	public boolean isHovered(int mouseX, int mouseY) {

		int range = 50;
		if(!this.lines) {
			return false;
		}

		if (mc.currentScreen != null) {
			for (int i = -range; i < range; i++) {
				for (int i1 = -range; i1 < range; i1++) {
					if (mouseX + i == this.x && mouseY + i1 == this.y) {
						return true;
					}
				}
			}

		}

		return false;
	}
}
