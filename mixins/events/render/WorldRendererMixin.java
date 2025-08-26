package ru.luminar.mixins.events.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.render.Render3DEvent;
import ru.luminar.events.impl.render.Render3DEvent2;

@Mixin({WorldRenderer.class})
public abstract class WorldRendererMixin {
   Render3DEvent render = new Render3DEvent(new MatrixStack(), 1.0F);
   Render3DEvent2 render3DEvent = new Render3DEvent2(new MatrixStack(), 1.0F);

   @Inject(
      method = {"renderLevel"},
      at = {@At(
   value = "INVOKE",
   target = "Lcom/mojang/blaze3d/systems/RenderSystem;multMatrix(Lnet/minecraft/util/math/vector/Matrix4f;)V",
   shift = Shift.AFTER,
   ordinal = 1
)},
      cancellable = false
   )
   public void onRenderLevelPostMultMatrix(MatrixStack p_228426_1_, float p_228426_2_, long p_228426_3_, boolean p_228426_5_, ActiveRenderInfo p_228426_6_, GameRenderer p_228426_7_, LightTexture p_228426_8_, Matrix4f p_228426_9_, CallbackInfo ci) {
      this.render.setPartialTicks(p_228426_2_);
      this.render.setStack(p_228426_1_);
      Luminar.instance.eventBus.post(this.render);
   }

   @Inject(
      method = {"renderLevel"},
      at = {@At(
   value = "INVOKE",
   target = "Lcom/mojang/blaze3d/systems/RenderSystem;pushMatrix()V",
   shift = Shift.AFTER,
   ordinal = 1
)},
      cancellable = false
   )
   public void onRenderLevelEnd(MatrixStack p_228426_1_, float p_228426_2_, long p_228426_3_, boolean p_228426_5_, ActiveRenderInfo p_228426_6_, GameRenderer p_228426_7_, LightTexture p_228426_8_, Matrix4f p_228426_9_, CallbackInfo ci) {
      this.render3DEvent.setPartialTicks(p_228426_2_);
      this.render3DEvent.setStack(p_228426_1_);
      Luminar.instance.eventBus.post(this.render3DEvent);
   }
}
