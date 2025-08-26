package ru.luminar.mixins.functions.worldtweaks;

import net.minecraft.network.play.server.SUpdateTimePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SUpdateTimePacket.class})
public interface WorldTweaksPacketMixin {
   @Accessor("gameTime")
   void setGameTime(long var1);

   @Accessor("dayTime")
   void setDayTime(long var1);
}
