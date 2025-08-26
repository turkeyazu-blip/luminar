package ru.luminar.utils.draw;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import ru.luminar.Luminar;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.vectors.Vector4i;

public class ColorUtils {
   public final int green = (new Color(64, 255, 64)).getRGB();
   public final int yellow = (new Color(255, 255, 64)).getRGB();
   public final int white = (new Color(255, 255, 255)).getRGB();
   public final int orange = (new Color(255, 128, 32)).getRGB();
   public final int red = (new Color(255, 64, 64)).getRGB();
   public static int blue = (new Color(0, 255, 246)).getRGB();
   public static int black = (new Color(0, 0, 0)).getRGB();
   private static double amount;

   public static int rgb(int r, int g, int b) {
      return -16777216 | r << 16 | g << 8 | b;
   }

   public static int rgba(int r, int g, int b, int a) {
      return a << 24 | r << 16 | g << 8 | b;
   }

   public static int rgba(Color rgb, int a) {
      return a << 24 | rgb.getRed() << 16 | rgb.getGreen() << 8 | rgb.getBlue();
   }

   public static Color injectAlpha(Color color, float alpha) {
      return new Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), alpha * 255.0F);
   }

   public static Color random() {
      return new Color(Color.HSBtoRGB((float)Math.random(), (float)(0.75D + Math.random() / 4.0D), (float)(0.75D + Math.random() / 4.0D)));
   }

   public int multAlpha(int c, float apc) {
      return getColor((float)red(c), (float)green(c), (float)blue(c), (float)alpha(c) * apc);
   }

   public static int replAlpha(int c, int a) {
      return getColor(red(c), green(c), blue(c), a);
   }

   public int multDark(int c, float brpc) {
      return getColor((float)red(c) * brpc, (float)green(c) * brpc, (float)blue(c) * brpc, (float)alpha(c));
   }

   public static void setAlphaColor(int color, float alpha) {
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      RenderSystem.color4f(red, green, blue, alpha);
   }

   public static int getColor(Color color) {
      return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static int getColor(int index) {
      return Luminar.instance.styleManager.getCurrentStyle().getColor(index);
   }

   public static int getColorBright(int bright) {
      return getColor(bright, bright, bright, 255);
   }

   public static int getColor(int red, int green, int blue, int alpha) {
      int color = 0;
      int color = color | alpha << 24;
      color |= red << 16;
      color |= green << 8;
      color |= blue;
      return color;
   }

   public static int getColor(int brightness, int alpha) {
      return getColor(brightness, brightness, brightness, alpha);
   }

   public static int interpolateColor(int color1, int color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      int red1 = getRed(color1);
      int green1 = getGreen(color1);
      int blue1 = getBlue(color1);
      int alpha1 = getAlpha(color1);
      int red2 = getRed(color2);
      int green2 = getGreen(color2);
      int blue2 = getBlue(color2);
      int alpha2 = getAlpha(color2);
      int interpolatedRed = interpolateInt(red1, red2, (double)amount);
      int interpolatedGreen = interpolateInt(green1, green2, (double)amount);
      int interpolatedBlue = interpolateInt(blue1, blue2, (double)amount);
      int interpolatedAlpha = interpolateInt(alpha1, alpha2, (double)amount);
      return interpolatedAlpha << 24 | interpolatedRed << 16 | interpolatedGreen << 8 | interpolatedBlue;
   }

   public static void setColor(int color) {
      setAlphaColor(color, (float)(color >> 24 & 255) / 255.0F);
   }

   public static int toColor(String hexColor) {
      int argb = Integer.parseInt(hexColor.substring(1), 16);
      return setAlpha(argb, 255);
   }

   public static int setAlpha(int color, int alpha) {
      return color & 16777215 | alpha << 24;
   }

   public static int astolfo(int pulseSpeed, int offset, float saturation, float brightness, float alpha) {
      float hueDegrees = (float)calculateHueDegrees(pulseSpeed, offset);
      hueDegrees = (float)((double)hueDegrees % 360.0D);
      float normalizedHue = hueDegrees % 360.0F;
      float hueFraction = normalizedHue / 360.0F;
      boolean useNegativeHue = (double)hueFraction < 0.5D;
      float adjustedHue = useNegativeHue ? -(normalizedHue / 360.0F) : normalizedHue / 360.0F;
      int rgbColor = Color.HSBtoRGB(adjustedHue, saturation, brightness);
      int clampedAlpha = Math.max(0, Math.min(255, (int)(alpha * 255.0F)));
      return reAlphaInt(rgbColor, clampedAlpha);
   }

   private static int calculateHueDegrees(int var0, int var1) {
      long var2 = System.currentTimeMillis();
      long var4 = (var2 / (long)var0 + (long)var1) % 360L;
      return (int)var4;
   }

   public static float[] rgba(int color) {
      return new float[]{(float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F};
   }

   public static int red(int c) {
      return c >> 16 & 255;
   }

   public static int green(int c) {
      return c >> 8 & 255;
   }

   public static int blue(int c) {
      return c & 255;
   }

   public static int alpha(int c) {
      return c >> 24 & 255;
   }

   public static int getColor(float r, float g, float b, float a) {
      return (new Color((int)r, (int)g, (int)b, (int)a)).getRGB();
   }

   public static int overCol(int c1, int c2, float pc01) {
      return getColor((float)red(c1) * (1.0F - pc01) + (float)red(c2) * pc01, (float)green(c1) * (1.0F - pc01) + (float)green(c2) * pc01, (float)blue(c1) * (1.0F - pc01) + (float)blue(c2) * pc01, (float)alpha(c1) * (1.0F - pc01) + (float)alpha(c2) * pc01);
   }

   public static int getColor(float r, float g, float b) {
      return (int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F);
   }

   public static float[] getRGBf(int color) {
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      return new float[]{r, g, b};
   }

   public int overCol(int c1, int c2) {
      return overCol(c1, c2, 0.5F);
   }

   public static float[] getRGBAf(int c) {
      return new float[]{(float)red(c) / 255.0F, (float)green(c) / 255.0F, (float)blue(c) / 255.0F, (float)alpha(c) / 255.0F};
   }

   public static float rgb(int color) {
      return 0.0F;
   }

   public static int getRed(int hex) {
      return hex >> 16 & 255;
   }

   public static int getGreen(int hex) {
      return hex >> 8 & 255;
   }

   public static int getBlue(int hex) {
      return hex & 255;
   }

   public static int getAlpha(int hex) {
      return hex >> 24 & 255;
   }

   public static int reAlphaInt(int color, int alpha) {
      return MathHelper.func_76125_a(alpha, 0, 255) << 24 | color & 16777215;
   }

   public static int interpolateColor(int color1, int color2) {
      amount = Math.min(1.0D, Math.max(0.0D, amount));
      int red1 = getRed(color1);
      int green1 = getGreen(color1);
      int blue1 = getBlue(color1);
      int alpha1 = getAlpha(color1);
      int red2 = getRed(color2);
      int green2 = getGreen(color2);
      int blue2 = getBlue(color2);
      int alpha2 = getAlpha(color2);
      int interpolatedRed = interpolateInt(red1, red2, amount);
      int interpolatedGreen = interpolateInt(green1, green2, amount);
      int interpolatedBlue = interpolateInt(blue1, blue2, amount);
      int interpolatedAlpha = interpolateInt(alpha1, alpha2, amount);
      return interpolatedAlpha << 24 | interpolatedRed << 16 | interpolatedGreen << 8 | interpolatedBlue;
   }

   public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
      return interpolateD((double)oldValue, (double)newValue, (double)((float)interpolationValue)).intValue();
   }

   public static Double interpolateD(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }

   public static int gradient(int start, int end, int index, int speed) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle > 180 ? 360 - angle : angle) + 180;
      int color = interpolate(start, end, MathHelper.func_76131_a((float)angle / 180.0F - 1.0F, 0.0F, 1.0F));
      float[] hs = rgba(color);
      float[] hsb = Color.RGBtoHSB((int)(hs[0] * 255.0F), (int)(hs[1] * 255.0F), (int)(hs[2] * 255.0F), (float[])null);
      hsb[1] *= 1.5F;
      hsb[1] = Math.min(hsb[1], 1.0F);
      return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
   }

   public static int rainbow(int speed, int index, float saturation, float brightness, float opacity) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      float hue = (float)angle / 360.0F;
      int color = Color.HSBtoRGB(hue, saturation, brightness);
      return getColor(red(color), green(color), blue(color), Math.round(opacity * 255.0F));
   }

   public static int darkenColor(int color, float factor) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      r = Math.max((int)((float)r * (1.0F - factor)), 0);
      g = Math.max((int)((float)g * (1.0F - factor)), 0);
      b = Math.max((int)((float)b * (1.0F - factor)), 0);
      return color & -16777216 | r << 16 | g << 8 | b;
   }

   public static int interpolate(int start, int end, float value) {
      float[] startColor = rgba(start);
      float[] endColor = rgba(end);
      return rgba((int)MathUtil.interpolate((double)(startColor[0] * 255.0F), (double)(endColor[0] * 255.0F), (double)value), (int)MathUtil.interpolate((double)(startColor[1] * 255.0F), (double)(endColor[1] * 255.0F), (double)value), (int)MathUtil.interpolate((double)(startColor[2] * 255.0F), (double)(endColor[2] * 255.0F), (double)value), (int)MathUtil.interpolate((double)(startColor[3] * 255.0F), (double)(endColor[3] * 255.0F), (double)value));
   }

   public static Vector4i interpolate(Vector4i start, Vector4i end, float value) {
      return new Vector4i(interpolate(start.getX(), end.getX(), value), interpolate(start.getY(), end.getY(), value), interpolate(start.getZ(), end.getZ(), value), interpolate(start.getW(), end.getW(), value));
   }

   public static class IntColor {
      public static float[] rgb(int color) {
         return new float[]{(float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F};
      }

      public static int rgba(int r, int g, int b, int a) {
         return a << 24 | r << 16 | g << 8 | b;
      }

      public static int rgb(int r, int g, int b) {
         return -16777216 | r << 16 | g << 8 | b;
      }

      public static int getRed(int hex) {
         return hex >> 16 & 255;
      }

      public static int getGreen(int hex) {
         return hex >> 8 & 255;
      }

      public static int getBlue(int hex) {
         return hex & 255;
      }

      public static int getAlpha(int hex) {
         return hex >> 24 & 255;
      }
   }
}
