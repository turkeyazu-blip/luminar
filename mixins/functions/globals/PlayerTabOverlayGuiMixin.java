package ru.luminar.mixins.functions.globals;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.overlay.PlayerTabOverlayGui;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.utils.globalsapi.ClientAPI;
import ru.luminar.utils.draw.DisplayUtils;

@Mixin({PlayerTabOverlayGui.class})
public abstract class PlayerTabOverlayGuiMixin {
   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/FontRenderer;drawShadow(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/text/ITextComponent;FFI)I"
)
   )
   private int onDrawPlayerName(FontRenderer instance, MatrixStack p_243246_1_, ITextComponent p_243246_2_, float p_243246_3_, float p_243246_4_, int p_243246_5_) {
      float offsetX = p_243246_3_;
      if (Luminar.instance.functions.globals.isEnabled()) {
         String displayName = p_243246_2_.getString();
         Iterator var9 = ClientAPI.USERS.keySet().iterator();

         while(var9.hasNext()) {
            String user = (String)var9.next();
            if (ClientAPI.USERS.keySet().isEmpty()) {
               break;
            }

            if (displayName.contains(user)) {
               DisplayUtils.drawLogo(p_243246_3_ - 2.0F, p_243246_4_ - 3.5F, 14.0F, 14.0F, true);
               offsetX = p_243246_3_ + 12.0F;
               break;
            }
         }
      }

      instance.func_243246_a(p_243246_1_, p_243246_2_, offsetX, p_243246_4_, p_243246_5_);
      return p_243246_5_;
   }
}
