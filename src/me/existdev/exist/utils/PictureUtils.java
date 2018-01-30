package me.existdev.exist.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class PictureUtils {

	private ResourceLocation location;

	private int x, y, width, height;

	private Color color;

	public PictureUtils(ResourceLocation location, int x, int y, int width, int height) {
		this.location = location;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = new Color(255, 255, 255, 255);
	}

	public PictureUtils(ResourceLocation location, int x, int y, int width, int height, Color color) {
		this.location = location;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	private Minecraft mc = Minecraft.getMinecraft();

	public void draw() {
		ScaledResolution sr = new ScaledResolution(mc, width, height);
		mc.getTextureManager().bindTexture(location);
		Gui.drawScaledCustomSizeModalRect(this.x, this.y, 0.0f, 0.0f, this.width, this.height, this.width, this.height,
				this.width, this.height);

	}

}
