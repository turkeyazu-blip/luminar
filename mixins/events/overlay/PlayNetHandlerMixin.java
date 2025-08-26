package ru.luminar.mixins.events.overlay;

import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SEntityStatusPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.luminar.Luminar;
import ru.luminar.events.impl.overlay.EventOverlay;
import ru.luminar.utils.client.IMinecraft;

@Mixin({ClientPlayNetHandler.class})
public class PlayNetHandlerMixin {
   @Inject(
      method = {"handleEntityEvent"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/world/ClientWorld;playLocalSound(DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V",
   shift = Shift.AFTER
)},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void onAfterTotemSound(SEntityStatusPacket packet, CallbackInfo ci) {
      Entity entity = packet.func_149161_a(IMinecraft.mc.field_71441_e);
      if (packet.func_149160_c() == 35 && entity != null) {
         EventOverlay event = new EventOverlay(EventOverlay.Overlays.TOTEM);
         Luminar.instance.eventBus.post(event);
         if (event.isCancel()) {
            ci.cancel();
            event.open();
         }
      }

   }
}
