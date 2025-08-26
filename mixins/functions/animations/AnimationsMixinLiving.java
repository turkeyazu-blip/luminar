package ru.luminar.mixins.functions.animations;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.Animations;

@Mixin({LivingEntity.class})
public class AnimationsMixinLiving {
   @Inject(
      method = {"getCurrentSwingDuration"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getCooledDuration(CallbackInfoReturnable<Integer> cir) {
      Animations animations = Luminar.instance.functions.animations;
      if (animations.isEnabled()) {
         cir.setReturnValue(25 - ((Float)animations.speed.get()).intValue() * 2);
      }

   }
}
