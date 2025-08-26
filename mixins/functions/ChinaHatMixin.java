package ru.luminar.mixins.functions;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.ChinaHat;

@Mixin({HeadLayer.class})
public abstract class ChinaHatMixin<T extends LivingEntity, M extends EntityModel<T> & IHasHead> extends LayerRenderer<T, M> {
   public ChinaHatMixin(IEntityRenderer<T, M> p_i50926_1_) {
      super(p_i50926_1_);
   }

   @Inject(
      method = {"render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/Entity;FFFFFF)V"},
      at = {@At("TAIL")}
   )
   public void renderChinaHat(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, Entity p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_, CallbackInfo ci) {
      if (p_225628_4_ instanceof ClientPlayerEntity && Luminar.instance.functions.chinaHat.isEnabled()) {
         ChinaHat.draw(p_225628_1_, ((IHasHead)this.func_215332_c()).func_205072_a());
      }

   }
}
