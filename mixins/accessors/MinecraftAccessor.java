package ru.luminar.mixins.accessors;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Minecraft.class})
public interface MinecraftAccessor {
   @Accessor("user")
   void setSession(Session var1);

   @Accessor("user")
   Session getSession();

   @Accessor("fps")
   int getDebugFPS();
}
