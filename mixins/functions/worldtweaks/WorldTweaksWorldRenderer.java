package ru.luminar.mixins.functions.worldtweaks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.WorldTweaks;
import ru.luminar.utils.draw.ColorUtils;

@Mixin({WorldRenderer.class})
public abstract class WorldTweaksWorldRenderer {
   @Inject(
      method = {"renderSky"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/vector/Vector3d;",
   shift = Shift.AFTER
)},
      cancellable = true
   )
   private void onRenderSkyColor(MatrixStack matrixStack, float partialTicks, CallbackInfo ci) {
      WorldTweaks worldTweaks = Luminar.instance.functions.worldTweaks;
      if (worldTweaks != null && worldTweaks.isEnabled() && (Boolean)worldTweaks.sky.get()) {
         int color = (Boolean)worldTweaks.clientColor.get() ? Luminar.instance.styleManager.getCurrentStyle().getFirstColor().darker().getRGB() : (Integer)worldTweaks.color.get();
         float[] colors = ColorUtils.rgba(color);
         RenderSystem.color3f(colors[0], colors[1], colors[2]);
         ci.cancel();
      }
   }

   @Redirect(
      method = {"renderSky"},
      at = @At(
   value = "FIELD",
   target = "Lnet/minecraft/util/math/vector/Vector3d;x:D",
   ordinal = 0
)
   )
   private double redirectSkyColorR(Vector3d vector3d) {
      return this.getModifiedSkyColorComponent(0, vector3d);
   }

   @Redirect(
      method = {"renderSky"},
      at = @At(
   value = "FIELD",
   target = "Lnet/minecraft/util/math/vector/Vector3d;y:D",
   ordinal = 0
)
   )
   private double redirectSkyColorG(Vector3d vector3d) {
      return this.getModifiedSkyColorComponent(1, vector3d);
   }

   @Redirect(
      method = {"renderSky"},
      at = @At(
   value = "FIELD",
   target = "Lnet/minecraft/util/math/vector/Vector3d;z:D",
   ordinal = 0
)
   )
   private double redirectSkyColorB(Vector3d vector3d) {
      return this.getModifiedSkyColorComponent(2, vector3d);
   }

   private double getModifiedSkyColorComponent(int component, Vector3d vector3d) {
      WorldTweaks worldTweaks = Luminar.instance.functions.worldTweaks;
      if (worldTweaks != null && worldTweaks.isEnabled() && (Boolean)worldTweaks.sky.get()) {
         int color = (Boolean)worldTweaks.clientColor.get() ? Luminar.instance.styleManager.getCurrentStyle().getFirstColor().darker().getRGB() : (Integer)worldTweaks.color.get();
         float[] colors = ColorUtils.rgba(color);
         return (double)colors[component];
      } else {
         return component == 0 ? vector3d.field_72450_a : (component == 1 ? vector3d.field_72448_b : vector3d.field_72449_c);
      }
   }
}
