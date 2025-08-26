package ru.luminar.ui.comp.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.ui.GuiScreen;
import ru.luminar.ui.comp.ModuleComponent;
import ru.luminar.ui.comp.api.Component;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.themes.Theme;

public class BooleanRender extends Component {
   BooleanSetting option;
   float anim;
   private float textScrollOffset = 0.0F;
   private int textScrollState = 0;
   private int scrollTimer = 0;

   public BooleanRender(BooleanSetting booleanSetting) {
      this.option = booleanSetting;
      this.setting = booleanSetting;
   }

   public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.anim = MathUtil.lerp(this.anim, (Boolean)this.option.get() ? 1.0F : 0.0F, 10.0F);
      float textWidth = Fonts.sfbold.getWidth(this.option.getName(), 6.0F, 0.05F);
      float textX = this.x + 5.0F + 2.0F;
      float textY = this.y + this.height / 2.0F - 6.0F + 3.0F;
      boolean isHovered = MathUtil.isHovered((float)mouseX, (float)mouseY, textX, textY - 3.0F, textWidth, 10.0F);
      float buttonX = this.x + this.width - 5.0F - 21.0F;
      if (isHovered && textX + textWidth > buttonX) {
         if (this.textScrollState == 0) {
            this.textScrollOffset = MathUtil.lerp(this.textScrollOffset, -(textWidth - (buttonX - textX) + 10.0F + 10.0F), 1.0F);
            if (textX + this.textScrollOffset + textWidth <= buttonX - 2.0F) {
               this.textScrollState = 1;
               this.scrollTimer = 100;
            }
         } else if (this.textScrollState == 2) {
            this.textScrollOffset = MathUtil.lerp(this.textScrollOffset, 0.0F, 2.0F);
            if (this.textScrollOffset >= -1.0F) {
               this.textScrollState = 3;
               this.scrollTimer = 60;
            }
         } else if (this.scrollTimer > 0) {
            --this.scrollTimer;
         } else {
            if (this.textScrollState == 1) {
               this.textScrollState = 2;
            }

            if (this.textScrollState == 3) {
               this.textScrollState = 0;
            }
         }
      } else {
         this.textScrollOffset = MathUtil.lerp(this.textScrollOffset, 0.0F, 2.0F);
         this.textScrollState = 0;
         this.scrollTimer = 0;
      }

      int textColor = ColorUtils.interpolate(-1, (new Color(117, 117, 117)).getRGB(), GuiScreen.whiteAnim);
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)(this.x + 5.0F), (double)(this.y + this.height / 2.0F - 6.0F + 3.0F - 3.0F), 70.0D, 10.0D);
      Fonts.sfbold.drawText(matrixStack, this.option.getName(), textX + this.textScrollOffset, textY, textColor, 6.0F, 0.05F);
      Scissor.unset();
      Scissor.pop();
      Theme style = Luminar.instance.styleManager.getCurrentStyle();
      int colorr = ColorUtils.interpolate(style.getFirstColor().getRGB(), ModuleComponent.BIND_BORDER_COLOR.brighter().getRGB(), this.anim);
      DisplayUtils.drawRoundedRect(buttonX, this.y + this.height / 2.0F - 6.0F, 21.0F, 12.0F, 5.0F, colorr);
      float animProgress = this.anim * 8.5F;
      DisplayUtils.drawCircle2(this.x + this.width - 19.5F + animProgress, this.y + this.height / 2.0F, 0.0F, 360.0F, 3.0F, 6.0F, true, -1);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.height)) {
         this.option.set(!(Boolean)this.option.get());
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
   }

   public void keyTyped(int keyCode, int scanCode, int modifiers) {
   }

   public void charTyped(char codePoint, int modifiers) {
   }
}
