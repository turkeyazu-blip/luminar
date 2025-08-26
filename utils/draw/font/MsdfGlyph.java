package ru.luminar.utils.draw.font;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.vector.Matrix4f;

public final class MsdfGlyph {
   private final int code;
   private final float minU;
   private final float maxU;
   private final float minV;
   private final float maxV;
   private final float advance;
   private final float topPosition;
   private final float width;
   private final float height;
   private final float leftBearing;

   public MsdfGlyph(FontData.GlyphData data, float atlasWidth, float atlasHeight) {
      this.code = data.unicode();
      this.advance = data.advance();
      FontData.BoundsData atlasBounds = data.atlasBounds();
      if (atlasBounds != null) {
         this.minU = atlasBounds.left() / atlasWidth;
         this.maxU = atlasBounds.right() / atlasWidth;
         this.minV = 1.0F - atlasBounds.top() / atlasHeight;
         this.maxV = 1.0F - atlasBounds.bottom() / atlasHeight;
      } else {
         this.minU = 0.0F;
         this.maxU = 0.0F;
         this.minV = 0.0F;
         this.maxV = 0.0F;
      }

      FontData.BoundsData planeBounds = data.planeBounds();
      if (planeBounds != null) {
         this.width = planeBounds.right() - planeBounds.left();
         this.height = planeBounds.top() - planeBounds.bottom();
         this.topPosition = planeBounds.top();
         this.leftBearing = planeBounds.left();
      } else {
         this.width = 0.0F;
         this.height = 0.0F;
         this.topPosition = 0.0F;
         this.leftBearing = 0.0F;
      }

   }

   public float apply(Matrix4f matrix, IVertexBuilder processor, float size, float x, float y, float z, int red, int green, int blue, int alpha) {
      x += this.leftBearing * size;
      y -= this.topPosition * size;
      --y;
      float width = this.width * size;
      float height = this.height * size;
      processor.func_227888_a_(matrix, x, y, z).func_225586_a_(red, green, blue, alpha).func_225583_a_(this.minU, this.minV).func_181675_d();
      processor.func_227888_a_(matrix, x, y + height, z).func_225586_a_(red, green, blue, alpha).func_225583_a_(this.minU, this.maxV).func_181675_d();
      processor.func_227888_a_(matrix, x + width, y + height, z).func_225586_a_(red, green, blue, alpha).func_225583_a_(this.maxU, this.maxV).func_181675_d();
      processor.func_227888_a_(matrix, x + width, y, z).func_225586_a_(red, green, blue, alpha).func_225583_a_(this.maxU, this.minV).func_181675_d();
      return this.advance * size + (Character.isSpaceChar(this.code) ? 0.25F : 0.0F);
   }

   public float getWidth(float size) {
      return Character.isSpaceChar(this.code) ? this.advance * size + 0.25F : this.advance * size;
   }

   public int getCharCode() {
      return this.code;
   }
}
