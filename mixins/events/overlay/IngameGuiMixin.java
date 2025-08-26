package ru.luminar.mixins.events.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.overlay.EventOverlay;

@Mixin({IngameGui.class})
public abstract class IngameGuiMixin {
   @Shadow
   public abstract FontRenderer func_175179_f();

   @Inject(
      method = {"displayScoreboardSidebar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onTab(MatrixStack matrixStack, ScoreObjective objective, CallbackInfo ci) {
      EventOverlay overlay = new EventOverlay(EventOverlay.Overlays.SCOREBOARD);
      Luminar.instance.eventBus.post(overlay);
      if (overlay.isCancel()) {
         ci.cancel();
         overlay.open();
      }

   }
}
