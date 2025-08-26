package ru.luminar.mixins.accessors;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({WorldRenderer.class})
public interface FrustumOtdai {
   @Accessor("capturedFrustum")
   ClippingHelper getCapturedFrustum();

   @Accessor("captureFrustum")
   void setCaptureFrustum(boolean var1);
}
