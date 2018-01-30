package me.existdev.exist.gui.button;

import java.awt.Color;

import me.existdev.exist.Exist;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MainMenuButton extends GuiButton {

	private ResourceLocation icon;
	float targetX;
	float currentX;

	public MainMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.icon = new ResourceLocation("Exist/Buttons/" + this.displayString + ".png");
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			mc.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition - 10 && mouseY >= this.yPosition
					&& mouseX < this.xPosition + this.width + 10 && mouseY < this.yPosition + this.height;
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (!this.enabled) {

			} else if (this.hovered) {
				this.offSpeedX = 6f;
				this.speedA = 20;
				this.targetA += this.speedA;
				if (this.targetA >= 70) {
					this.targetA = 70;
				}
				this.speedA -= 1f;
				if (this.speedA < 5) {
					this.speedA = 5;
				}
				this.targetX += this.onSpeedX;
				if (this.targetX >= 10) {
					this.targetX = 10;
				}
				this.onSpeedX -= 0.8f;
				if (this.onSpeedX < 0) {
					this.onSpeedX = 0f;
				}
			} else if (!this.hovered) {
				this.onSpeedX = 6f;
				this.speedA = 20;
				this.targetA -= this.speedA;
				if (this.targetA <= 0) {
					this.targetA = 0;
				}
				this.speedA -= 1f;
				if (this.speedA < 5) {
					this.speedA = 5;
				}
				this.targetX -= this.offSpeedX;
				if (this.targetX <= 0) {
					this.targetX = 0;
				}
				this.offSpeedX -= 0.8f;
				if (this.offSpeedX < 0) {
					this.offSpeedX = 0f;
				}
			}
			RenderHelper.drawRect(this.xPosition - 10, hovered ? this.yPosition - targetX : this.yPosition,
					this.xPosition + this.width + 10, this.yPosition + this.height,
					hovered ? this.default_getWhite(10 + targetA) : this.getBlack(50 + targetA));
			Exist.fontRenderer.fontRenderer30.drawCenteredString(this.displayString, this.xPosition + this.width / 2,
					this.yPosition + (this.height - 8) / 2 - 2, hovered ? 0xFF000000 : 0xFFFFFFFF);
		}
	}

	public static int getBlack(int A) {
		return new Color(0, 0, 0, A).getRGB();
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.visible && mouseX >= this.xPosition - 10 && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width + 10 && mouseY < this.yPosition + 60;
	}

}
