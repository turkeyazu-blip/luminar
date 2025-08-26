package ru.luminar.ui.comp.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.ui.GuiScreen;
import ru.luminar.ui.comp.api.Component;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;

public class ModeRender extends Component {
   public ModeSetting option;
   float anim;
   float anim2 = 30.0F;
   boolean open;
   float targetWidth = 30.0F;
   private String lastValue;
   private float textScrollOffset = 0.0F;
   private int textScrollState = 0;
   private int scrollTimer = 0;

   public ModeRender(ModeSetting mode) {
      this.option = mode;
      this.setting = mode;
   }

   public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.lastValue = (String)this.option.get();
      this.anim = MathUtil.lerp(this.anim, this.open ? 1.0F : 0.0F, 10.0F);
      int textColor = ColorUtils.interpolate(-1, (new Color(117, 117, 117)).getRGB(), GuiScreen.whiteAnim);
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)(this.x + 5.0F), (double)(this.y + this.height / 2.0F - 6.0F + 3.0F - 3.0F), 70.0D, 10.0D);
      Fonts.sfbold.drawText(matrixStack, this.option.getName(), this.x + 5.0F + 2.0F + this.textScrollOffset, this.y + 5.0F, textColor, 6.0F, 0.05F);
      Scissor.unset();
      Scissor.pop();
      float itemHeight = Fonts.sfbold.getHeight(6.0F) + 4.0F;
      float totalHeight = itemHeight * (float)this.option.strings.length + 1.0F;
      float animProgress = this.anim * totalHeight;
      float nameWidth = Fonts.sfbold.getWidth(this.option.getName(), 6.0F, 0.05F);
      float nameX = this.x + 5.0F + 2.0F;
      float nameY = this.y + 5.0F;
      boolean isHovered = MathUtil.isHovered((float)mouseX, (float)mouseY, nameX, nameY - 3.0F, nameWidth, Fonts.sfbold.getHeight(6.0F));
      float dropdownX = this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F;
      if (isHovered && nameX + nameWidth > dropdownX) {
         if (this.textScrollState == 0) {
            this.textScrollOffset = MathUtil.lerp(this.textScrollOffset, -(nameWidth - (dropdownX - nameX) + 10.0F + 10.0F), 1.0F);
            if (nameX + this.textScrollOffset + nameWidth <= dropdownX - 2.0F) {
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

      String currentValue = (String)this.option.get();
      float textWidth;
      if (currentValue.equals(this.lastValue)) {
         textWidth = Fonts.sfbold.getWidth(currentValue, 6.0F, 0.05F);
         this.targetWidth = Math.max(30.0F, textWidth + 5.0F);
         this.lastValue = currentValue;
      }

      this.anim2 = MathUtil.lerp(this.anim2, this.targetWidth, 10.0F);
      textWidth = Fonts.sfbold.getWidth(currentValue, 6.0F, 0.05F);
      int rectColor = ColorUtils.interpolate(Color.darkGray.getRGB(), (new Color(237, 238, 235)).getRGB(), GuiScreen.whiteAnim);
      DisplayUtils.drawRoundedRect(this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F, this.y + 5.0F - 2.0F, this.anim2 + 5.0F - 2.0F, animProgress + Fonts.sfbold.getHeight(6.0F) + 3.0F, 3.0F, rectColor);
      int textColor2 = ColorUtils.interpolate(-1, (new Color(123, 123, 123)).getRGB(), GuiScreen.whiteAnim);
      Fonts.sfbold.drawText(matrixStack, currentValue, this.x + this.width - 5.0F - this.anim2 - 2.0F + (this.anim2 - textWidth) / 2.0F, this.y + 5.0F, textColor2, 6.0F, 0.05F);
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)(this.x + this.width - 5.0F - 30.0F - 2.0F - 3.0F), (double)(this.y + 5.0F - 2.0F), 35.0D, (double)(animProgress + Fonts.sfbold.getHeight(6.0F) + 3.0F));
      float textY = this.y + 5.0F + Fonts.sfbold.getHeight(6.0F) + 6.0F;
      String[] var18 = this.option.strings;
      int var19 = var18.length;

      for(int var20 = 0; var20 < var19; ++var20) {
         String s = var18[var20];
         Fonts.sfbold.drawCenteredText(matrixStack, s, this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F + (this.anim2 + 5.0F - 2.0F) / 2.0F, (float)((int)textY), textColor2, 6.0F, 0.05F);
         textY += 9.0F;
      }

      Scissor.unset();
      Scissor.pop();
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - 30.0F - 2.0F - 3.0F, this.y + 5.0F - 2.0F, 35.0F, Fonts.sfbold.getHeight(6.0F) + 2.0F)) {
         this.open = !this.open;
      } else if (this.open) {
         if (!MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - 30.0F - 2.0F - 3.0F, this.y + 5.0F - 2.0F, 35.0F, Fonts.sfbold.getHeight(6.0F) + 2.0F)) {
            this.open = false;
         }

         int i = 1;
         String[] var5 = this.option.strings;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];
            if (MathUtil.isInRegion((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - 30.0F - 2.0F - 3.0F, this.y + 5.0F + 8.0F + (float)i, 35.0F, 8.0F)) {
               this.option.setMode(s);
               this.lastValue = s;
               this.open = false;
            }

            i += 9;
         }

      }
   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
   }

   public void keyTyped(int keyCode, int scanCode, int modifiers) {
   }

   public void charTyped(char codePoint, int modifiers) {
   }

   public boolean isMenuOpen() {
      float itemHeight = Fonts.sfbold.getHeight(6.0F) + 4.0F;
      float totalHeight = itemHeight * (float)this.option.strings.length + 1.0F;
      float animProgress = this.anim * totalHeight;
      float allh = animProgress + Fonts.sfbold.getHeight(6.0F) + 3.0F;
      return this.open || allh > Fonts.sfbold.getHeight(6.0F) + 3.0F;
   }
}
