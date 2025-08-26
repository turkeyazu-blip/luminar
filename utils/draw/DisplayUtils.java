package ru.luminar.utils.draw;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.opengl.GL11;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draw.font.TextureHelper;
import ru.luminar.utils.math.vectors.Vector4i;
import ru.luminar.utils.shader.ShaderUtil;

public class DisplayUtils implements IMinecraft {
   private static HashMap<Integer, Integer> shadowCache2 = new HashMap();
   public static final List<DisplayUtils.Vec2fColored> VERTEXES_COLORED = new ArrayList();

   public static void quads(float x, float y, float width, float height, int glQuads, int color) {
      float[] colors = ColorUtils.rgba(color);
      float r = colors[0];
      float g = colors[1];
      float b = colors[2];
      float a = colors[3];
      buffer.func_181668_a(glQuads, DefaultVertexFormats.field_181709_i);
      buffer.func_225582_a_((double)x, (double)y, 0.0D).func_225583_a_(0.0F, 0.0F).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_((double)x, (double)(y + height), 0.0D).func_225583_a_(0.0F, 1.0F).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)(y + height), 0.0D).func_225583_a_(1.0F, 1.0F).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)y, 0.0D).func_225583_a_(1.0F, 0.0F).func_227885_a_(r, g, b, a).func_181675_d();
      tessellator.func_78381_a();
   }

   public static int reAlphaInt(int i, int i2) {
      return MathHelper.func_76125_a(i2, 0, 255) << 24 | i & 16777215;
   }

   public static void scissor(double x, double y, double width, double height) {
      double scale = mc.func_228018_at_().func_198100_s();
      y = (double)mc.func_228018_at_().func_198083_n() - y;
      x *= scale;
      y *= scale;
      width *= scale;
      height *= scale;
      GL11.glScissor((int)x, (int)(y - height), (int)width, (int)height);
   }

   public static boolean isInRegion(int mouseX, int mouseY, int x, int y, int width, int height) {
      return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
   }

   public static boolean isInRegion(double mouseX, double mouseY, float x, float y, float width, float height) {
      return mouseX >= (double)x && mouseX <= (double)(x + width) && mouseY >= (double)y && mouseY <= (double)(y + height);
   }

   public static boolean isInRegion(double mouseX, double mouseY, int x, int y, int width, int height) {
      return mouseX >= (double)x && mouseX <= (double)(x + width) && mouseY >= (double)y && mouseY <= (double)(y + height);
   }

   public static void drawGradientOutline(float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 771);
      ShaderUtil.outline.attach();
      ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, ShaderUtil.outline);
      ShaderUtil.outline.setUniform("color1", ColorUtils.IntColor.rgb(bottomLeft));
      ShaderUtil.outline.setUniform("color2", ColorUtils.IntColor.rgb(topLeft));
      ShaderUtil.outline.setUniform("color3", ColorUtils.IntColor.rgb(bottomRight));
      ShaderUtil.outline.setUniform("color4", ColorUtils.IntColor.rgb(topRight));
      ShaderUtil.outline.setUniform("borderWidth", 2.0F);
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      ShaderUtil.outline.detach();
      RenderSystem.disableBlend();
   }

   public static void drawGradientRound(float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 771);
      ShaderUtil.gradientRound.attach();
      ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, ShaderUtil.gradientRound);
      ShaderUtil.gradientRound.setUniform("color1", ColorUtils.IntColor.rgb(bottomLeft));
      ShaderUtil.gradientRound.setUniform("color2", ColorUtils.IntColor.rgb(topLeft));
      ShaderUtil.gradientRound.setUniform("color3", ColorUtils.IntColor.rgb(bottomRight));
      ShaderUtil.gradientRound.setUniform("color4", ColorUtils.IntColor.rgb(topRight));
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      ShaderUtil.gradientRound.detach();
      RenderSystem.disableBlend();
   }

   public static void drawGradientRound(float x, float y, float width, float height, Vector4f vector4f, int bottomLeft, int topLeft, int bottomRight, int topRight) {
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 771);
      GlStateManager.func_227626_N_();
      GlStateManager.func_227740_m_();
      ShaderUtil.rounded.attach();
      ShaderUtil.rounded.setUniform("size", width * 2.0F, height * 2.0F);
      ShaderUtil.rounded.setUniform("round", vector4f.func_195910_a() * 2.0F, vector4f.func_195913_b() * 2.0F, vector4f.func_195914_c() * 2.0F, vector4f.func_195915_d() * 2.0F);
      ShaderUtil.rounded.setUniform("smoothness", 0.0F, 1.5F);
      ShaderUtil.gradientRound.setUniform("color1", ColorUtils.IntColor.rgb(bottomLeft));
      ShaderUtil.gradientRound.setUniform("color2", ColorUtils.IntColor.rgb(topLeft));
      ShaderUtil.gradientRound.setUniform("color3", ColorUtils.IntColor.rgb(bottomRight));
      ShaderUtil.gradientRound.setUniform("color4", ColorUtils.IntColor.rgb(topRight));
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      ShaderUtil.gradientRound.detach();
      RenderSystem.disableBlend();
      GlStateManager.func_227627_O_();
      RenderSystem.disableBlend();
   }

   public static void drawShadow(float x, float y, float width, float height, int radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
      GlStateManager.func_227644_a_(SourceFactor.SRC_ALPHA.field_225655_p_, DestFactor.ONE_MINUS_SRC_ALPHA.field_225654_o_, SourceFactor.ONE.field_225655_p_, DestFactor.ZERO.field_225654_o_);
      GlStateManager.func_227762_u_(7425);
      GlStateManager.func_227740_m_();
      GlStateManager.func_227676_b_(770, 771);
      GlStateManager.func_227639_a_(516, 0.01F);
   }

   public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, int color) {
      RenderSystem.pushMatrix();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.shadeModel(7425);
      mc.func_110434_K().func_110577_a(resourceLocation);
      quads(x, y, width, height, 7, color);
      RenderSystem.shadeModel(7424);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.popMatrix();
   }

   public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Vector4i color) {
      RenderSystem.pushMatrix();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.shadeModel(7425);
      mc.func_110434_K().func_110577_a(resourceLocation);
      int packedColor = color.x;
      float a = (float)(packedColor >> 24 & 255) / 255.0F;
      float r = (float)(packedColor >> 16 & 255) / 255.0F;
      float g = (float)(packedColor >> 8 & 255) / 255.0F;
      float b = (float)(packedColor & 255) / 255.0F;
      buffer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
      buffer.func_225582_a_((double)x, (double)y, 0.0D).func_225583_a_(0.0F, 0.0F).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_((double)x, (double)(y + height), 0.0D).func_225583_a_(0.0F, 1.0F).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)(y + height), 0.0D).func_225583_a_(1.0F, 1.0F).func_227885_a_(r, g, b, a).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)y, 0.0D).func_225583_a_(1.0F, 0.0F).func_227885_a_(r, g, b, a).func_181675_d();
      tessellator.func_78381_a();
      RenderSystem.shadeModel(7424);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.popMatrix();
   }

   public static void drawRectWBuilding(double left, double top, double right, double bottom, int color) {
      right += left;
      bottom += top;
      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      BufferBuilder bufferbuilder = Tessellator.func_178181_a().func_178180_c();
      bufferbuilder.func_225582_a_(left, bottom, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(right, bottom, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(right, top, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(left, top, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
   }

   public static void drawRectBuilding(double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      BufferBuilder bufferbuilder = Tessellator.func_178181_a().func_178180_c();
      bufferbuilder.func_225582_a_(left, bottom, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(right, bottom, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(right, top, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(left, top, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
   }

   public static void drawMCVerticalBuilding(double x, double y, double width, double height, int start, int end) {
      float f = (float)(start >> 24 & 255) / 255.0F;
      float f1 = (float)(start >> 16 & 255) / 255.0F;
      float f2 = (float)(start >> 8 & 255) / 255.0F;
      float f3 = (float)(start & 255) / 255.0F;
      float f4 = (float)(end >> 24 & 255) / 255.0F;
      float f5 = (float)(end >> 16 & 255) / 255.0F;
      float f6 = (float)(end >> 8 & 255) / 255.0F;
      float f7 = (float)(end & 255) / 255.0F;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_225582_a_(x, height, 0.0D).func_227885_a_(f1, f2, f3, f).func_181675_d();
      bufferbuilder.func_225582_a_(width, height, 0.0D).func_227885_a_(f1, f2, f3, f).func_181675_d();
      bufferbuilder.func_225582_a_(width, y, 0.0D).func_227885_a_(f5, f6, f7, f4).func_181675_d();
      bufferbuilder.func_225582_a_(x, y, 0.0D).func_227885_a_(f5, f6, f7, f4).func_181675_d();
   }

   public static void drawMCHorizontalBuilding(double x, double y, double width, double height, int start, int end) {
      float f = (float)(start >> 24 & 255) / 255.0F;
      float f1 = (float)(start >> 16 & 255) / 255.0F;
      float f2 = (float)(start >> 8 & 255) / 255.0F;
      float f3 = (float)(start & 255) / 255.0F;
      float f4 = (float)(end >> 24 & 255) / 255.0F;
      float f5 = (float)(end >> 16 & 255) / 255.0F;
      float f6 = (float)(end >> 8 & 255) / 255.0F;
      float f7 = (float)(end & 255) / 255.0F;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_225582_a_(x, height, 0.0D).func_227885_a_(f1, f2, f3, f).func_181675_d();
      bufferbuilder.func_225582_a_(width, height, 0.0D).func_227885_a_(f5, f6, f7, f4).func_181675_d();
      bufferbuilder.func_225582_a_(width, y, 0.0D).func_227885_a_(f5, f6, f7, f4).func_181675_d();
      bufferbuilder.func_225582_a_(x, y, 0.0D).func_227885_a_(f1, f2, f3, f).func_181675_d();
   }

   public static void drawRectW(double x, double y, double w, double h, int color) {
      w += x;
      h += y;
      double j;
      if (x < w) {
         j = x;
         x = w;
         w = j;
      }

      if (y < h) {
         j = y;
         y = h;
         h = j;
      }

      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      BufferBuilder bufferbuilder = Tessellator.func_178181_a().func_178180_c();
      RenderSystem.enableBlend();
      RenderSystem.disableTexture();
      RenderSystem.defaultBlendFunc();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      bufferbuilder.func_225582_a_(x, h, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(w, h, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(w, y, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_225582_a_(x, y, 0.0D).func_227885_a_(f, f1, f2, f3).func_181675_d();
      bufferbuilder.func_178977_d();
      WorldVertexBufferUploader.func_181679_a(bufferbuilder);
      RenderSystem.enableTexture();
      RenderSystem.disableBlend();
   }

   public static void drawRectHorizontalW(double x, double y, double w, double h, int color, int color1) {
      w += x;
      h += y;
      double j;
      if (x < w) {
         j = x;
         x = w;
         w = j;
      }

      if (y < h) {
         j = y;
         y = h;
         h = j;
      }

      float[] colorOne = ColorUtils.rgba(color);
      float[] colorTwo = ColorUtils.rgba(color1);
      BufferBuilder bufferbuilder = Tessellator.func_178181_a().func_178180_c();
      RenderSystem.enableBlend();
      RenderSystem.disableTexture();
      RenderSystem.shadeModel(7425);
      RenderSystem.defaultBlendFunc();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      bufferbuilder.func_225582_a_(x, h, 0.0D).func_227885_a_(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).func_181675_d();
      bufferbuilder.func_225582_a_(w, h, 0.0D).func_227885_a_(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).func_181675_d();
      bufferbuilder.func_225582_a_(w, y, 0.0D).func_227885_a_(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).func_181675_d();
      bufferbuilder.func_225582_a_(x, y, 0.0D).func_227885_a_(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).func_181675_d();
      bufferbuilder.func_178977_d();
      WorldVertexBufferUploader.func_181679_a(bufferbuilder);
      RenderSystem.shadeModel(7424);
      RenderSystem.enableTexture();
      RenderSystem.disableBlend();
   }

   public static void drawRectVerticalW(double x, double y, double w, double h, int color, int color1) {
      w += x;
      h += y;
      double j;
      if (x < w) {
         j = x;
         x = w;
         w = j;
      }

      if (y < h) {
         j = y;
         y = h;
         h = j;
      }

      float[] colorOne = ColorUtils.rgba(color);
      float[] colorTwo = ColorUtils.rgba(color1);
      BufferBuilder bufferbuilder = Tessellator.func_178181_a().func_178180_c();
      RenderSystem.enableBlend();
      RenderSystem.shadeModel(7425);
      RenderSystem.disableTexture();
      RenderSystem.defaultBlendFunc();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      bufferbuilder.func_225582_a_(x, h, 0.0D).func_227885_a_(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).func_181675_d();
      bufferbuilder.func_225582_a_(w, h, 0.0D).func_227885_a_(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).func_181675_d();
      bufferbuilder.func_225582_a_(w, y, 0.0D).func_227885_a_(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).func_181675_d();
      bufferbuilder.func_225582_a_(x, y, 0.0D).func_227885_a_(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).func_181675_d();
      bufferbuilder.func_178977_d();
      WorldVertexBufferUploader.func_181679_a(bufferbuilder);
      RenderSystem.enableTexture();
      RenderSystem.shadeModel(7424);
      RenderSystem.disableBlend();
   }

   public static void drawRoundedRect(float x, float y, float width, float height, Vector4f vector4f, int color) {
      GlStateManager.func_227626_N_();
      GlStateManager.func_227740_m_();
      ShaderUtil.rounded.attach();
      ShaderUtil.rounded.setUniform("size", width * 2.0F, height * 2.0F);
      ShaderUtil.rounded.setUniform("round", vector4f.func_195910_a() * 2.0F, vector4f.func_195913_b() * 2.0F, vector4f.func_195914_c() * 2.0F, vector4f.func_195915_d() * 2.0F);
      ShaderUtil.rounded.setUniform("smoothness", 0.0F, 1.5F);
      ShaderUtil.rounded.setUniform("color1", ColorUtils.rgba(color));
      ShaderUtil.rounded.setUniform("color2", ColorUtils.rgba(color));
      ShaderUtil.rounded.setUniform("color3", ColorUtils.rgba(color));
      ShaderUtil.rounded.setUniform("color4", ColorUtils.rgba(color));
      drawQuads(x, y, width, height, 7);
      ShaderUtil.rounded.detach();
      RenderSystem.disableBlend();
      GlStateManager.func_227627_O_();
   }

   public static void drawCircle2(float x, float y, float start, float end, float radius, float width, boolean filled, int color) {
      if (start > end) {
         float endOffset = end;
         end = start;
         start = endOffset;
      }

      GlStateManager.func_227740_m_();
      GL11.glDisable(3553);
      RenderSystem.blendFuncSeparate(770, 771, 1, 0);
      GL11.glEnable(2848);
      GL11.glLineWidth(width);
      GL11.glBegin(3);

      float i;
      float cos;
      float sin;
      for(i = end; i >= start; --i) {
         ColorUtils.setColor(color);
         cos = MathHelper.func_76134_b((float)((double)i * 3.141592653589793D / 180.0D)) * radius;
         sin = MathHelper.func_76126_a((float)((double)i * 3.141592653589793D / 180.0D)) * radius;
         GL11.glVertex2f(x + cos, y + sin);
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      if (filled) {
         GL11.glBegin(6);

         for(i = end; i >= start; --i) {
            ColorUtils.setColor(color);
            cos = MathHelper.func_76134_b((float)((double)i * 3.141592653589793D / 180.0D)) * radius;
            sin = MathHelper.func_76126_a((float)((double)i * 3.141592653589793D / 180.0D)) * radius;
            GL11.glVertex2f(x + cos, y + sin);
         }

         GL11.glEnd();
      }

      GL11.glEnable(3553);
      GlStateManager.func_227737_l_();
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float round, Vector4i color) {
      GlStateManager.func_227626_N_();
      GlStateManager.func_227740_m_();
      ShaderUtil.rounded.attach();
      ShaderUtil.rounded.setUniform("size", width * 2.0F, height * 2.0F);
      ShaderUtil.rounded.setUniform("round", round * 2.0F, round * 2.0F, round * 2.0F, round * 2.0F);
      ShaderUtil.rounded.setUniform("smoothness", 0.0F, 1.5F);
      ShaderUtil.rounded.setUniform("color1", ColorUtils.rgba(color.getX()));
      ShaderUtil.rounded.setUniform("color2", ColorUtils.rgba(color.getY()));
      ShaderUtil.rounded.setUniform("color3", ColorUtils.rgba(color.getZ()));
      ShaderUtil.rounded.setUniform("color4", ColorUtils.rgba(color.getW()));
      drawQuads(x, y, width, height, 7);
      ShaderUtil.rounded.detach();
      GlStateManager.func_227737_l_();
      GlStateManager.func_227627_O_();
   }

   public static void drawRoundedRect(float x, float y, float width, float height, Vector4f vector4f, Vector4i color) {
      GlStateManager.func_227626_N_();
      GlStateManager.func_227740_m_();
      ShaderUtil.rounded.attach();
      ShaderUtil.rounded.setUniform("size", width * 2.0F, height * 2.0F);
      ShaderUtil.rounded.setUniform("round", vector4f.func_195910_a() * 2.0F, vector4f.func_195913_b() * 2.0F, vector4f.func_195914_c() * 2.0F, vector4f.func_195915_d() * 2.0F);
      ShaderUtil.rounded.setUniform("smoothness", 0.0F, 1.5F);
      ShaderUtil.rounded.setUniform("color1", ColorUtils.rgba(color.getX()));
      ShaderUtil.rounded.setUniform("color2", ColorUtils.rgba(color.getY()));
      ShaderUtil.rounded.setUniform("color3", ColorUtils.rgba(color.getZ()));
      ShaderUtil.rounded.setUniform("color4", ColorUtils.rgba(color.getW()));
      drawQuads(x, y, width, height, 7);
      ShaderUtil.rounded.detach();
      RenderSystem.disableBlend();
      GlStateManager.func_227627_O_();
   }

   public static void drawRoundedRectWithOutline(float x, float y, float width, float height, float radius, int fillColor, int outlineColor, float outlineThickness) {
      drawRoundedRect(x - outlineThickness / 2.0F, y - outlineThickness / 2.0F, width + outlineThickness, height + outlineThickness, radius + outlineThickness / 2.0F, outlineColor);
      drawRoundedRect(x, y, width, height, radius, fillColor);
   }

   public static void drawRoundedRectWithGradientOutline(float x, float y, float width, float height, float radius, int fillColor, int outlineColorX, int outlineColorY, int outlineColorZ, int outlineColorW, float outlineThickness) {
      drawGradientRound(x - outlineThickness / 2.0F, y - outlineThickness / 2.0F, width + outlineThickness, height + outlineThickness, radius + outlineThickness / 2.0F, outlineColorX, outlineColorY, outlineColorZ, outlineColorW);
      drawRoundedRect(x, y, width, height, radius, fillColor);
   }

   public static void drawGlow(float x, float y, float width, float height, float glowRadius, int glowColor) {
      GlStateManager.func_227740_m_();
      GlStateManager.func_227676_b_(770, 771);
      float glowAlpha = (float)(glowColor >> 24 & 255) / 255.0F;
      float glowRed = (float)(glowColor >> 16 & 255) / 255.0F;
      float glowGreen = (float)(glowColor >> 8 & 255) / 255.0F;
      float glowBlue = (float)(glowColor & 255) / 255.0F;
      GlStateManager.func_227621_I_();
      GL11.glColor4f(glowRed, glowGreen, glowBlue, glowAlpha);

      for(float i = 0.0F; i < glowRadius; ++i) {
         float alphaFactor = glowAlpha * (1.0F - i / glowRadius);
         GL11.glColor4f(glowRed, glowGreen, glowBlue, alphaFactor);
         drawRoundedRect(x - i, y - i, width + 2.0F * i, height + 2.0F * i, i, glowColor);
      }

      GlStateManager.func_227619_H_();
      RenderSystem.disableBlend();
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float outline, int color1, Vector4f vector4f, Vector4i color) {
      GlStateManager.func_227626_N_();
      GlStateManager.func_227740_m_();
      ShaderUtil.roundedout.attach();
      ShaderUtil.roundedout.setUniform("size", width * 2.0F, height * 2.0F);
      ShaderUtil.roundedout.setUniform("round", vector4f.func_195910_a() * 2.0F, vector4f.func_195913_b() * 2.0F, vector4f.func_195914_c() * 2.0F, vector4f.func_195915_d() * 2.0F);
      ShaderUtil.roundedout.setUniform("smoothness", 0.0F, 1.5F);
      ShaderUtil.roundedout.setUniform("outlineColor", ColorUtils.rgba(color.getX()));
      ShaderUtil.roundedout.setUniform("outlineColor1", ColorUtils.rgba(color.getY()));
      ShaderUtil.roundedout.setUniform("outlineColor2", ColorUtils.rgba(color.getZ()));
      ShaderUtil.roundedout.setUniform("outlineColor3", ColorUtils.rgba(color.getW()));
      ShaderUtil.roundedout.setUniform("color", ColorUtils.rgba(color1));
      ShaderUtil.roundedout.setUniform("outline", outline);
      drawQuads(x, y, width, height, 7);
      ShaderUtil.rounded.detach();
      RenderSystem.disableBlend();
      GlStateManager.func_227627_O_();
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
      GlStateManager.func_227626_N_();
      GlStateManager.func_227740_m_();
      ShaderUtil.smooth.attach();
      ShaderUtil.smooth.setUniformf("location", (float)((double)x * mc.func_228018_at_().func_198100_s()), (float)((double)mc.func_228018_at_().func_198091_l() - (double)height * mc.func_228018_at_().func_198100_s() - (double)y * mc.func_228018_at_().func_198100_s()));
      ShaderUtil.smooth.setUniformf("rectSize", (double)width * mc.func_228018_at_().func_198100_s(), (double)height * mc.func_228018_at_().func_198100_s());
      ShaderUtil.smooth.setUniformf("radius", (double)radius * mc.func_228018_at_().func_198100_s());
      ShaderUtil.smooth.setUniform("blur", 0);
      ShaderUtil.smooth.setUniform("color", ColorUtils.rgba(color));
      drawQuads(x, y, width, height, 7);
      ShaderUtil.smooth.detach();
      RenderSystem.disableBlend();
      GlStateManager.func_227627_O_();
   }

   public static void drawCircle(float x, float y, float radius, int color) {
      drawRoundedRect(x - radius / 2.0F, y - radius / 2.0F, radius, radius, radius / 2.0F, color);
   }

   public static void drawCircleS(float x, float y, float radius, int color) {
      drawRoundedRect(x - radius / 2.0F, y - radius / 2.0F, radius, radius, radius / 2.0F - 0.5F, color);
   }

   private static void drawCircleSegment(float centerX, float centerY, float radius, float startAngle, float endAngle) {
      for(float angle = startAngle; angle <= endAngle; ++angle) {
         float rad = (float)Math.toRadians((double)angle);
         GL11.glVertex2f(centerX + (float)Math.cos((double)rad) * radius, centerY + (float)Math.sin((double)rad) * radius);
      }

   }

   public static void drawQuads(float x, float y, float width, float height, int glQuads) {
      buffer.func_181668_a(glQuads, DefaultVertexFormats.field_181707_g);
      buffer.func_225582_a_((double)x, (double)(y + height), 0.0D).func_225583_a_(0.0F, 1.0F).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)(y + height), 0.0D).func_225583_a_(1.0F, 1.0F).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)y, 0.0D).func_225583_a_(1.0F, 0.0F).func_181675_d();
      buffer.func_225582_a_((double)x, (double)y, 0.0D).func_225583_a_(0.0F, 0.0F).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
   }

   public static void drawBox(double x, double y, double width, double height, double size, int color) {
      drawRectBuilding(x + size, y, width - size, y + size, color);
      drawRectBuilding(x, y, x + size, height, color);
      drawRectBuilding(width - size, y, width, height, color);
      drawRectBuilding(x + size, height - size, width - size, height, color);
   }

   public static void drawBoxTest(double x, double y, double width, double height, double size, Vector4i colors) {
      drawMCHorizontalBuilding(x + size, y, width - size, y + size, colors.x, colors.z);
      drawMCVerticalBuilding(x, y, x + size, height, colors.z, colors.x);
      drawMCVerticalBuilding(width - size, y, width, height, colors.x, colors.z);
      drawMCHorizontalBuilding(x + size, height - size, width - size, height, colors.z, colors.x);
   }

   public static void drawImage(MatrixStack stack, ResourceLocation image, double x, double y, double z, double width, double height, int color1, int color2, int color3, int color4) {
      mc.func_110434_K().func_110577_a(image);
      drawImage(stack, x, y, z, width, height, color1, color2, color3, color4);
   }

   public static void drawImage(MatrixStack stack, double x, double y, double z, double width, double height, int color1, int color2, int color3, int color4) {
      boolean blend = GL11.glIsEnabled(3042);
      GlStateManager.func_227740_m_();
      GlStateManager.func_227676_b_(770, 1);
      GL11.glShadeModel(7425);
      GL11.glAlphaFunc(516, 0.0F);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      buffer.func_181668_a(7, DefaultVertexFormats.field_227852_q_);
      buffer.func_227888_a_(stack.func_227866_c_().func_227870_a_(), (float)x, (float)(y + height), (float)z).func_225586_a_(color1 >> 16 & 255, color1 >> 8 & 255, color1 & 255, color1 >>> 24).func_225583_a_(0.0F, 0.99F).func_225587_b_(0, 240).func_181675_d();
      buffer.func_227888_a_(stack.func_227866_c_().func_227870_a_(), (float)(x + width), (float)(y + height), (float)z).func_225586_a_(color2 >> 16 & 255, color2 >> 8 & 255, color2 & 255, color2 >>> 24).func_225583_a_(1.0F, 0.99F).func_225587_b_(0, 240).func_181675_d();
      buffer.func_227888_a_(stack.func_227866_c_().func_227870_a_(), (float)(x + width), (float)y, (float)z).func_225586_a_(color3 >> 16 & 255, color3 >> 8 & 255, color3 & 255, color3 >>> 24).func_225583_a_(1.0F, 0.0F).func_225587_b_(0, 240).func_181675_d();
      buffer.func_227888_a_(stack.func_227866_c_().func_227870_a_(), (float)x, (float)y, (float)z).func_225586_a_(color4 >> 16 & 255, color4 >> 8 & 255, color4 & 255, color4 >>> 24).func_225583_a_(0.0F, 0.0F).func_225587_b_(0, 240).func_181675_d();
      tessellator.func_78381_a();
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glShadeModel(7424);
      GlStateManager.func_227676_b_(770, 0);
      if (!blend) {
         RenderSystem.disableBlend();
      }

   }

   public static void drawTexturedRect(float x, float y, float w, float h, float t1, float t2, float t3, float call, ResourceLocation image) {
      Minecraft.func_71410_x().func_110434_K().func_110577_a(image);
      RenderSystem.enableTexture();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      BufferBuilder buffer = Tessellator.func_178181_a().func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
      buffer.func_225582_a_((double)x, (double)(y + h), 0.0D).func_225583_a_(t1, t2 + call).func_225586_a_(255, 255, 255, 255).func_181675_d();
      buffer.func_225582_a_((double)(x + w), (double)(y + h), 0.0D).func_225583_a_(t1 + t3, t2 + call).func_225586_a_(255, 255, 255, 255).func_181675_d();
      buffer.func_225582_a_((double)(x + w), (double)y, 0.0D).func_225583_a_(t1 + t3, t2).func_225586_a_(255, 255, 255, 255).func_181675_d();
      buffer.func_225582_a_((double)x, (double)y, 0.0D).func_225583_a_(t1, t2).func_225586_a_(255, 255, 255, 255).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
      RenderSystem.disableBlend();
      RenderSystem.disableTexture();
   }

   public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height) {
      mc.func_110434_K().func_110577_a(resourceLocation);
      RenderSystem.pushMatrix();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.shadeModel(7425);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      buffer.func_225582_a_((double)x, (double)(y + height), 0.0D).func_225583_a_(0.0F, 1.0F).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)(y + height), 0.0D).func_225583_a_(1.0F, 1.0F).func_181675_d();
      buffer.func_225582_a_((double)(x + width), (double)y, 0.0D).func_225583_a_(1.0F, 0.0F).func_181675_d();
      buffer.func_225582_a_((double)x, (double)y, 0.0D).func_225583_a_(0.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      RenderSystem.shadeModel(7424);
      RenderSystem.disableBlend();
      RenderSystem.popMatrix();
   }

   public static void drawCircle(MatrixStack ms, float centerX, float centerY, float radius, Color color) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      ColorUtils.setColor(color.getRGB());
      GL11.glBegin(6);
      GL11.glVertex2f(centerX, centerY);
      int segments = 50;
      double angleIncrement = 6.283185307179586D / (double)segments;

      for(int i = 0; i <= segments; ++i) {
         double angle = (double)i * angleIncrement;
         float xOffset = (float)(Math.cos(angle) * (double)radius);
         float yOffset = (float)(Math.sin(angle) * (double)radius);
         GL11.glVertex2f(centerX + xOffset, centerY + yOffset);
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   public static int downloadImage(String url) {
      int texId = true;
      int identifier = Objects.hash(new Object[]{url});
      int texId;
      if (shadowCache2.containsKey(identifier)) {
         texId = (Integer)shadowCache2.get(identifier);
      } else {
         URL stringURL = null;

         try {
            stringURL = new URL(url);
         } catch (MalformedURLException var8) {
            throw new RuntimeException(var8);
         }

         BufferedImage img = null;

         try {
            img = ImageIO.read(stringURL);
         } catch (IOException var7) {
            throw new RuntimeException(var7);
         }

         try {
            texId = TextureHelper.loadTexture(img);
         } catch (Exception var6) {
            throw new RuntimeException(var6);
         }

         shadowCache2.put(identifier, texId);
      }

      return texId;
   }

   private static Shape createCustomRoundedShape(int width, int height, int topLeftRadius, int topRightRadius, int bottomRightRadius, int bottomLeftRadius) {
      Path2D path = new Double();
      path.moveTo((double)(width - bottomRightRadius), (double)height);
      path.quadTo((double)width, (double)height, (double)width, (double)(height - bottomRightRadius));
      path.lineTo((double)width, (double)topRightRadius);
      path.quadTo((double)width, 0.0D, (double)(width - topRightRadius), 0.0D);
      path.lineTo((double)topLeftRadius, 0.0D);
      path.quadTo(0.0D, 0.0D, 0.0D, (double)topLeftRadius);
      path.lineTo(0.0D, (double)(height - bottomLeftRadius));
      path.quadTo(0.0D, (double)height, (double)bottomLeftRadius, (double)height);
      path.closePath();
      return path;
   }

   public static void drawRect(MatrixStack matrix, float x, float y, float x2, float y2, int color1, int color2, int color3, int color4, boolean bloom, boolean texture) {
      VERTEXES_COLORED.clear();
      VERTEXES_COLORED.add(new DisplayUtils.Vec2fColored(x, y, color1));
      VERTEXES_COLORED.add(new DisplayUtils.Vec2fColored(x2, y, color2));
      VERTEXES_COLORED.add(new DisplayUtils.Vec2fColored(x2, y2, color3));
      VERTEXES_COLORED.add(new DisplayUtils.Vec2fColored(x, y2, color4));
      drawVertexesList(matrix, VERTEXES_COLORED, 9, texture, bloom);
   }

   public static void drawVertexesList(MatrixStack matrix, List<DisplayUtils.Vec2fColored> vec2c, int begin, boolean texture, boolean bloom) {
      setupRenderRect(texture, bloom);
      buffer.func_181668_a(begin, texture ? DefaultVertexFormats.field_181709_i : DefaultVertexFormats.field_181706_f);

      try {
         int counter = 0;

         for(Iterator var6 = vec2c.iterator(); var6.hasNext(); ++counter) {
            DisplayUtils.Vec2fColored vec = (DisplayUtils.Vec2fColored)var6.next();
            float[] rgba = ColorUtils.rgba(vec.color);
            buffer.func_227888_a_(matrix.func_227866_c_().func_227870_a_(), vec.x, vec.y, 0.0F);
            if (texture) {
               float u = counter != 0 && counter != 3 ? 1.0F : 0.0F;
               float v = counter != 0 && counter != 1 ? 1.0F : 0.0F;
               buffer.func_225583_a_(u, v);
            }

            buffer.func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]);
            buffer.func_181675_d();
         }
      } finally {
         tessellator.func_78381_a();
         endRenderRect(bloom);
      }

   }

   public static void setupRenderRect(boolean texture, boolean bloom) {
      RenderSystem.enableBlend();
      RenderSystem.disableAlphaTest();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.shadeModel(7425);
      if (texture) {
         RenderSystem.enableTexture();
         RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, bloom ? DestFactor.ONE : DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
      } else {
         RenderSystem.disableTexture();
      }

      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
   }

   public static void endRenderRect(boolean bloom) {
      RenderSystem.enableAlphaTest();
      RenderSystem.enableCull();
      RenderSystem.shadeModel(7424);
      RenderSystem.defaultBlendFunc();
      if (bloom) {
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      }

      RenderSystem.enableTexture();
   }

   public static void drawLogo(float x, float y, float w, float h, boolean tab) {
      if (tab) {
         drawImage(new ResourceLocation("luminar/images/luminar.png"), x, y, w, h, -1);
      }

   }

   public static MatrixStack matrixFrom(MatrixStack matrices, ActiveRenderInfo camera) {
      matrices.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(0.0F));
      matrices.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(0.0F));
      return matrices;
   }

   public static class Vec2fColored {
      float x;
      float y;
      int color;

      public Vec2fColored(float x, float y, int c) {
         this.x = x;
         this.y = y;
         this.color = c;
      }
   }
}
