package ru.luminar.ui.comp.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.ui.GuiScreen;
import ru.luminar.ui.comp.api.Component;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.vectors.Vector4i;

public class ColorPickerRender extends Component {
   ColorSetting option;
   private String hexInput = "";
   private boolean hexEditing = false;
   private int hexCursorPos = 0;
   private long lastCursorBlink;
   private boolean cursorVisible = false;
   private float[] hsb;
   private float alpha;
   float anim;
   boolean open;
   float openX;
   float openY;
   boolean dragPicker;
   boolean dragHue;
   boolean dragAlpha;
   boolean copyHovered;
   boolean pasteHovered;
   boolean hexHovered;

   public ColorPickerRender(ColorSetting setting) {
      this.setting = setting;
      this.option = setting;
      this.hsb = Color.RGBtoHSB(ColorUtils.IntColor.getRed((Integer)setting.get()), ColorUtils.IntColor.getGreen((Integer)setting.get()), ColorUtils.IntColor.getBlue((Integer)setting.get()), (float[])null);
      this.alpha = (float)ColorUtils.IntColor.getAlpha((Integer)setting.get()) / 255.0F;
   }

   public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.anim = MathUtil.lerp(this.anim, this.open ? 1.0F : 0.0F, 10.0F);
      if (this.open) {
         this.option.set(this.getCurrentColor());
      }

      if (this.openX + 115.0F > (float)Minecraft.func_71410_x().func_228018_at_().func_198107_o()) {
         this.openX = (float)(Minecraft.func_71410_x().func_228018_at_().func_198107_o() - 5 - 115);
      }

      float animedWidth = 115.0F * this.anim;
      float animedHeight = 90.0F * this.anim;
      int textColor = ColorUtils.interpolate(-1, (new Color(117, 117, 117)).getRGB(), GuiScreen.whiteAnim);
      Fonts.sfbold.drawText(matrixStack, this.option.getName(), this.x + 5.0F + 2.0F, this.y + 5.0F, textColor, 6.0F, 0.05F);
      DisplayUtils.drawRoundedRect(this.x + this.width - 5.0F - 20.0F, this.y + 2.0F, 20.0F, 10.0F, 2.0F, (Integer)this.option.get());
      Vector4i darkVec = new Vector4i(Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB());
      Vector4i whiteVec = new Vector4i((new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB());
      Vector4i vec = ColorUtils.interpolate(darkVec, whiteVec, GuiScreen.whiteAnim);
      DisplayUtils.drawRoundedRect(this.openX, this.openY, animedWidth, animedHeight, 6.0F, vec);
      Vector4i vecc = new Vector4i(Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.getHSBColor(this.hsb[0], 1.0F, 1.0F).getRGB(), Color.BLACK.getRGB());
      DisplayUtils.drawRoundedRect(this.openX + 5.0F, this.openY + 5.0F, animedWidth - 10.0F, animedHeight - 35.0F, 4.0F, vecc);
      if (this.open) {
         float x;
         float y;
         if (this.dragPicker) {
            float v1 = this.openX + 5.0F;
            x = MathHelper.func_76131_a((float)mouseX - v1 - 3.0F, 0.0F, animedWidth - 10.0F - 6.0F) / (animedWidth - 10.0F - 6.0F);
            v1 = this.openY + 5.0F;
            y = MathHelper.func_76131_a((float)mouseY - v1 - 3.0F, 0.0F, animedHeight - 35.0F - 6.0F) / (animedHeight - 35.0F - 6.0F);
            this.hsb[1] = x;
            this.hsb[2] = 1.0F - y;
         }

         x = this.openX + 5.0F + 3.0F + this.hsb[1] * (animedWidth - 10.0F - 6.0F);
         y = this.openY + 5.0F + 3.0F + (1.0F - this.hsb[2]) * (animedHeight - 35.0F - 6.0F);
         DisplayUtils.drawCircle(x, y, 6.0F, Color.DARK_GRAY.brighter().getRGB());
         DisplayUtils.drawCircle(x, y, 4.0F, Color.DARK_GRAY.brighter().brighter().brighter().brighter().getRGB());

         int currentColor;
         for(currentColor = 0; (float)currentColor < animedWidth - 10.0F - 5.0F; ++currentColor) {
            DisplayUtils.drawCircle(this.openX + 7.5F + 1.0F + (float)currentColor, this.openY + 5.0F + (animedHeight - 35.0F - 8.0F) + 12.5F, 3.0F, Color.HSBtoRGB((float)currentColor / (animedWidth - 10.0F - 5.0F), 1.0F, 1.0F));
         }

         DisplayUtils.drawCircle(this.openX + 7.5F - 2.0F + this.hsb[0] * (animedWidth - 10.0F), this.openY + 5.0F + (animedHeight - 35.0F - 8.0F) + 12.5F, 6.0F, Color.DARK_GRAY.brighter().getRGB());
         DisplayUtils.drawCircle(this.openX + 7.5F - 2.0F + this.hsb[0] * (animedWidth - 10.0F), this.openY + 5.0F + (animedHeight - 35.0F - 8.0F) + 12.5F, 4.0F, Color.DARK_GRAY.brighter().brighter().brighter().brighter().getRGB());
         if (this.dragHue) {
            float var10001 = this.openX + 7.0F;
            float var5 = ((float)mouseX - var10001) / (animedWidth - 10.0F - 5.0F);
            this.hsb[0] = MathHelper.func_76131_a(var5, 0.0F, 1.0F);
         }

         currentColor = this.getCurrentColor();
         String hex = this.hexEditing ? this.hexInput : String.format("#%06X", currentColor & 16777215);
         int rectColor = ColorUtils.interpolate(Color.darkGray.getRGB(), (new Color(237, 238, 235)).getRGB(), GuiScreen.whiteAnim);
         float hexW = Fonts.sfbold.getWidth("HEX: " + hex, 5.5F, 0.05F);
         this.hexHovered = MathUtil.isHovered((float)mouseX, (float)mouseY, this.openX + 5.0F, this.openY + animedHeight - 28.0F + 10.0F, hexW + 10.0F, 15.0F);
         DisplayUtils.drawRoundedRect(this.openX + 5.0F, this.openY + animedHeight - 28.0F + 10.0F, (hexW + 10.0F) * this.anim, 15.0F, 4.0F, rectColor);
         Fonts.sfbold.drawText(matrixStack, "HEX: " + hex, this.openX + 10.0F, this.openY + animedHeight - 23.0F + 10.0F, textColor, 5.5F, 0.05F);
         if (this.hexEditing && System.currentTimeMillis() - this.lastCursorBlink > 500L) {
            this.lastCursorBlink = System.currentTimeMillis();
            this.cursorVisible = !this.cursorVisible;
         }

         int hCl;
         if (this.hexEditing && this.hexInput.length() == 7 && this.hexInput.startsWith("#")) {
            try {
               hCl = Integer.parseInt(this.hexInput.substring(1), 16);
               this.updateHSBFromColor(-16777216 | hCl);
            } catch (NumberFormatException var21) {
            }
         }

         if (this.hexEditing && this.cursorVisible) {
            float cursorX = this.openX + 10.0F + Fonts.sfbold.getWidth("HEX: " + hex.substring(0, this.hexCursorPos), 5.5F, 0.05F);
            DisplayUtils.drawRoundedRect(cursorX, this.openY + animedHeight - 24.5F + 10.0F, 1.0F, 8.0F, 0.0F, textColor);
         }

         this.copyHovered = MathUtil.isHovered((float)mouseX, (float)mouseY, this.openX + animedWidth - 60.0F + 2.5F, this.openY + animedHeight - 28.0F + 10.0F, 25.0F, 15.0F);
         hCl = ColorUtils.interpolate((new Color(80, 80, 80)).getRGB(), (new Color(227, 228, 225)).getRGB(), GuiScreen.whiteAnim);
         int hvCl = this.copyHovered ? hCl : rectColor;
         DisplayUtils.drawRoundedRect(this.openX + animedWidth - 60.0F + 2.5F, this.openY + animedHeight - 28.0F + 10.0F, 25.0F, 15.0F, 3.0F, hvCl);
         Fonts.sfbold.drawCenteredText(matrixStack, "Copy", this.openX + animedWidth - 60.0F + 12.0F + 2.5F, this.openY + animedHeight - 23.5F + 10.0F, textColor, 6.0F, 0.05F);
         this.pasteHovered = MathUtil.isHovered((float)mouseX, (float)mouseY, this.openX + animedWidth - 30.0F, this.openY + animedHeight - 28.0F + 10.0F, 25.0F, 15.0F);
         int hvCl2 = this.pasteHovered ? hCl : rectColor;
         DisplayUtils.drawRoundedRect(this.openX + animedWidth - 30.0F, this.openY + animedHeight - 28.0F + 10.0F, 25.0F, 15.0F, 3.0F, hvCl2);
         Fonts.sfbold.drawCenteredText(matrixStack, "Paste", this.openX + animedWidth - 30.0F + 12.0F, this.openY + animedHeight - 23.5F + 10.0F, textColor, 6.0F, 0.05F);
      }

   }

   private int getCurrentColor() {
      int color;
      if (this.hexEditing && this.hexInput.length() == 7 && this.hexInput.startsWith("#")) {
         try {
            color = Integer.parseInt(this.hexInput.substring(1), 16);
            return -16777216 | color;
         } catch (NumberFormatException var2) {
         }
      }

      color = Color.getHSBColor(this.hsb[0], this.hsb[1], this.hsb[2]).getRGB();
      return MathHelper.func_76125_a((int)(this.alpha * 255.0F), 0, 255) << 24 | color & 16777215;
   }

   private void updateHSBFromColor(int color) {
      this.hsb = Color.RGBtoHSB(ColorUtils.IntColor.getRed(color), ColorUtils.IntColor.getGreen(color), ColorUtils.IntColor.getBlue(color), (float[])null);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - 20.0F, this.y + 2.0F, 20.0F, 10.0F)) {
         if (this.open) {
            return;
         }

         this.openX = (float)mouseX;
         this.openY = (float)mouseY;
         this.open = true;
      }

      this.anim = MathUtil.lerp(this.anim, this.open ? 1.0F : 0.0F, 10.0F);
      float animedWidth = 115.0F * this.anim;
      float animedHeight = 90.0F * this.anim;
      if (this.open) {
         if (!MathUtil.isHovered((float)mouseX, (float)mouseY, this.x + this.width - 5.0F - 20.0F, this.y + 2.0F, 20.0F, 10.0F) && !MathUtil.isHovered((float)mouseX, (float)mouseY, this.openX, this.openY, animedWidth, animedHeight)) {
            this.hexEditing = false;
            this.open = false;
         }

         if (MathUtil.isInRegion((float)mouseX, (float)mouseY, this.openX + 5.0F, this.openY + 5.0F, animedWidth - 10.0F, animedHeight - 35.0F)) {
            this.dragPicker = true;
         }

         if (MathUtil.isInRegion((float)mouseX, (float)mouseY, this.openX + 7.5F - 2.0F, this.openY + 5.0F + (animedHeight - 35.0F - 8.0F) + 12.5F - 3.0F - 1.0F, animedWidth - 10.0F - 5.0F + 4.0F, 5.0F)) {
            this.dragHue = true;
         }

         if (this.hexHovered && mouseButton == 0) {
            this.hexEditing = true;
            this.hexInput = String.format("#%06X", this.getCurrentColor() & 16777215);
            this.hexCursorPos = this.hexInput.length();
            this.lastCursorBlink = System.currentTimeMillis();
            this.cursorVisible = true;
         }

         String clipboard;
         if (this.copyHovered && mouseButton == 0) {
            clipboard = String.format("#%06X", this.getCurrentColor() & 16777215);
            Minecraft.func_71410_x().field_195559_v.func_197960_a(clipboard);
         }

         if (this.pasteHovered && mouseButton == 0) {
            try {
               clipboard = Minecraft.func_71410_x().field_195559_v.func_197965_a();
               if (clipboard.startsWith("#") && clipboard.length() == 7) {
                  this.hexInput = clipboard.toUpperCase();
                  this.hexCursorPos = this.hexInput.length();

                  try {
                     int color = Integer.parseInt(this.hexInput.substring(1), 16);
                     this.hsb = Color.RGBtoHSB(color >> 16 & 255, color >> 8 & 255, color & 255, (float[])null);
                  } catch (NumberFormatException var8) {
                  }
               }
            } catch (Exception var9) {
               var9.printStackTrace();
            }
         }
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
      this.dragPicker = false;
      this.dragAlpha = false;
      this.dragHue = false;
   }

   public void keyTyped(int keyCode, int scanCode, int modifiers) {
      if (this.hexEditing) {
         String var10001;
         switch(keyCode) {
         case 257:
         case 335:
            this.hexEditing = false;
            break;
         case 259:
            if (this.hexCursorPos > 0) {
               var10001 = this.hexInput.substring(0, this.hexCursorPos - 1);
               this.hexInput = var10001 + this.hexInput.substring(this.hexCursorPos);
               --this.hexCursorPos;
               if (this.hexInput.length() == 1) {
                  this.hexInput = "#";
               }
            }
            break;
         case 261:
            if (this.hexCursorPos < this.hexInput.length()) {
               var10001 = this.hexInput.substring(0, this.hexCursorPos);
               this.hexInput = var10001 + this.hexInput.substring(this.hexCursorPos + 1);
               if (this.hexInput.length() == 0) {
                  this.hexInput = "#";
               }
            }
            break;
         case 262:
            if (this.hexCursorPos < this.hexInput.length()) {
               ++this.hexCursorPos;
            }
            break;
         case 263:
            if (this.hexCursorPos > 0) {
               --this.hexCursorPos;
            }
            break;
         case 268:
            this.hexCursorPos = 1;
            break;
         case 269:
            this.hexCursorPos = this.hexInput.length();
         }

         this.lastCursorBlink = System.currentTimeMillis();
         this.cursorVisible = true;
      }
   }

   public void charTyped(char codePoint, int modifiers) {
      if (this.hexEditing) {
         if (this.hexInput.length() < 7 && (codePoint >= '0' && codePoint <= '9' || codePoint >= 'A' && codePoint <= 'F' || codePoint >= 'a' && codePoint <= 'f')) {
            if (this.hexInput.isEmpty()) {
               this.hexInput = "#";
            }

            String var10001 = this.hexInput.substring(0, this.hexCursorPos);
            this.hexInput = var10001 + Character.toUpperCase(codePoint) + this.hexInput.substring(this.hexCursorPos);
            ++this.hexCursorPos;
            if (this.hexInput.length() == 7) {
               try {
                  int color = Integer.parseInt(this.hexInput.substring(1), 16);
                  this.updateHSBFromColor(-16777216 | color);
               } catch (NumberFormatException var4) {
               }
            }
         }

      }
   }

   public boolean isMenuOpen() {
      float animedHeight = 100.0F * this.anim;
      return this.open || animedHeight > 0.0F;
   }
}
