package ru.luminar.ui.comp.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.Iterator;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.settings.impl.ThemeSelectSetting;
import ru.luminar.ui.GuiScreen;
import ru.luminar.ui.comp.api.Component;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.themes.Theme;

public class ThemeRender extends Component {
   public ThemeSelectSetting option;
   float anim;
   float anim2 = 30.0F;
   boolean open;
   float targetWidth = 30.0F;
   private String lastValue;
   private float colorPanelAnim = 0.0F;
   private boolean colorPanelVisible = false;

   public ThemeRender(ThemeSelectSetting mode) {
      this.option = mode;
      this.setting = mode;
   }

   public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.lastValue = ((Theme)this.option.get()).getStyleName();
      this.anim = MathUtil.lerp(this.anim, this.open ? 1.0F : 0.0F, 10.0F);
      if (this.colorPanelVisible && this.open) {
         this.colorPanelAnim = MathUtil.lerp(this.colorPanelAnim, 20.0F, 10.0F);
      } else {
         this.colorPanelAnim = MathUtil.lerp(this.colorPanelAnim, 0.0F, 10.0F);
      }

      int textColor = ColorUtils.interpolate(-1, (new Color(117, 117, 117)).getRGB(), GuiScreen.whiteAnim);
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)(this.x + 5.0F), (double)(this.y + this.height / 2.0F - 6.0F + 3.0F - 3.0F), 70.0D, 10.0D);
      Fonts.sfbold.drawText(matrixStack, this.option.getName(), this.x + 5.0F + 2.0F, this.y + 5.0F, textColor, 6.0F, 0.05F);
      Scissor.unset();
      Scissor.pop();
      float itemHeight = Fonts.sfbold.getHeight(6.0F) + 2.0F + 2.0F;
      float totalHeight = itemHeight * (float)this.option.themes.size();
      float animProgress = this.anim * totalHeight;
      String currentValue = ((Theme)this.option.get()).getStyleName();
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
      Scissor.setFromComponentCoordinates((double)(this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F), (double)(this.y + 5.0F - 2.0F), (double)(this.anim2 + 5.0F - 2.0F), (double)(animProgress + Fonts.sfbold.getHeight(6.0F) + 3.0F));
      float textY = this.y + 5.0F + Fonts.sfbold.getHeight(6.0F) + 6.0F;

      for(Iterator var13 = this.option.themes.iterator(); var13.hasNext(); textY += 9.0F) {
         Theme s = (Theme)var13.next();
         Fonts.sfbold.drawCenteredText(matrixStack, s.getStyleName(), this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F + (this.anim2 + 5.0F - 2.0F) / 2.0F, (float)((int)textY), textColor2, 6.0F, 0.05F);
      }

      Scissor.unset();
      Scissor.pop();
      if (this.colorPanelAnim > 0.1F) {
         float panelX = this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F - this.colorPanelAnim - 2.0F;
         DisplayUtils.drawRoundedRect(panelX - 2.0F, this.y + 5.0F - 2.0F + 5.0F + 4.0F, this.colorPanelAnim, Fonts.sfbold.getHeight(6.0F) + 3.0F + animProgress - 10.0F, 3.0F, (new Color(40, 40, 40)).getRGB());
         float cubeSize = this.colorPanelAnim - 12.0F - 2.0F;
         float cubeY = this.y + 5.0F + 2.0F + 5.0F + 4.0F;

         for(Iterator var16 = this.option.themes.iterator(); var16.hasNext(); cubeY += 9.1F) {
            Theme theme = (Theme)var16.next();
            DisplayUtils.drawRoundedRect(panelX + 2.0F, cubeY, cubeSize, 6.0F, 0.0F, theme.getFirstColor().getRGB());
            DisplayUtils.drawRoundedRect(panelX + 2.0F + cubeSize, cubeY, cubeSize, 6.0F, 0.0F, theme.getSecondColor().getRGB());
         }
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F, this.y + 5.0F - 2.0F, this.anim2 + 5.0F - 2.0F, Fonts.sfbold.getHeight(6.0F) + 2.0F) && mouseButton == 0) {
         this.open = !this.open;
      } else if (this.open) {
         if (!MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F, this.y + 5.0F - 2.0F, this.anim2 + 5.0F - 2.0F, Fonts.sfbold.getHeight(6.0F) + 2.0F)) {
            this.open = false;
         }

         int i = 1;

         for(Iterator var5 = this.option.themes.iterator(); var5.hasNext(); i += 9) {
            Theme s = (Theme)var5.next();
            if (MathUtil.isInRegion((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F, this.y + 5.0F + 8.0F + (float)i, this.anim2 + 5.0F - 2.0F, 8.0F)) {
               this.option.setTheme(s);
               Luminar.instance.styleManager.setCurrentStyle(s);
               this.lastValue = s.getStyleName();
               this.open = false;
            }
         }

         float itemHeight = Fonts.sfbold.getHeight(6.0F) + 2.0F + 2.0F;
         float totalHeight = itemHeight * (float)this.option.themes.size();
         float animProgress = this.anim * totalHeight;
         if (mouseButton == 1 && MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - this.anim2 - 2.0F - 3.0F + 2.0F, this.y + 5.0F - 2.0F, this.anim2 + 5.0F - 2.0F, Fonts.sfbold.getHeight(6.0F) + 3.0F + animProgress)) {
            this.colorPanelVisible = !this.colorPanelVisible;
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
      return this.open;
   }
}
