package ru.luminar.mixins.events.overlay;

import net.minecraft.client.renderer.OverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.overlay.EventOverlay;

@Mixin({OverlayRenderer.class})
public class OverlayMixin {
   @Inject(
      method = {"renderFire"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onEventPost(CallbackInfo ci) {
      EventOverlay overlay = new EventOverlay(EventOverlay.Overlays.FIRE_OVERLAY);
      Luminar.instance.eventBus.post(overlay);
      if (overlay.isCancel()) {
         ci.cancel();
         overlay.open();
      }

   }
}
