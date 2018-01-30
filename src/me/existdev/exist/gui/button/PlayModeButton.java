package me.existdev.exist.gui.button;

import java.awt.Color;

import me.existdev.exist.Exist;
import me.existdev.exist.utils.helper.RenderHelper;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PlayModeButton extends GuiButton {

	private int x;
	private int y;
	private int x1;
	private int y1;
	private String text;
	int alphaInc = 100;
	int alpha = 0;
	int size = 0;
	TimeHelper time = new TimeHelper();
	int textint = 0;
	int buttonup = -5;
	int buttondown = 15;
	boolean b1 = false;
	boolean b2 = false;
	boolean b3 = false;

	public PlayModeButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
		this.x = par2;
		this.y = par3;
		this.x1 = par4;
		this.y1 = par5;
		this.text = par6Str;
		textint = (this.y + this.y1 / 2 - 4) / 3;
		buttondown = (this.y + this.y1 - this.size) / 3;
		buttonup = (this.y + this.size) / 3;
		System.out.println(buttondown);
	}

	public PlayModeButton(int i, int j, int k, String stringParams) {
		this(i, j, k, 200, 20, stringParams);

	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (!(textint > this.y + this.y1 / 2 - 4) && !(b1)) {
			textint += 10;
		} else {
			textint = this.y + this.y1 / 2 - 4;
			b1 = true;
		}
		if (!(buttonup > this.y - this.size) && !(b2)) {
			buttonup += 10;
		} else {
			buttonup = this.y - this.size;
			b2 = true;
		}

		if (!(buttondown > this.y + this.y1 + this.size) && !(b3)) {
			buttondown += 10;
		} else {
			buttondown = this.y + this.y1 + this.size;
			b3 = true;
		}
		boolean isOverButton = (mouseX >= this.x) && (mouseX <= this.x + this.x1) && (mouseY >= yPosition - height / 2)
				&& (mouseY <= this.y + this.y1);
		if ((isOverButton) && (this.alphaInc <= 150)) {
			this.alphaInc += 5;
			this.alpha = (this.alphaInc << 24);
		} else if ((!isOverButton) && (this.alphaInc >= 100)) {
			this.alphaInc -= 5;
			this.alpha = (this.alphaInc << 24);
		}
		if (this.alphaInc > 150) {
			this.alphaInc = 150;
		} else if (this.alphaInc < 100) {
			this.alphaInc = 100;
		}
		if ((isOverButton) && (this.size <= 20)) {
			this.size += 5;
		} else if ((!isOverButton) && (this.size >= 0)) {
			this.size -= 3;
		}
		RenderHelper.drawRect(this.x, yPosition - height / 2, this.x + this.x1 + this.size + 12, yPosition + height,
				0xFF363539);
		RenderHelper.drawRect(this.x + this.x1 + this.size + 12, yPosition - height / 2, this.x + this.x1 + this.size + 14, yPosition + height,
				Exist.getClientColor());
		Exist.fontRenderer.fontRenderer40.drawCenteredString(this.text, this.x + this.x1 / 2 + 4 + this.size, yPosition - height / 4,
				0xFFFFFF);
	}
}
