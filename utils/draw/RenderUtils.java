package ru.luminar.utils.draw;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import ru.luminar.utils.client.IMinecraft;

public class RenderUtils {
   public static void drawBlockBox(BlockPos blockPos, int color) {
      drawBox((new AxisAlignedBB(blockPos)).func_72317_d(-IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c), color);
   }

   public static void drawBBCorrect(AxisAlignedBB bb, int color) {
      drawBox(bb.func_72317_d(-IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c), color);
   }

   public static void drawBBCorrectNiamNiam(AxisAlignedBB bb, int color, float lineW, boolean outline, boolean fill, float outlF, float fillAlpha) {
      AxisAlignedBB top = bb.func_72317_d(-IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c);
      drawBox(top, color, lineW, outline, fill, outlF, fillAlpha);
   }

   public static void drawBlockNiamNiam(BlockPos pos, int color, float lineW, boolean outline, boolean fill, float outlF, float fillAlpha) {
      AxisAlignedBB top = (new AxisAlignedBB(pos)).func_72317_d(-IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -IMinecraft.mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c);
      drawBox(top, color, lineW, outline, fill, outlF, fillAlpha);
   }

   public static void drawBox(AxisAlignedBB bb, int color) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glLineWidth(2.0F);
      float[] rgb = ColorUtils.rgba(color);
      GlStateManager.func_227702_d_(rgb[0], rgb[1], rgb[2], rgb[3]);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexbuffer = tessellator.func_178180_c();
      vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      tessellator.func_78381_a();
      vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      tessellator.func_78381_a();
      vertexbuffer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      vertexbuffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_227885_a_(rgb[0], rgb[1], rgb[2], rgb[3]).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_227702_d_(rgb[0], rgb[1], rgb[2], rgb[3]);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawBox(AxisAlignedBB bb, int color, float lineWidth, boolean outline, boolean fill, float outlAlpha, float fillAlpha) {
      GlStateManager.func_227626_N_();
      GlStateManager.func_227621_I_();
      GlStateManager.func_227740_m_();
      GlStateManager.func_227700_d_();
      GlStateManager.func_227676_b_(770, 771);
      GlStateManager.func_227762_u_(7425);
      float[] rgb = ColorUtils.rgba(color);
      if (fill) {
         GlStateManager.func_227702_d_(rgb[0], rgb[1], rgb[2], fillAlpha);
         renderFilledBox(bb);
      }

      if (outline) {
         GL11.glEnable(2848);
         GL11.glHint(3154, 4354);
         GL11.glLineWidth(lineWidth);
         GlStateManager.func_227702_d_(rgb[0], rgb[1], rgb[2], outlAlpha);
         renderOutlineBox(bb);
         GL11.glDisable(2848);
      }

      GlStateManager.func_227762_u_(7424);
      GlStateManager.func_227709_e_();
      GlStateManager.func_227619_H_();
      GlStateManager.func_227737_l_();
      GlStateManager.func_227627_O_();
   }

   public static void drawRoundFace(float x, float y, float width, float height, float radius, float alpha, AbstractClientPlayerEntity target) {
            try {
                ResourceLocation skin = target.getLocationSkin();
                mc.getTextureManager().bindTexture(skin);
                // Вычисляем процент повреждения
                float hurtPercent = getHurtPercent(target);

                RenderSystem.pushMatrix();
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(770, 771);
                // Подключаем шейдер для округлой головы
                ShaderUtil.FACE_ROUND.attach();

                // Передаем данные в шейдер
                ShaderUtil.FACE_ROUND.setUniform("location", x * 2, sr.getHeight() - height * 2 - y * 2);
                ShaderUtil.FACE_ROUND.setUniform("size", width * 2, height * 2);
                ShaderUtil.FACE_ROUND.setUniform("texture", 0);
                ShaderUtil.FACE_ROUND.setUniform("radius", radius * 2);
                ShaderUtil.FACE_ROUND.setUniform("alpha", alpha);
                ShaderUtil.FACE_ROUND.setUniform("hurtPercent", hurtPercent);  // Передаем процент повреждений в шейдер
                ShaderUtil.FACE_ROUND.setUniform("u", (1f / 64) * 8);
                ShaderUtil.FACE_ROUND.setUniform("v", (1f / 64) * 8);
                ShaderUtil.FACE_ROUND.setUniform("w", 1f / 8);
                ShaderUtil.FACE_ROUND.setUniform("h", 1f / 8);


                quadsBegin(x, y, width, height, 7);

                // Отключаем шейдер
                ShaderUtil.FACE_ROUND.detach();

                RenderSystem.disableBlend();
                RenderSystem.popMatrix();
            } catch(Exception e){
                e.printStackTrace();
   }

   private static void renderFilledBox(AxisAlignedBB bb) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      tessellator.func_78381_a();
   }

   private static void renderOutlineBox(AxisAlignedBB bb) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(2, DefaultVertexFormats.field_181705_e);
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      tessellator.func_78381_a();
      buffer.func_181668_a(2, DefaultVertexFormats.field_181705_e);
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      tessellator.func_78381_a();
      buffer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
      buffer.func_225582_a_(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
      tessellator.func_78381_a();
   }
}
