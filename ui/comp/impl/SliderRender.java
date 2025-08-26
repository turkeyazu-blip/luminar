package ru.luminar.ui.comp.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.ui.GuiScreen;
import ru.luminar.ui.comp.api.Component;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.themes.Theme;

public class SliderRender extends Component {
   public SliderSetting option;
   private float anim;
   private boolean drag;

   public SliderRender(SliderSetting sliderSetting) {
      this.option = sliderSetting;
      this.setting = sliderSetting;
   }

   public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
      int textColor = ColorUtils.interpolate(-1, (new Color(117, 117, 117)).getRGB(), GuiScreen.whiteAnim);
      Fonts.sfbold.drawText(matrixStack, this.option.getName(), this.x + 5.0F + 2.0F, this.y + 5.0F, textColor, 6.0F, 0.05F);
      int rclColor = ColorUtils.interpolate(Color.darkGray.brighter().getRGB(), (new Color(237, 238, 235)).getRGB(), GuiScreen.whiteAnim);
      DisplayUtils.drawRoundedRect(this.x + this.width - Fonts.sfbold.getWidth(String.valueOf(this.option.get()), 6.0F) - 5.0F - 5.0F - 3.0F, this.y + 5.0F - 2.0F, Fonts.sfbold.getWidth(String.valueOf(this.option.get()), 6.0F) + 3.0F + 4.0F, 9.0F, 3.0F, rclColor);
      int textColor2 = ColorUtils.interpolate(-1, (new Color(123, 123, 123)).getRGB(), GuiScreen.whiteAnim);
      Fonts.sfbold.drawText(matrixStack, String.valueOf(this.option.get()), this.x + this.width - Fonts.sfbold.getWidth(String.valueOf(this.option.get()), 6.0F) - 5.0F - 5.0F, this.y + 5.0F, textColor2, 6.0F, 0.05F);
      this.height += 2.0F;
      float sliderWidth = ((Float)this.option.get() - this.option.min) / (this.option.max - this.option.min) * (this.width - 12.0F);
      this.anim = MathUtil.lerp(this.anim, sliderWidth, 10.0F);
      DisplayUtils.drawRoundedRect(this.x + 6.0F + 1.0F, this.y + 16.0F, this.width - 12.0F, 4.0F, 2.0F, Color.darkGray.brighter().getRGB());
      Theme style = Luminar.instance.styleManager.getCurrentStyle();
      DisplayUtils.drawRoundedRect(this.x + 6.0F, this.y + 16.0F, this.anim, 4.0F, 2.0F, style.getFirstColor().getRGB());
      DisplayUtils.drawCircle(this.x + 5.0F + 1.0F + this.anim, this.y + 16.0F + 2.0F, 7.5F, textColor);
      if (this.drag) {
         float draggingValue = (float)MathHelper.func_151237_a(MathUtil.round((double)(((float)mouseX - this.x - 4.0F) / (this.width - 12.0F) * (this.option.max - this.option.min) + this.option.min), (double)this.option.increment), (double)this.option.min, (double)this.option.max);
         this.option.set(draggingValue);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.isHovered(mouseX, mouseY, this.x + 5.0F, this.y + 14.0F, this.width - 12.0F, 8.0F)) {
         this.drag = true;
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
      this.drag = false;
   }

   public void keyTyped(int keyCode, int scanCode, int modifiers) {
   }

   public void charTyped(char codePoint, int modifiers) {
   }
}
