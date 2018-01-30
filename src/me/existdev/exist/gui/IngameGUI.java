package me.existdev.exist.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.render.HUD;
import me.existdev.exist.utils.ColorUtils;
import me.existdev.exist.utils.PictureUtils;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class IngameGUI extends GuiIngame {

	Minecraft mc = Minecraft.getMinecraft();
	Exist m;

	private int item = 0;
	private float xPos = 0;
	private static float xPos1 = 0;
	public double xOffset = 0;
	public double xOffset1 = 0;
	public int yOffset = 0;

	public IngameGUI(Minecraft mcIn) {
		super(mcIn);
	}

	@Override
	public void func_175180_a(float p_175180_1_) {
		super.func_175180_a(p_175180_1_);
		if (!Exist.getModules(HUD.class).isToggled()) {
			return;
		}
		renderArrayList();
		renderWaterMark();
		renderHotbar();
	}

	public void renderWaterMark() {
		GL11.glPushMatrix();
		PictureUtils p = new PictureUtils(new ResourceLocation("Exist/Logo.png"), 1, 5, 60, 60,
				ColorUtils.getClientColor());

		p.draw();
		GL11.glPopMatrix();
	}

	public void renderHotbar() {
		try {
			ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

			if (mc.currentScreen instanceof GuiChat) {
				if (xOffset < (float) (sr.getScaledWidth() / 2 - 89) + 22f - 27f) {
					xOffset += 5;
				}
				if (xOffset1 < sr.getScaledWidth_double() - (float) (sr.getScaledWidth() / 2 - 89) + 22f - 24.5f
						- 178f) {
					xOffset1 += 5;
				}

			} else {
				if (xOffset > 0) {
					xOffset -= 5;
				}
				if (xOffset1 > 0) {
					xOffset1 -= 5;
				}
				if (yOffset > 0) {
					yOffset--;
				}
			}

			RenderHelper.drawRect(0 + xOffset - 0.8, sr.getScaledHeight() - 23 - yOffset,
					(float) (sr.getScaledWidth() / 2 - 89) + 22f - 24.5f, sr.getScaledHeight() - yOffset, 0x30FFFFFF);
			RenderHelper.drawRect((float) (sr.getScaledWidth() / 2 - 89) + 22f - 24.5f,
					sr.getScaledHeight() - 23 - yOffset, sr.getScaledWidth_double() - xOffset1 + 1,
					sr.getScaledHeight() - yOffset, 0x40FFFFFF);
			item = mc.thePlayer.inventory.currentItem + 1;

			xPos = item * 20 + 1.5f;
			if (xPos1 < xPos) {
				this.xPos1 += 1.5;
			} else if (xPos1 > xPos) {
				this.xPos1 -= 1.5;
			} else {

			}

			if (xPos1 == xPos + 0.5 || xPos1 == xPos - 0.5) {
				xPos1 = xPos;
			}

			float selectionheight = 23;
			RenderHelper.drawRect((float) (sr.getScaledWidth() / 2 - 88) + xPos1 - 25.2f,
					sr.getScaledHeight() - selectionheight, (float) (sr.getScaledWidth() / 2 - 90) + xPos1 - 1,
					sr.getScaledHeight(), 0x90000000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void renderArrayList() {
		int y = 0;
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		for (Module m : Exist.getMods()) {
			if (!m.isToggled()) {
				continue;
			}
			Gui.drawRect(0, 0, 0, 0, 0);
			RenderHelper.drawRect(
					sr.getScaledWidth() - Exist.fontRenderer.helveticaArrayList.getStringWidth(m.getDisplayName()) - 6,
					(y - Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT + 18), sr.getScaledWidth(),
					(y - Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT + 18)
							+ Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT,
					0x20FFFFFF);
			RenderHelper.drawRect(sr.getScaledWidth() - 2, (y - Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT + 18),
					sr.getScaledWidth(), (y - Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT + 18)
							+ Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT,
					0xFFFFFFFF);
			Exist.fontRenderer.helveticaArrayList.drawStringWithShadow(m.getDisplayName(),
					sr.getScaledWidth() - Exist.fontRenderer.helveticaArrayList.getStringWidth(m.getDisplayName()) - 4,
					30 + y + Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT - 31, 0xFFFFFFFF);
			y += Exist.fontRenderer.helveticaArrayList.FONT_HEIGHT;
		}
	}

	public static int getRainbow(int speed, int offset) {
		float hue = (float) ((System.currentTimeMillis() + offset) % speed);
		hue /= speed;
		return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
	}
}
