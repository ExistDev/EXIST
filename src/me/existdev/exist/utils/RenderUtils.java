package me.existdev.exist.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class RenderUtils {

	public static float delta;

	public static void drawFullscreenImage(ResourceLocation image) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		GL11.glDisable((int) 3008);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, scaledResolution.getScaledWidth(),
				scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(),
				scaledResolution.getScaledHeight());
		GL11.glDepthMask((boolean) true);
		GL11.glEnable((int) 2929);
		GL11.glEnable((int) 3008);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	}

	public static double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) ((double) delta * speed);
		if (animation < finalState) {
			if (animation + (double) add >= finalState)
				return finalState;
			return animation += (double) add;
		}
		if (animation - (double) add <= finalState)
			return finalState;
		return animation -= (double) add;
	}

	public static int loadShader(String file, int type) {
		StringBuilder shaderSource;
		shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			do {
				String line;
				if ((line = reader.readLine()) == null) {
					reader.close();
					break;
				}
				shaderSource.append(line).append("//\n");
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader((int) type);
		GL20.glShaderSource((int) shaderID, (CharSequence) shaderSource);
		GL20.glCompileShader((int) shaderID);
		if (GL20.glGetShaderi((int) shaderID, (int) 35713) != 0)
			return shaderID;
		System.out.println(GL20.glGetShaderInfoLog((int) shaderID, (int) 500));
		System.err.println("Could not compile shader!");
		System.exit(-1);
		return shaderID;
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor3f((float) ((float) color.getRed() / 255.0f), (float) ((float) color.getBlue() / 255.0f),
				(float) ((float) color.getRed() / 255.0f));
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void doGlScissor(int x, int y, int width, int height) {
		Minecraft mc = Minecraft.getMinecraft();
		int scaleFactor = 1;
		int k = mc.gameSettings.guiScale;
		if (k == 0) {
			k = 1000;
		}
		while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320
				&& mc.displayHeight / (scaleFactor + 1) >= 240) {
			++scaleFactor;
		}
		GL11.glScissor((int) (x * scaleFactor), (int) (mc.displayHeight - (y + height) * scaleFactor),
				(int) (width * scaleFactor), (int) (height * scaleFactor));
	}

	public static void drawRect(float x1, float y1, float x2, float y2, int color) {
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glEnable((int) 2848);
		GL11.glPushMatrix();
		RenderUtils.color(color);
		GL11.glBegin((int) 7);
		GL11.glVertex2d((double) x2, (double) y1);
		GL11.glVertex2d((double) x1, (double) y1);
		GL11.glVertex2d((double) x1, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glDisable((int) 2848);
		GL11.glPopMatrix();
	}

	public static void color(int color) {
		float f = (float) (color >> 24 & 255) / 255.0f;
		float f1 = (float) (color >> 16 & 255) / 255.0f;
		float f2 = (float) (color >> 8 & 255) / 255.0f;
		float f3 = (float) (color & 255) / 255.0f;
		GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
	}

	public static int createShader(String shaderCode, int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB((int) shaderType);
			if (shader == 0) {
				return 0;
			}
			ARBShaderObjects.glShaderSourceARB((int) shader, (CharSequence) shaderCode);
			ARBShaderObjects.glCompileShaderARB((int) shader);
			if (ARBShaderObjects.glGetObjectParameteriARB((int) shader, (int) 35713) != 0)
				return shader;
			throw new RuntimeException("Error creating shader:");
		} catch (Exception exc) {
			ARBShaderObjects.glDeleteObjectARB((int) shader);
			throw exc;
		}
	}

	public void drawCircle(int x, int y, float radius, int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0f;
		float red = (float) (color >> 16 & 255) / 255.0f;
		float green = (float) (color >> 8 & 255) / 255.0f;
		float blue = (float) (color & 255) / 255.0f;
		boolean blend = GL11.glIsEnabled((int) 3042);
		boolean line = GL11.glIsEnabled((int) 2848);
		boolean texture = GL11.glIsEnabled((int) 3553);
		if (!blend) {
			GL11.glEnable((int) 3042);
		}
		if (!line) {
			GL11.glEnable((int) 2848);
		}
		if (texture) {
			GL11.glDisable((int) 3553);
		}
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		GL11.glBegin((int) 9);
		int i = 0;
		while (i <= 360) {
			GL11.glVertex2d((double) ((double) x + Math.sin((double) i * 3.141526 / 180.0) * (double) radius),
					(double) ((double) y + Math.cos((double) i * 3.141526 / 180.0) * (double) radius));
			++i;
		}
		GL11.glEnd();
		if (texture) {
			GL11.glEnable((int) 3553);
		}
		if (!line) {
			GL11.glDisable((int) 2848);
		}
		if (blend)
			return;
		GL11.glDisable((int) 3042);
	}

	public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
		x = (float) ((double) x + ((double) (round / 2.0f) + 0.5));
		y = (float) ((double) y + ((double) (round / 2.0f) + 0.5));
		x2 = (float) ((double) x2 - ((double) (round / 2.0f) + 0.5));
		y2 = (float) ((double) y2 - ((double) (round / 2.0f) + 0.5));
		RenderHelper.drawRect(x, y, x2, y2, color);
		RenderHelper.drawCircle(x2 - round / 2.0f, y + round / 2.0f, round, color);
		RenderHelper.drawCircle(x + round / 2.0f, y2 - round / 2.0f, round, color);
		RenderHelper.drawCircle(x + round / 2.0f, y + round / 2.0f, round, color);
		RenderHelper.drawCircle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
		RenderHelper.drawRect(x - round / 2.0f - 0.5f, y + round / 2.0f, x2, y2 - round / 2.0f, color);
		RenderHelper.drawRect(x, y + round / 2.0f, x2 + round / 2.0f + 0.5f, y2 - round / 2.0f, color);
		RenderHelper.drawRect(x + round / 2.0f, y - round / 2.0f - 0.5f, x2 - round / 2.0f, y2 - round / 2.0f, color);
		RenderHelper.drawRect(x + round / 2.0f, y, x2 - round / 2.0f, y2 + round / 2.0f + 0.5f, color);
	}
}
