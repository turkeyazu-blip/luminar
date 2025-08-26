package ru.luminar.utils.draw.font;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public final class MsdfFont {
   private final String name;
   private final Texture texture;
   private final FontData.AtlasData atlas;
   private final FontData.MetricsData metrics;
   private final Map<Integer, MsdfGlyph> glyphs;
   private boolean filtered = false;

   public String getName() {
      return this.name;
   }

   public FontData.AtlasData getAtlas() {
      return this.atlas;
   }

   public FontData.MetricsData getMetrics() {
      return this.metrics;
   }

   private MsdfFont(String name, Texture texture, FontData.AtlasData atlas, FontData.MetricsData metrics, Map<Integer, MsdfGlyph> glyphs) {
      this.name = name;
      this.texture = texture;
      this.atlas = atlas;
      this.metrics = metrics;
      this.glyphs = glyphs;
   }

   public void bind() {
      GlStateManager.func_227760_t_(this.texture.func_110552_b());
      if (!this.filtered) {
         this.texture.setBlurMipmap(true, false);
         this.filtered = true;
      }

   }

   public void unbind() {
      GlStateManager.func_227760_t_(0);
   }

   public void applyGlyphs(Matrix4f matrix, IVertexBuilder processor, float size, String text, float thickness, float x, float y, float z, int red, int green, int blue, int alpha) {
      for(int i = 0; i < text.length(); ++i) {
         int _char = text.charAt(i);
         MsdfGlyph glyph = (MsdfGlyph)this.glyphs.get(Integer.valueOf(_char));
         if (glyph != null) {
            x += glyph.apply(matrix, processor, size, x, y, z, red, green, blue, alpha) + thickness;
         }
      }

   }

   public float getWidth(String text, float size) {
      float width = 0.0F;

      for(int i = 0; i < text.length(); ++i) {
         int _char = text.charAt(i);
         MsdfGlyph glyph = (MsdfGlyph)this.glyphs.get(Integer.valueOf(_char));
         if (glyph != null) {
            width += glyph.getWidth(size);
         }
      }

      return width;
   }

   public float getWidth(String text, float size, float thickness) {
      float width = 0.0F;

      for(int i = 0; i < text.length(); ++i) {
         int _char = text.charAt(i);
         MsdfGlyph glyph = (MsdfGlyph)this.glyphs.get(Integer.valueOf(_char));
         if (glyph != null) {
            width += glyph.getWidth(size) + thickness;
         }
      }

      return width;
   }

   public static MsdfFont.Builder builder() {
      return new MsdfFont.Builder();
   }

   public static class Builder {
      public static final String MSDF_PATH = "luminar/fonts/";
      private String name = "undefined";
      private ResourceLocation dataFile;
      private ResourceLocation atlasFile;

      private Builder() {
      }

      public MsdfFont.Builder withName(String name) {
         this.name = name;
         return this;
      }

      public MsdfFont.Builder withData(String dataFile) {
         this.dataFile = new ResourceLocation("luminar/fonts/" + dataFile);
         return this;
      }

      public MsdfFont.Builder withAtlas(String atlasFile) {
         this.atlasFile = new ResourceLocation("luminar/fonts/" + atlasFile);
         return this;
      }

      public MsdfFont build() {
         FontData data = (FontData)IOUtils.fromJsonToInstance(this.dataFile, FontData.class);
         Texture texture = IOUtils.toTexture(this.atlasFile);
         if (data == null) {
            throw new RuntimeException("Failed to read font data file: " + this.dataFile.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax.");
         } else {
            float aWidth = data.atlas().width();
            float aHeight = data.atlas().height();
            Map<Integer, MsdfGlyph> glyphs = (Map)data.glyphs().stream().collect(Collectors.toMap((glyphData) -> {
               return glyphData.unicode();
            }, (glyphData) -> {
               return new MsdfGlyph(glyphData, aWidth, aHeight);
            }));
            return new MsdfFont(this.name, texture, data.atlas(), data.metrics(), glyphs);
         }
      }
   }
}
