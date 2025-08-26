package ru.luminar.utils.draw.font;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.shader.ShaderUtil;

public class Font implements IMinecraft {
   private final String atlas;
   private final String data;
   private final MsdfFont font;

   public Font(String atlas, String data) {
      this.atlas = atlas;
      this.data = data;
      this.font = MsdfFont.builder().withAtlas(atlas).withData(data).build();
   }

   public void drawText(MatrixStack stack, String text, float x, float y, int color, float size) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      ShaderUtil shader = ShaderUtil.textShader;
      FontData.AtlasData atlas = this.font.getAtlas();
      shader.attach();
      shader.setUniform("Sampler", 0);
      shader.setUniform("EdgeStrength", 0.5F);
      shader.setUniform("TextureSize", atlas.width(), atlas.height());
      shader.setUniform("Range", atlas.range());
      shader.setUniform("Thickness", 0.0F);
      shader.setUniform("Outline", 0);
      shader.setUniform("OutlineThickness", 0.0F);
      shader.setUniform("OutlineColor", 1.0F, 1.0F, 1.0F, 1.0F);
      shader.setUniform("color", ColorUtils.rgba(color));
      this.font.bind();
      GlStateManager.func_227740_m_();
      Tessellator.func_178181_a().func_178180_c().func_181668_a(7, DefaultVertexFormats.field_227851_o_);
      this.font.applyGlyphs(stack.func_227866_c_().func_227870_a_(), Tessellator.func_178181_a().func_178180_c(), size, text, -0.2F, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, 255, 255, 255, 255);
      Tessellator.func_178181_a().func_78381_a();
      this.font.unbind();
      shader.detach();
   }

   public void drawText(MatrixStack stack, ITextComponent text, float x, float y, float size, int alpha) {
      float offset = 0.0F;
      Iterator var8 = text.func_150253_a().iterator();

      while(var8.hasNext()) {
         ITextComponent it = (ITextComponent)var8.next();

         String draw;
         for(Iterator var10 = it.func_150253_a().iterator(); var10.hasNext(); offset += this.getWidth(draw, size)) {
            ITextComponent it1 = (ITextComponent)var10.next();
            draw = it1.getString();
            if (it1.func_150256_b().func_240711_a_() != null) {
               this.drawText(stack, draw, x + offset, y, ColorUtils.setAlpha(ColorUtils.toColor(it1.func_150256_b().func_240711_a_().func_240747_b_()), alpha), size);
            } else {
               this.drawText(stack, draw, x + offset, y, ColorUtils.setAlpha(Color.GRAY.getRGB(), alpha), size);
            }
         }

         if (it.func_150253_a().size() <= 1) {
            String draw = TextFormatting.func_110646_a(it.getString());
            this.drawText(stack, draw, x + offset, y, ColorUtils.setAlpha(it.func_150256_b().func_240711_a_() == null ? Color.GRAY.getRGB() : it.func_150256_b().func_240711_a_().func_240742_a_(), alpha), size);
            offset += this.getWidth(draw, size);
         }
      }

      if (text.func_150253_a().isEmpty()) {
         String draw = TextFormatting.func_110646_a(text.getString());
         this.drawText(stack, draw, x + offset, y, ColorUtils.setAlpha(text.func_150256_b().func_240711_a_() == null ? Color.GRAY.getRGB() : text.func_150256_b().func_240711_a_().func_240742_a_(), alpha), size);
         float var10000 = offset + this.getWidth(draw, size);
      }

   }

   public float getWidth(ITextComponent text, float size) {
      float offset = 0.0F;
      Iterator var4 = text.func_150253_a().iterator();

      while(var4.hasNext()) {
         ITextComponent it = (ITextComponent)var4.next();

         String draw;
         for(Iterator var6 = it.func_150253_a().iterator(); var6.hasNext(); offset += this.getWidth(draw, size)) {
            ITextComponent it1 = (ITextComponent)var6.next();
            draw = it1.getString();
         }

         if (it.func_150253_a().size() <= 1) {
            String draw = TextFormatting.func_110646_a(it.getString());
            offset += this.getWidth(draw, size);
         }
      }

      if (text.func_150253_a().isEmpty()) {
         String draw = TextFormatting.func_110646_a(text.getString());
         offset += this.getWidth(draw, size);
      }

      return offset;
   }

   public void drawTextBuilding(MatrixStack stack, String text, float x, float y, int color, float size) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      ShaderUtil shader = ShaderUtil.textShader;
      FontData.AtlasData atlas = this.font.getAtlas();
      shader.attach();
      shader.setUniform("Sampler", 0);
      shader.setUniform("EdgeStrength", 0.5F);
      shader.setUniform("TextureSize", atlas.width(), atlas.height());
      shader.setUniform("Range", atlas.range());
      shader.setUniform("Thickness", 0.0F);
      shader.setUniform("Outline", 0);
      shader.setUniform("OutlineThickness", 0.0F);
      shader.setUniform("OutlineColor", 1.0F, 1.0F, 1.0F, 1.0F);
      shader.setUniform("color", ColorUtils.rgba(color));
      this.font.bind();
      GlStateManager.func_227740_m_();
      this.font.applyGlyphs(stack.func_227866_c_().func_227870_a_(), Tessellator.func_178181_a().func_178180_c(), size, text, 0.0F, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, 255, 255, 255, 255);
      this.font.unbind();
      shader.detach();
   }

   public void drawCenteredText(MatrixStack stack, String text, float x, float y, int color, float size) {
      this.drawText(stack, text, x - this.getWidth(text, size) / 2.0F, y, color, size);
   }

   public void drawCenteredText(MatrixStack stack, ITextComponent text, float x, float y, float size) {
      this.drawText(stack, text, x - this.getWidth(text, size) / 2.0F, y, size, 255);
   }

   public void drawCenteredTextWithOutline(MatrixStack stack, String text, float x, float y, int color, float size) {
      this.drawTextWithOutline(stack, text, x - this.getWidth(text, size) / 2.0F, y, color, size, 0.05F);
   }

   public void drawCenteredTextEmpty(MatrixStack stack, String text, float x, float y, int color, float size) {
      this.drawEmpty(stack, text, x - this.getWidth(text, size) / 2.0F, y, size, color, 0.0F);
   }

   public void drawCenteredTextEmptyOutline(MatrixStack stack, String text, float x, float y, int color, float size) {
      this.drawEmptyWithOutline(stack, text, x - this.getWidth(text, size) / 2.0F, y, size, color, 0.0F);
   }

   public void drawCenteredText(MatrixStack stack, String text, float x, float y, int color, float size, float thickness) {
      this.drawText(stack, text, x - this.getWidth(text, size, thickness) / 2.0F, y, color, size, thickness);
   }

   public void drawText(MatrixStack stack, String text, float x, float y, int color, float size, float thickness) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      ShaderUtil shader = ShaderUtil.textShader;
      FontData.AtlasData atlas = this.font.getAtlas();
      shader.attach();
      shader.setUniform("Sampler", 0);
      shader.setUniform("EdgeStrength", 0.5F);
      shader.setUniform("TextureSize", atlas.width(), atlas.height());
      shader.setUniform("Range", atlas.range());
      shader.setUniform("Thickness", thickness);
      shader.setUniform("color", ColorUtils.rgba(color));
      shader.setUniform("Outline", 0);
      shader.setUniform("OutlineThickness", 0.0F);
      shader.setUniform("OutlineColor", 1.0F, 1.0F, 1.0F, 1.0F);
      this.font.bind();
      GlStateManager.func_227740_m_();
      Tessellator.func_178181_a().func_178180_c().func_181668_a(7, DefaultVertexFormats.field_227851_o_);
      this.font.applyGlyphs(stack.func_227866_c_().func_227870_a_(), Tessellator.func_178181_a().func_178180_c(), size, text, thickness, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, 255, 255, 255, 255);
      Tessellator.func_178181_a().func_78381_a();
      this.font.unbind();
      shader.detach();
   }

   public void drawTextWithOutline(MatrixStack stack, String text, float x, float y, int color, float size, float thickness) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      ShaderUtil shader = ShaderUtil.textShader;
      FontData.AtlasData atlas = this.font.getAtlas();
      shader.attach();
      shader.setUniform("Sampler", 0);
      shader.setUniform("EdgeStrength", 0.5F);
      shader.setUniform("TextureSize", atlas.width(), atlas.height());
      shader.setUniform("Range", atlas.range());
      shader.setUniform("Thickness", thickness);
      shader.setUniform("color", ColorUtils.rgba(color));
      shader.setUniform("Outline", 1);
      shader.setUniform("OutlineThickness", 0.2F);
      shader.setUniform("OutlineColor", 0.0F, 0.0F, 0.0F, 1.0F);
      this.font.bind();
      GlStateManager.func_227740_m_();
      Tessellator.func_178181_a().func_178180_c().func_181668_a(7, DefaultVertexFormats.field_227851_o_);
      this.font.applyGlyphs(stack.func_227866_c_().func_227870_a_(), Tessellator.func_178181_a().func_178180_c(), size, text, thickness, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, 255, 255, 255, 255);
      Tessellator.func_178181_a().func_78381_a();
      this.font.unbind();
      shader.detach();
   }

   public void init(float thickness, float smoothness) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      ShaderUtil shader = ShaderUtil.textShader;
      FontData.AtlasData atlas = this.font.getAtlas();
      shader.attach();
      shader.setUniform("Sampler", 0);
      shader.setUniform("EdgeStrength", smoothness);
      shader.setUniform("TextureSize", atlas.width(), atlas.height());
      shader.setUniform("Range", atlas.range());
      shader.setUniform("Thickness", thickness);
      shader.setUniform("Outline", 0);
      shader.setUniform("OutlineThickness", 0.0F);
      shader.setUniform("OutlineColor", 1.0F, 1.0F, 1.0F, 1.0F);
      this.font.bind();
      GlStateManager.func_227740_m_();
      Tessellator.func_178181_a().func_178180_c().func_181668_a(7, DefaultVertexFormats.field_227851_o_);
   }

   public void drawEmpty(MatrixStack stack, String text, float x, float y, float size, int color, float thickness) {
      ShaderUtil shader = ShaderUtil.textShader;
      shader.setUniform("color", ColorUtils.rgba(color));
      this.font.applyGlyphs(stack.func_227866_c_().func_227870_a_(), Tessellator.func_178181_a().func_178180_c(), size, text, thickness, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, 255, 255, 255, 255);
   }

   public void drawEmptyWithOutline(MatrixStack stack, String text, float x, float y, float size, int color, float thickness) {
      ShaderUtil shader = ShaderUtil.textShader;
      shader.setUniform("Outline", 1);
      shader.setUniform("OutlineThickness", 0.2F);
      shader.setUniform("OutlineColor", 0.0F, 0.0F, 0.0F, 1.0F);
      shader.setUniform("color", ColorUtils.rgba(color));
      this.font.applyGlyphs(stack.func_227866_c_().func_227870_a_(), Tessellator.func_178181_a().func_178180_c(), size, text, thickness, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, 255, 255, 255, 255);
   }

   public void end() {
      Tessellator.func_178181_a().func_78381_a();
      this.font.unbind();
      ShaderUtil shader = ShaderUtil.textShader;
      shader.detach();
   }

   public float getWidth(String text, float size) {
      return this.font.getWidth(text, size);
   }

   public float getWidth(String text, float size, float thickness) {
      return this.font.getWidth(text, size, thickness);
   }

   public float getHeight(float size) {
      return size;
   }
}
