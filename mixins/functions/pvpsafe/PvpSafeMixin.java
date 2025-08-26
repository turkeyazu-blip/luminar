package ru.luminar.mixins.functions.pvpsafe;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.utils.client.ClientUtil;
import ru.luminar.utils.client.IMinecraft;

@Mixin({ClientPlayerEntity.class})
public class PvpSafeMixin implements IMinecraft {
   @Inject(
      method = {"chat"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void nonono(String chat, CallbackInfo ci) {
      if ((chat.startsWith("/hub") || chat.startsWith("/an")) && Luminar.instance.functions.pvpSafe.isEnabled() && ClientUtil.isPvp()) {
         this.print("В пвп нельзя выйти! Подождите или отключите PVPSafe");
         ci.cancel();
      }

   }
}
