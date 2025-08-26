package ru.luminar.mixins.functions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.HitColor;

@Mixin({LivingRenderer.class})
public abstract class HitColorMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
   protected HitColorMixin(EntityRendererManager p_i46179_1_) {
      super(p_i46179_1_);
   }

   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/renderer/entity/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V"
)
   )
   private void redirectRenderToBuffer(M model, MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, T entity, float entityYaw, float partialTicks, MatrixStack originalMatrixStack, IRenderTypeBuffer originalBuffer, int originalPackedLight) {
      if (entity.field_70737_aN > 0 && Luminar.instance.functions.hitColor.isEnabled()) {
         int color = (Integer)HitColor.color.get();
         float customAlpha = 0.5F;
         float r = (float)(color >> 16 & 255) / 255.0F;
         float g = (float)(color >> 8 & 255) / 255.0F;
         float b = (float)(color & 255) / 255.0F;
         model.func_225598_a_(matrixStack, buffer, packedLight, packedOverlay, r, g, b, customAlpha);
      } else {
         model.func_225598_a_(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
      }

   }
}
