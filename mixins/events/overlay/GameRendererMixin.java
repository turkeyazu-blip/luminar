package ru.luminar.mixins.events.overlay;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.overlay.EventOverlay;

@Mixin({GameRenderer.class})
public class GameRendererMixin {
   @Inject(
      method = {"bobHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void cancel(CallbackInfo ci) {
      EventOverlay overlay = new EventOverlay(EventOverlay.Overlays.HURT);
      Luminar.instance.eventBus.post(overlay);
      if (overlay.isCancel()) {
         ci.cancel();
         overlay.open();
      }

   }
}
