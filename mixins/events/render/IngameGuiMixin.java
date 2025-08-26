package ru.luminar.mixins.events.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.render.Render2DEvent;

@Mixin({GameRenderer.class})
public abstract class IngameGuiMixin {
   Render2DEvent render2DEvent = new Render2DEvent(new MatrixStack(), 1.0F);

   @Inject(
      method = {"render"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/IngameGui;render(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V"
)}
   )
   private void beforeIngameGuiRender(float partialTicks, long nanoTime, boolean renderWorld, CallbackInfo ci) {
      this.render2DEvent.setPartialTicks(partialTicks);
      Luminar.instance.eventBus.post(this.render2DEvent);
   }
}
