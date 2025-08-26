package ru.luminar.mixins.events.overlay;

import net.minecraft.client.gui.overlay.BossOverlayGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.overlay.EventOverlay;

@Mixin({BossOverlayGui.class})
public class BosslineMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void addEvent(CallbackInfo info) {
      EventOverlay eventOverlay = new EventOverlay(EventOverlay.Overlays.BOSS_LINE);
      Luminar.instance.eventBus.post(eventOverlay);
      if (eventOverlay.isCancel()) {
         info.cancel();
         eventOverlay.open();
      }

   }
}
