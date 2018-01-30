package me.existdev.exist.utils;

import java.awt.Color;

public class ColorUtils {

	public final static Color getClientColor() {
		return new Color(66, 134, 244);
	}

	public static int getRainbow(int speed, int offset) {
		float hue = (float) ((System.currentTimeMillis() + offset) % speed);
		hue /= speed;
		return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
	}

	public static int reAlpha(int color, float alpha) {
		Color c = new Color(color);
		float r = 0.003921569F * (float) c.getRed();
		float g = 0.003921569F * (float) c.getGreen();
		float b = 0.003921569F * (float) c.getBlue();
		return (new Color(r, g, b, alpha)).getRGB();
	}
}
