package ru.luminar.mixins;

import net.minecraft.client.GameConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.luminar.Luminar;
import ru.luminar.events.impl.other.EventWorldChange;
import ru.luminar.utils.draw.font.Font;
import ru.luminar.utils.draw.font.Fonts;

@Mixin({Minecraft.class})
public class MinecraftMixin {
   @Inject(
      method = {"allowsMultiplayer"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isMultiplayerEnabled(CallbackInfoReturnable<Boolean> callbackInfo) {
      callbackInfo.setReturnValue(true);
   }

   @Inject(
      method = {"allowsChat"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isChatEnabled(CallbackInfoReturnable<Boolean> callbackInfo) {
      callbackInfo.setReturnValue(true);
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void initFonts(GameConfiguration p_i45547_1_, CallbackInfo ci) {
      Fonts.montserrat = new Font("ms.png", "ms.json");
      Fonts.consolas = new Font("consolas.ttf.png", "consolas.ttf.json");
      Fonts.sf_semibold = new Font("sfsemibold.png", "sfsemibold.json");
      Fonts.sf_regular = new Font("sfregular.png", "sfregular.json");
      Fonts.sf_medium = new Font("sfmedium.png", "sfmedium.json");
      Fonts.sfui = new Font("sfsemibold2.png", "sfsemibold2.json");
      Fonts.nuralphaicons = new Font("nurikalpha.ttf.png", "nurikalpha.ttf.json");
      Fonts.INTER_MEDIUM = new Font("intermedium.png", "intermedium.json");
      Fonts.sfbold = new Font("sfbold.png", "sfbold.json");
      Fonts.sfMedium = new Font("sfmedium2.png", "sfmedium2.json");
      Fonts.poppinsSb = new Font("poppinssb.png", "poppinssb.json");
      Fonts.poppinsBold = new Font("poppinsbold.png", "poppinsbold.json");
      Fonts.promobold = new Font("promobold.png", "promobold.json");
      Fonts.promomed = new Font("promomedium.png", "promomedium.json");
   }

   @Inject(
      method = {"setLevel"},
      at = {@At("HEAD")}
   )
   public void inita(ClientWorld p_71403_1_, CallbackInfo ci) {
      Luminar.instance.eventBus.post(new EventWorldChange());
   }
}
