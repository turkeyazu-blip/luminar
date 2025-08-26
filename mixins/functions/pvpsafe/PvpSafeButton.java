package ru.luminar.mixins.functions.pvpsafe;

import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.luminar.Luminar;
import ru.luminar.utils.client.ClientUtil;

@Mixin({IngameMenuScreen.class})
public class PvpSafeButton extends Screen {
   protected PvpSafeButton(ITextComponent p_i51108_1_) {
      super(p_i51108_1_);
   }

   @Redirect(
      method = {"createPauseMenu"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/screen/IngameMenuScreen;addButton(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;",
   ordinal = 7
)
   )
   private Widget modifyExitButton(IngameMenuScreen instance, Widget widget) {
      Button button = new Button(this.field_230708_k_ / 2 - 102, this.field_230709_l_ / 4 + 120 + -16, 204, 20, new TranslationTextComponent("menu.returnToMenu"), (buttonWidget) -> {
         if (ClientUtil.isPvp() && Luminar.instance.functions.pvpSafe.isEnabled()) {
            this.field_230706_i_.func_147108_a(new ConfirmScreen((confirmed) -> {
               if (confirmed) {
                  this.executeExitLogic(buttonWidget);
               } else {
                  this.field_230706_i_.func_147108_a(this);
               }

            }, new StringTextComponent("Вы уверены, что хотите выйти?"), new StringTextComponent("Нажмите на одну из кнопок ниже.")));
         } else {
            this.executeExitLogic(buttonWidget);
         }

      });
      if (!this.field_230706_i_.func_71387_A()) {
         button.func_238482_a_(new TranslationTextComponent("menu.disconnect"));
      }

      return this.func_230480_a_(button);
   }

   private void executeExitLogic(Widget button) {
      boolean isSingleplayer = this.field_230706_i_.func_71387_A();
      boolean isRealms = this.field_230706_i_.func_181540_al();
      button.field_230693_o_ = false;
      this.field_230706_i_.field_71441_e.func_72882_A();
      if (isSingleplayer) {
         this.field_230706_i_.func_213231_b(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
      } else {
         this.field_230706_i_.func_213254_o();
      }

      if (isSingleplayer) {
         this.field_230706_i_.func_147108_a(new MainMenuScreen());
      } else if (isRealms) {
         RealmsBridgeScreen realmsScreen = new RealmsBridgeScreen();
         realmsScreen.func_231394_a_(new MainMenuScreen());
      } else {
         this.field_230706_i_.func_147108_a(new MultiplayerScreen(new MainMenuScreen()));
      }

   }
}
