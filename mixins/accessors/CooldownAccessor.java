package ru.luminar.mixins.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
   targets = {"net.minecraft.util.CooldownTracker$Cooldown"}
)
public interface CooldownAccessor {
   @Accessor("startTime")
   int getStartTime();

   @Accessor("endTime")
   int getEndTime();
}
