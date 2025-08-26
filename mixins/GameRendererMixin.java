package ru.luminar.mixins;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.luminar.utils.client.FovTracker;

@Mixin({GameRenderer.class})
public abstract class GameRendererMixin {
   @Inject(
      method = {"getFov"},
      at = {@At("RETURN")}
   )
   private void onGetFov(ActiveRenderInfo camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
      FovTracker.setLastFov((Double)cir.getReturnValue());
   }
}
