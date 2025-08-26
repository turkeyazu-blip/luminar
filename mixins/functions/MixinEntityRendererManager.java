package ru.luminar.mixins.functions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.entity.PartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.HitboxCustomizer;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.RenderUtils;

@Mixin({EntityRendererManager.class})
public abstract class MixinEntityRendererManager {
   @Inject(
      method = {"renderHitbox"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderHitbox(MatrixStack matrixStack, IVertexBuilder vertexBuilder, Entity entity, float partialTicks, CallbackInfo ci) {
      if (Luminar.instance.functions.hitboxCustomizer.isEnabled()) {
         ci.cancel();
         float width = entity.func_213311_cf() / 2.0F;
         this.renderCustomBox(matrixStack, vertexBuilder, entity, 1.0F, 1.0F, 1.0F);
         if (entity.isMultipartEntity()) {
            double xOffset = -MathHelper.func_219803_d((double)partialTicks, entity.field_70142_S, entity.func_226277_ct_());
            double yOffset = -MathHelper.func_219803_d((double)partialTicks, entity.field_70137_T, entity.func_226278_cu_());
            double zOffset = -MathHelper.func_219803_d((double)partialTicks, entity.field_70136_U, entity.func_226281_cx_());
            PartEntity[] var13 = entity.getParts();
            int var14 = var13.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               PartEntity<?> part = var13[var15];
               matrixStack.func_227860_a_();
               double px = xOffset + MathHelper.func_219803_d((double)partialTicks, part.field_70142_S, part.func_226277_ct_());
               double py = yOffset + MathHelper.func_219803_d((double)partialTicks, part.field_70137_T, part.func_226278_cu_());
               double pz = zOffset + MathHelper.func_219803_d((double)partialTicks, part.field_70136_U, part.func_226281_cx_());
               matrixStack.func_227861_a_(px, py, pz);
               this.renderCustomBox(matrixStack, vertexBuilder, part, 0.25F, 1.0F, 0.0F);
               matrixStack.func_227865_b_();
            }
         }

         if (!(Boolean)HitboxCustomizer.hideLook.get()) {
            if (entity instanceof LivingEntity) {
               float eyeHeight = entity.func_70047_e();
               WorldRenderer.func_228427_a_(matrixStack, vertexBuilder, (double)(-width), (double)(eyeHeight - 0.01F), (double)(-width), (double)width, (double)(eyeHeight + 0.01F), (double)width, 1.0F, 0.0F, 0.0F, 1.0F);
            }

            Vector3d viewVector = entity.func_70676_i(partialTicks);
            Matrix4f matrix = matrixStack.func_227866_c_().func_227870_a_();
            vertexBuilder.func_227888_a_(matrix, 0.0F, entity.func_70047_e(), 0.0F).func_225586_a_(0, 0, 255, 255).func_181675_d();
            vertexBuilder.func_227888_a_(matrix, (float)(viewVector.field_72450_a * 2.0D), (float)((double)entity.func_70047_e() + viewVector.field_72448_b * 2.0D), (float)(viewVector.field_72449_c * 2.0D)).func_225586_a_(0, 0, 255, 255).func_181675_d();
         }

      }
   }

   private void renderBox(MatrixStack matrixStack, Entity entity) {
      AxisAlignedBB box = entity.func_174813_aQ().func_72317_d(-entity.func_226277_ct_(), -entity.func_226278_cu_(), -entity.func_226281_cx_());
      RenderUtils.drawBBCorrectNiamNiam(box, (Boolean)HitboxCustomizer.clientColor.get() ? Luminar.instance.styleManager.getCurrentStyle().getColor(90, 10) : (Integer)HitboxCustomizer.colorr.get(), 1.0F, (Boolean)HitboxCustomizer.lines.get(), (Boolean)HitboxCustomizer.fill.get(), (Float)HitboxCustomizer.alpha.get(), (Float)HitboxCustomizer.alphaFill.get());
   }

   private void renderCustomBox(MatrixStack matrixStack, IVertexBuilder vertexBuilder, Entity entity, float r, float g, float b) {
      AxisAlignedBB aabb = entity.func_174813_aQ().func_72317_d(-entity.func_226277_ct_(), -entity.func_226278_cu_(), -entity.func_226281_cx_());
      int color = (Boolean)HitboxCustomizer.clientColor.get() ? Luminar.instance.styleManager.getCurrentStyle().getFirstColor().getRGB() : (Integer)HitboxCustomizer.colorr.get();
      float[] colors = ColorUtils.rgba(color);
      float red = colors[0];
      float green = colors[1];
      float blue = colors[2];
      float alpha = (Float)HitboxCustomizer.alpha.get();
      if ((Boolean)HitboxCustomizer.lines.get()) {
         WorldRenderer.func_228430_a_(matrixStack, vertexBuilder, aabb, red, green, blue, alpha);
      }

   }
}
