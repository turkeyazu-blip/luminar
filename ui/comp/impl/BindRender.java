package ru.luminar.ui.comp.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import ru.luminar.feature.functions.settings.impl.BindSetting;
import ru.luminar.ui.GuiScreen;
import ru.luminar.ui.comp.api.Component;
import ru.luminar.utils.client.KeyStorage;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;

public class BindRender extends Component {
   public BindSetting option;
   private float textScrollOffset = 0.0F;
   private int textScrollState = 0;
   private int scrollTimer = 0;
   float anim;
   boolean binding;

   public BindRender(BindSetting setting) {
      this.option = setting;
      this.setting = setting;
   }

   public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
      int textColor = ColorUtils.interpolate(-1, (new Color(117, 117, 117)).getRGB(), GuiScreen.whiteAnim);
      Fonts.sfbold.drawText(matrixStack, this.option.getName(), this.x + 5.0F + 2.0F + this.textScrollOffset, this.y + 5.0F, textColor, 6.0F, 0.05F);
      float nameWidth = Fonts.sfbold.getWidth(this.option.getName(), 6.0F, 0.05F);
      float nameX = this.x + 5.0F + 2.0F;
      float nameY = this.y + 5.0F;
      boolean isHovered = MathUtil.isHovered((float)mouseX, (float)mouseY, nameX, nameY - 3.0F, nameWidth, Fonts.sfbold.getHeight(6.0F));
      float bindButtonX = this.x + this.width - 5.0F - this.anim;
      if (isHovered && nameX + nameWidth > bindButtonX) {
         if (this.textScrollState == 0) {
            this.textScrollOffset = MathUtil.lerp(this.textScrollOffset, -(nameWidth - (bindButtonX - nameX) + 10.0F + 10.0F), 1.0F);
            if (nameX + this.textScrollOffset + nameWidth <= bindButtonX - 2.0F) {
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

      String bind = KeyStorage.getKey((Integer)this.option.get());
      String key = this.binding ? "Press key.." : (bind.isEmpty() ? "-" : bind);
      float textWidth = Fonts.sfbold.getWidth(key, 6.0F, 0.05F);
      float targetWidth = Math.max(20.0F, textWidth + 8.0F);
      this.anim = MathUtil.lerp(this.anim, targetWidth, 10.0F);
      float animWidth = this.anim;
      DisplayUtils.drawRoundedRect(this.x + this.width - 5.0F - animWidth, this.y + 5.0F - 1.0F - 2.0F, animWidth, 12.0F, 3.0F, GuiScreen.whiteTheme ? (new Color(237, 238, 235)).getRGB() : Color.darkGray.getRGB());
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)(this.x + this.width - 5.0F - animWidth), (double)(this.y + 5.0F - 1.0F - 2.0F), (double)animWidth, 12.0D);
      Fonts.sfbold.drawText(matrixStack, key, this.x + this.width - 5.0F - animWidth + (animWidth - textWidth) / 2.0F, this.y + 5.0F - 1.0F - 2.0F + 2.0F + 1.0F, GuiScreen.whiteTheme ? (new Color(123, 123, 123)).getRGB() : -1, 6.0F, 0.05F);
      Scissor.unset();
      Scissor.pop();
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      String bind = KeyStorage.getKey((Integer)this.option.get());
      String key = bind.isEmpty() ? "-" : bind;
      float textWidth = Fonts.sfbold.getWidth(key, 6.0F, 0.05F);
      int rectWidth = Math.max(20, (int)textWidth + 8);
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - (float)rectWidth, this.y + 5.0F - 1.7777778F - 2.0F, (float)rectWidth, 12.0F)) {
         this.binding = true;
      }

      if (this.binding && mouseButton > 2) {
         this.option.set(mouseButton);
         this.binding = false;
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
   }

   public void keyTyped(int keyCode, int scanCode, int modifiers) {
      if (this.binding) {
         if (keyCode != 256 && keyCode != 261) {
            this.option.set(keyCode);
            this.binding = false;
         } else {
            this.option.set(-1);
            this.binding = false;
         }
      }

   }

   public void charTyped(char codePoint, int modifiers) {
   }
}
