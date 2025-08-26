package ru.luminar.mixins.functions.worldtweaks;

import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.WorldTweaks;
import ru.luminar.utils.draw.ColorUtils;

@Mixin({FogRenderer.class})
public class WorldTweaksFogColor {
   @Shadow
   private static float field_205093_c;
   @Shadow
   private static float field_205094_d;
   @Shadow
   private static float field_205095_e;

   @Inject(
      method = {"setupColor"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/renderer/FogRenderer;fogRed:F",
   ordinal = 5,
   shift = Shift.BEFORE
)}
   )
   private static void setForGolor(CallbackInfo ci) {
      WorldTweaks worldTweaks = Luminar.instance.functions.worldTweaks;
      if (worldTweaks.isEnabled() && (Boolean)worldTweaks.fog.get()) {
         float[] colors = ColorUtils.rgba((Boolean)worldTweaks.clientColor.get() ? Luminar.instance.styleManager.getCurrentStyle().getFirstColor().darker().getRGB() : (Integer)worldTweaks.color.get());
         field_205093_c = colors[0];
         field_205094_d = colors[1];
         field_205095_e = colors[2];
      }

   }
}
