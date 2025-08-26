package ru.luminar.mixins.functions.thirdperson;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.feature.functions.impl.render.ThirdPerson;

@Mixin({Entity.class})
public abstract class MixinEntity {
   @Inject(
      method = {"turn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTurn(double yaw, double pitch, CallbackInfo ci) {
      if (ThirdPerson.active && Minecraft.func_71410_x().field_71462_r == null) {
         double d0 = pitch * 0.15D;
         double d1 = yaw * 0.15D;
         ThirdPerson.x = (float)((double)ThirdPerson.x + d1);
         ThirdPerson.y = (float)((double)ThirdPerson.y + d0);
         ThirdPerson.prevX = (float)((double)ThirdPerson.prevX + d1);
         ThirdPerson.prevY = (float)((double)ThirdPerson.prevY + d0);
         ci.cancel();
      }

   }
}
