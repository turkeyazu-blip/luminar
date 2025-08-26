package ru.luminar.mixins.functions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.luminar.Luminar;

@Mixin({GameRenderer.class})
public abstract class AspectMixin {
   @Inject(
      method = {"getProjectionMatrix"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onGetProjectionMatrix(ActiveRenderInfo camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Matrix4f> cir) {
      Luminar luminar = Luminar.instance;
      if (luminar != null && luminar.functions.aspect.isEnabled()) {
         Matrix4f original = (Matrix4f)cir.getReturnValue();
         Minecraft mc = Minecraft.func_71410_x();
         float aspectRatio = (float)mc.func_228018_at_().func_198109_k() / (float)mc.func_228018_at_().func_198091_l();
         float modifiedAspect = aspectRatio * luminar.functions.aspect.res;
         Matrix4f modified = Matrix4f.func_195876_a(this.func_215311_a(camera, tickDelta, changingFov), modifiedAspect, 0.05F, this.func_205001_m() * 2.0F);
         cir.setReturnValue(modified);
      }

   }

   @Shadow
   public abstract double func_215311_a(ActiveRenderInfo var1, float var2, boolean var3);

   @Shadow
   public abstract float func_205001_m();
}
