package ru.luminar.mixins.functions.streamermode;

import net.minecraft.client.gui.IngameGui;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.utils.StreamerMode;

@Mixin({IngameGui.class})
public abstract class MixinIngameGui {
   @Redirect(
      method = {"displayScoreboardSidebar"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/scoreboard/ScorePlayerTeam;formatNameForTeam(Lnet/minecraft/scoreboard/Team;Lnet/minecraft/util/text/ITextComponent;)Lnet/minecraft/util/text/IFormattableTextComponent;"
)
   )
   private IFormattableTextComponent replaceScoreboardText(Team p_237500_0_, ITextComponent p_237500_1_) {
      String text = p_237500_1_.getString();
      if (Luminar.instance.functions.streamerMode.isEnabled()) {
         text = StreamerMode.replace(text);
      }

      return ScorePlayerTeam.func_237500_a_(p_237500_0_, new StringTextComponent(text));
   }
}
