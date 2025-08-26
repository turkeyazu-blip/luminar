package ru.luminar.ui.comp;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.vector.Vector4f;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BindSetting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.feature.functions.settings.impl.ThemeSelectSetting;
import ru.luminar.ui.GuiScreen;
import ru.luminar.ui.comp.api.Component;
import ru.luminar.ui.comp.impl.BindRender;
import ru.luminar.ui.comp.impl.BooleanRender;
import ru.luminar.ui.comp.impl.ColorPickerRender;
import ru.luminar.ui.comp.impl.ModeRender;
import ru.luminar.ui.comp.impl.SliderRender;
import ru.luminar.ui.comp.impl.ThemeRender;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.client.KeyStorage;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.vectors.Vector4i;
import ru.luminar.utils.themes.Theme;

public class ModuleComponent extends Component {
   public Function function;
   public List<Component> components = new ArrayList();
   public static Function selectedFunction;
   private static final Color BACKGROUND_COLOR;
   private static final Color BORDER_COLOR;
   private static final Color BIND_BG_COLOR;
   public static final Color BIND_BORDER_COLOR;
   public float animationToggle;
   public static ModuleComponent binding;
   public boolean isRemoving;
   public float removalAnimation = 0.0F;
   public static float testX;
   public static float testY;
   public static float testW;
   public static float testH;

   public ModuleComponent(Function function) {
      this.function = function;
   }

   private void initComponents() {
      this.components.clear();
      if (selectedFunction != null && selectedFunction == this.function) {
         Iterator var1 = selectedFunction.getSettings().iterator();

         while(var1.hasNext()) {
            Setting<?> setting = (Setting)var1.next();
            if (setting instanceof SliderSetting) {
               SliderSetting slider = (SliderSetting)setting;
               this.components.add(new SliderRender(slider));
            }

            if (setting instanceof BooleanSetting) {
               BooleanSetting booleanSetting = (BooleanSetting)setting;
               this.components.add(new BooleanRender(booleanSetting));
            }

            if (setting instanceof ModeSetting) {
               ModeSetting modeSetting = (ModeSetting)setting;
               this.components.add(new ModeRender(modeSetting));
            }

            if (setting instanceof ColorSetting) {
               ColorSetting colorSetting = (ColorSetting)setting;
               this.components.add(new ColorPickerRender(colorSetting));
            }

            if (setting instanceof BindSetting) {
               BindSetting bind = (BindSetting)setting;
               this.components.add(new BindRender(bind));
            }

            if (setting instanceof ThemeSelectSetting) {
               ThemeSelectSetting theme = (ThemeSelectSetting)setting;
               this.components.add(new ThemeRender(theme));
            }
         }
      }

   }

   public void draw(MatrixStack matrixStack, int mouseX, int mouseY) {
      if (this.function.isPendingRemoval() && !this.isRemoving) {
         this.isRemoving = true;
      }

      if (this.isRemoving) {
         this.removalAnimation += 0.05F;
         if (this.removalAnimation >= 1.0F) {
            this.parent.objects.remove(this);
            this.isRemoving = false;
            return;
         }
      }

      Scissor.push();
      Scissor.setFromComponentCoordinates((double)testX, (double)(testY + 32.0F), (double)testW, (double)(testH - 32.0F));
      this.animationToggle = MathUtil.lerp(this.animationToggle, this.function.isEnabled() ? 1.0F : 0.0F, 10.0F);
      int outlineC = ColorUtils.interpolate(BORDER_COLOR.getRGB(), (new Color(240, 240, 240)).getRGB(), GuiScreen.whiteAnim);
      int bgC = ColorUtils.interpolate(BACKGROUND_COLOR.brighter().brighter().brighter().getRGB(), (new Color(254, 254, 254)).getRGB(), GuiScreen.whiteAnim);
      DisplayUtils.drawRoundedRect(this.x - 0.5F, this.y - 0.5F, this.width + 1.0F, this.height + 1.0F, new Vector4f(6.0F, 6.0F, 6.0F, 6.0F), outlineC);
      DisplayUtils.drawRoundedRect(this.x, this.y, this.width, this.height, new Vector4f(6.0F, 6.0F, 6.0F, 6.0F), bgC);
      Theme style = Luminar.instance.styleManager.getCurrentStyle();
      int textColor = ColorUtils.interpolate(-1, (new Color(94, 94, 94)).getRGB(), GuiScreen.whiteAnim);
      Fonts.sfbold.drawText(matrixStack, this.function.getName(), this.x + 7.5F, this.y + 9.0F, textColor, 8.0F, 0.05F);
      float width;
      if (binding == this) {
         String key = KeyStorage.getKey(this.function.bind <= 5 ? -100 + this.function.bind : this.function.bind);
         String bindText = "Бинд: " + key;
         width = Fonts.sfMedium.getWidth(bindText, 8.0F, 0.1F);
         DisplayUtils.drawRoundedRect(this.x + 5.0F + 2.5F - 0.5F + 0.25F + Fonts.sfbold.getWidth(this.function.getName(), 8.0F, 0.05F) + 5.0F, this.y + 4.5F + 1.0F - 0.5F, 10.0F + width - 3.0F, 15.0F, 2.0F, BIND_BG_COLOR.brighter().brighter().getRGB());
         Fonts.sfMedium.drawText(matrixStack, bindText, this.x + 5.0F + 2.5F - 0.5F + 5.0F + Fonts.sfbold.getWidth(this.function.getName(), 8.0F, 0.05F) + 5.0F, this.y + 8.5F + 1.0F - 0.5F, textColor, 8.0F);
      }

      int colorr = ColorUtils.interpolate(style.getFirstColor().getRGB(), BIND_BORDER_COLOR.brighter().getRGB(), this.animationToggle);
      DisplayUtils.drawRoundedRect(this.x + this.width - 5.0F - 21.0F, this.y + this.height / 2.0F - 6.0F, 21.0F, 12.0F, 5.0F, colorr);
      float animProgress = this.animationToggle * 8.5F;
      DisplayUtils.drawCircle2(this.x + this.width - 19.5F + animProgress, this.y + this.height / 2.0F, 0.0F, 360.0F, 3.0F, 6.0F, true, -1);
      Scissor.unset();
      Scissor.pop();
      if (selectedFunction == this.function && !this.components.isEmpty()) {
         width = 375.0F;
         float height = 300.0F;
         float x = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198107_o() / 2), width);
         float y = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198087_p() / 2), height);
         float offsetY = 5.0F;
         long visibleCount = this.components.stream().filter((componentx) -> {
            return componentx.setting != null && (Boolean)componentx.setting.visible.get();
         }).count();
         Vector4i darkVec = new Vector4i(Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB());
         Vector4i whiteVec = new Vector4i((new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB());
         Vector4i vec = ColorUtils.interpolate(darkVec, whiteVec, GuiScreen.whiteAnim);
         DisplayUtils.drawRoundedRect(x + width + 5.0F, y + height / 2.0F - (float)((visibleCount * 20L + 10L) / 2L), 100.0F, (float)(visibleCount * 20L + 10L), 8.0F, vec);
         Iterator var20 = this.components.iterator();

         while(true) {
            Component component;
            do {
               do {
                  if (!var20.hasNext()) {
                     offsetY = 5.0F;
                     var20 = this.components.iterator();

                     while(true) {
                        do {
                           do {
                              if (!var20.hasNext()) {
                                 return;
                              }

                              component = (Component)var20.next();
                           } while(component.setting == null);
                        } while(!(Boolean)component.setting.visible.get());

                        if (component instanceof ModeRender && ((ModeRender)component).isMenuOpen() || component instanceof ColorPickerRender && ((ColorPickerRender)component).isMenuOpen() || component instanceof ThemeRender && ((ThemeRender)component).isMenuOpen()) {
                           component.setPosition(x + width + 5.0F, y + height / 2.0F - (float)((visibleCount * 20L + 10L) / 2L) + offsetY, 100.0F, 20.0F);
                           component.draw(matrixStack, mouseX, mouseY);
                        }

                        offsetY += 20.0F;
                     }
                  }

                  component = (Component)var20.next();
               } while(component.setting == null);
            } while(!(Boolean)component.setting.visible.get());

            component.setPosition(x + width + 5.0F, y + height / 2.0F - (float)((visibleCount * 20L + 10L) / 2L) + offsetY, 100.0F, 20.0F);
            if ((!(component instanceof ModeRender) || !((ModeRender)component).isMenuOpen()) && (!(component instanceof ColorPickerRender) || !((ColorPickerRender)component).isMenuOpen()) && (!(component instanceof ThemeRender) || !((ThemeRender)component).isMenuOpen())) {
               component.draw(matrixStack, mouseX, mouseY);
            }

            offsetY += 20.0F;
         }
      }
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, testX, testY + 32.0F, testW, testH - 32.0F)) {
         if (MathUtil.isInRegion((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.height) && mouseButton == 0) {
            this.function.toggle();
         }

         if (MathUtil.isInRegion((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.height) && mouseButton == 1) {
            if (selectedFunction == this.function) {
               selectedFunction = null;
               this.components.clear();
            } else {
               selectedFunction = this.function;
               this.initComponents();
            }
         }

         if (MathUtil.isInRegion((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.height) && mouseButton == 2) {
            binding = this;
         }
      }

      if (binding == this && mouseButton > 2) {
         this.function.setBind(mouseButton);
         binding = null;
      }

      if (selectedFunction == this.function) {
         boolean isAnyMenuOpen = this.components.stream().anyMatch((c) -> {
            return c instanceof ModeRender && ((ModeRender)c).isMenuOpen();
         });
         this.components.forEach((c) -> {
            if (!isAnyMenuOpen || c instanceof ModeRender && ((ModeRender)c).isMenuOpen()) {
               c.mouseClicked(mouseX, mouseY, mouseButton);
            }

         });
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
      if (selectedFunction == this.function) {
         boolean isAnyMenuOpen = this.components.stream().anyMatch((c) -> {
            return c instanceof ModeRender && ((ModeRender)c).isMenuOpen();
         });
         this.components.forEach((c) -> {
            if (!isAnyMenuOpen || c instanceof ModeRender && ((ModeRender)c).isMenuOpen()) {
               c.mouseReleased(mouseX, mouseY, mouseButton);
            }

         });
      }

   }

   public void keyTyped(int keyCode, int scanCode, int modifiers) {
      if (binding == this) {
         if (keyCode != 261 && keyCode != 256) {
            this.function.setBind(keyCode);
         } else {
            this.function.setBind(-1);
         }

         binding = null;
      }

      if (selectedFunction == this.function) {
         this.components.forEach((c) -> {
            c.keyTyped(keyCode, scanCode, modifiers);
         });
      }

   }

   public void charTyped(char codePoint, int modifiers) {
      if (selectedFunction == this.function) {
         this.components.forEach((c) -> {
            c.charTyped(codePoint, modifiers);
         });
      }

   }

   static {
      BACKGROUND_COLOR = Color.darkGray.darker().darker().darker().darker();
      BORDER_COLOR = new Color(50, 55, 65);
      BIND_BG_COLOR = new Color(15, 15, 15);
      BIND_BORDER_COLOR = new Color(40, 45, 55);
   }
}
