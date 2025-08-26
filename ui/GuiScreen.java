package ru.luminar.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.ui.comp.ModuleComponent;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.vectors.Vector4i;
import ru.luminar.utils.player.PlayerUtils;
import ru.luminar.utils.themes.Theme;

public class GuiScreen extends Screen {
   Category category;
   public float animationToggle;
   public static boolean whiteTheme;
   public static float whiteAnim;
   public static float whiteAnim2;
   private float scrollY;
   private float targetScrollY;
   private boolean isScrolling;
   private float scrollbarHeight;
   private float scrollbarY;
   private float maxScroll;
   public static boolean locked;
   public ArrayList<ModuleComponent> objects;

   public GuiScreen(ITextComponent p_i51108_1_) {
      super(p_i51108_1_);
      this.category = Category.Render;
      this.objects = new ArrayList();
      Iterator var2 = Luminar.instance.functions.functions.iterator();

      while(var2.hasNext()) {
         Function function = (Function)var2.next();
         this.objects.add(new ModuleComponent(function));
      }

   }

   public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
      float width = 375.0F;
      float height = 300.0F;
      float x = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198107_o() / 2), width);
      float y = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198087_p() / 2), height);
      this.func_230446_a_(matrixStack);
      Vector4i darkVec = new Vector4i(Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB());
      Vector4i whiteVec = new Vector4i((new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB());
      Vector4i vec = ColorUtils.interpolate(darkVec, whiteVec, whiteAnim);
      DisplayUtils.drawRoundedRect(x, y, width, height, 8.0F, vec);
      this.categories(matrixStack);
      ModuleComponent.testX = x;
      ModuleComponent.testY = y;
      ModuleComponent.testW = width;
      ModuleComponent.testH = height;
      this.components(matrixStack, mouseX, mouseY, partialTicks);
      int r = Color.darkGray.darker().darker().brighter().darker().getRed();
      int g = Color.darkGray.darker().darker().brighter().darker().getGreen();
      int b = Color.darkGray.darker().darker().brighter().darker().getBlue();
      int drkClr = ColorUtils.rgb(r, g, b);
      int r1 = (new Color(247, 247, 247)).getRed();
      int g2 = (new Color(247, 247, 247)).getGreen();
      int b2 = (new Color(247, 247, 247)).getBlue();
      int wtClr = ColorUtils.rgb(r1, g2, b2);
      int clr = ColorUtils.interpolate(drkClr, wtClr, whiteAnim);
      Color nahuiaYaTakMnogoDelayu = new Color(clr);
      int rgb1 = ColorUtils.rgba(nahuiaYaTakMnogoDelayu, 255);
      int rgb2 = ColorUtils.rgba(nahuiaYaTakMnogoDelayu, 0);
      DisplayUtils.drawRectHorizontalW((double)(x + 10.0F), (double)(y + height - 10.0F), (double)(width - 20.0F), 10.0D, rgb1, rgb2);
      DisplayUtils.drawRectHorizontalW((double)(x + 10.0F), (double)(y + 30.0F), (double)(width - 20.0F), 10.0D, rgb2, rgb1);
      if (this.maxScroll > 0.0F) {
         int scrollBgColor = ColorUtils.interpolate((new Color(50, 50, 50)).getRGB(), (new Color(220, 220, 220)).getRGB(), whiteAnim);
         DisplayUtils.drawRoundedRect(x + width - 5.0F, y + 32.0F, 3.0F, height - 32.0F - 10.0F, 1.5F, scrollBgColor);
         int scrollColor = ColorUtils.interpolate((new Color(100, 100, 100)).getRGB(), (new Color(150, 150, 150)).getRGB(), whiteAnim);
         DisplayUtils.drawRoundedRect(x + width - 5.0F, y + 32.0F + this.scrollbarY, 3.0F, this.scrollbarHeight, 1.5F, scrollColor);
      }

      float themeY = (y - 5.0F - 32.0F) * whiteAnim * (float)IMinecraft.mc.func_228018_at_().func_198100_s() / 2.0F;
      float themeY2 = (y - 5.0F - 32.0F) * whiteAnim2 * (float)IMinecraft.mc.func_228018_at_().func_198100_s() / 2.0F;
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)(x - 5.0F - 32.0F - 5.0F), (double)(y - 5.0F - 32.0F - 5.0F), 42.0D, 42.0D);
      DisplayUtils.drawImage(new ResourceLocation("luminar/images/moon.png"), x - 5.0F - 32.0F, themeY, 32.0F, 32.0F, -1);
      DisplayUtils.drawImage(new ResourceLocation("luminar/images/sun.png"), x - 5.0F - 32.0F, themeY2, 32.0F, 32.0F, -1);
      Scissor.unset();
      Scissor.pop();
      this.animationToggle = MathUtil.lerp(this.animationToggle, Luminar.useCloudCfg ? 1.0F : 0.0F, 10.0F);
      Theme style = Luminar.instance.styleManager.getCurrentStyle();
      int colorr = ColorUtils.interpolate(style.getFirstColor().getRGB(), ModuleComponent.BIND_BORDER_COLOR.brighter().getRGB(), this.animationToggle);
      int textColor = ColorUtils.interpolate(-1, (new Color(94, 94, 94)).getRGB(), whiteAnim);
      Fonts.sfbold.drawText(matrixStack, "Клауд кфг", x + width - 5.0F - 21.0F - Fonts.sfbold.getWidth("Клауд кфг", 7.0F), y + 5.0F - 6.0F + 5.0F + 5.0F, textColor, 7.0F);
      DisplayUtils.drawRoundedRect(x + width - 5.0F - 21.0F, y + 5.0F - 6.0F + 5.0F + 2.5F, 21.0F, 12.0F, 5.0F, colorr);
      float animProgress = this.animationToggle * 8.5F;
      DisplayUtils.drawCircle2(x + width - 19.5F + animProgress, y + 5.0F + 5.0F + 2.5F, 0.0F, 360.0F, 3.0F, 6.0F, true, -1);
      DisplayUtils.drawCircle2(x + width - 19.5F - 15.0F - Fonts.sfbold.getWidth("Клауд кфг", 7.0F), y + 5.0F + 5.0F + 2.5F, 0.0F, 360.0F, 3.0F, 6.0F, true, locked ? style.getFirstColor().getRGB() : ModuleComponent.BIND_BORDER_COLOR.brighter().getRGB());
   }

   public void restoreFunctions() {
      List<Function> currentFunctions = Luminar.instance.functions.functions;
      Iterator var2 = currentFunctions.iterator();

      while(var2.hasNext()) {
         Function function = (Function)var2.next();
         boolean existsInGui = this.objects.stream().anyMatch((m) -> {
            return m.function == function;
         });
         if (!existsInGui && !function.isPendingRemoval()) {
            this.objects.add(new ModuleComponent(function));
         }
      }

      this.objects.forEach((m) -> {
         if (m.function.isPendingRemoval()) {
            m.function.setPendingRemoval(false);
            m.isRemoving = false;
            m.removalAnimation = 0.0F;
         }

      });
   }

   private void components(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      float width = 375.0F;
      float height = 300.0F;
      float x = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198107_o() / 2), width);
      float y = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198087_p() / 2), height);
      this.objects.removeIf((m) -> {
         return m.isRemoving;
      });
      List<ModuleComponent> filtered = this.objects.stream().filter((m) -> {
         return !m.isRemoving;
      }).filter((moduleObject) -> {
         return moduleObject.function.getCategory() == this.category;
      }).sorted(Comparator.comparing((m) -> {
         return m.function.getName();
      })).toList();
      List<ModuleComponent> first = new ArrayList();
      List<ModuleComponent> second = new ArrayList();
      Map<Character, Boolean> letterDistribution = new HashMap();

      char firstLetter;
      boolean addToFirst;
      for(Iterator var13 = filtered.iterator(); var13.hasNext(); letterDistribution.put(firstLetter, !addToFirst)) {
         ModuleComponent module = (ModuleComponent)var13.next();
         firstLetter = Character.toUpperCase(module.function.getName().charAt(0));
         addToFirst = (Boolean)letterDistribution.computeIfAbsent(firstLetter, (k) -> {
            return first.size() <= second.size();
         });
         if (addToFirst) {
            first.add(module);
         } else {
            second.add(module);
         }
      }

      float leftPadding = 20.0F;
      float columnWidth = (width - leftPadding * 2.0F - 10.0F) / 2.0F;
      float contentHeight = 0.0F;

      for(Iterator var24 = first.iterator(); var24.hasNext(); contentHeight += 33.0F) {
         ModuleComponent ignored = (ModuleComponent)var24.next();
      }

      float viewportHeight = height - 32.0F - 10.0F;
      this.maxScroll = Math.max(0.0F, contentHeight - viewportHeight);
      this.scrollY = MathUtil.lerp(this.scrollY, this.targetScrollY, 10.0F);
      this.targetScrollY = Math.max(0.0F, Math.min(this.maxScroll, this.targetScrollY));
      if (this.maxScroll > 0.0F) {
         this.scrollbarHeight = viewportHeight * viewportHeight / contentHeight;
         this.scrollbarHeight = Math.max(10.0F, this.scrollbarHeight);
         this.scrollbarY = this.scrollY / this.maxScroll * (viewportHeight - this.scrollbarHeight);
      }

      float offset1 = y + 32.0F + 12.0F - this.scrollY;
      Iterator var18 = first.iterator();

      while(var18.hasNext()) {
         ModuleComponent component = (ModuleComponent)var18.next();
         if (component.function.getCategory() == this.category) {
            component.parent = this;
            component.setPosition(x + leftPadding, offset1, columnWidth, 25.0F);
            component.draw(matrixStack, mouseX, mouseY);
            offset1 += 33.0F;
         }
      }

      float offset2 = y + 32.0F + 12.0F - this.scrollY;
      Iterator var28 = second.iterator();

      while(var28.hasNext()) {
         ModuleComponent component = (ModuleComponent)var28.next();
         if (component.function.getCategory() == this.category) {
            component.parent = this;
            component.setPosition(x + leftPadding + columnWidth + 10.0F, offset2, columnWidth, 25.0F);
            component.draw(matrixStack, mouseX, mouseY);
            offset2 += 33.0F;
         }
      }

   }

   private void categories(MatrixStack matrixStack) {
      float width = 375.0F;
      float height = 300.0F;
      float x = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198107_o() / 2), width);
      float y = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198087_p() / 2), height);
      float categoryButtonWidth = 40.0F;
      float categoryButtonHeight = 15.0F;
      float categorySpacing = 5.0F;
      float totalWidth = (float)Category.values().length * categoryButtonWidth + (float)(Category.values().length - 1) * categorySpacing;
      float startX = x + width / 2.0F - totalWidth / 2.0F;
      float categoryY = y + 10.0F;
      Category[] var12 = Category.values();
      int var13 = var12.length;

      for(int var14 = 0; var14 < var13; ++var14) {
         Category category = var12[var14];
         int rectColorD = this.category == category ? Color.darkGray.getRGB() : Color.darkGray.darker().darker().darker().getRGB();
         int textColorD = this.category == category ? Color.white.getRGB() : ColorUtils.rgb(220, 220, 220);
         int rectColorW = this.category == category ? (new Color(227, 227, 227)).getRGB() : (new Color(239, 239, 239)).getRGB();
         int textColorW = this.category == category ? (new Color(105, 105, 105)).getRGB() : (new Color(185, 185, 185)).getRGB();
         int rectColor = ColorUtils.interpolate(rectColorD, rectColorW, whiteAnim);
         int textColor = ColorUtils.interpolate(textColorD, textColorW, whiteAnim);
         DisplayUtils.drawRoundedRect(startX, categoryY, categoryButtonWidth, categoryButtonHeight, 3.0F, rectColor);
         float textX = startX + (categoryButtonWidth - Fonts.poppinsSb.getWidth(category.name(), 8.0F)) / 2.0F;
         float textY = categoryY + (categoryButtonHeight - Fonts.poppinsSb.getHeight(8.0F)) / 3.0F;
         Fonts.poppinsSb.drawText(matrixStack, category.name(), textX, textY, textColor, 8.0F);
         startX += categoryButtonWidth + categorySpacing;
      }

   }

   public boolean func_231044_a_(double mouseX, double mouseY, int button) {
      float width = 375.0F;
      float height = 300.0F;
      float x = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198107_o() / 2), width);
      float y = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198087_p() / 2), height);
      float categoryButtonWidth = 40.0F;
      float categoryButtonHeight = 15.0F;
      float categorySpacing = 5.0F;
      float totalWidth = (float)Category.values().length * categoryButtonWidth + (float)(Category.values().length - 1) * categorySpacing;
      float startX = x + width / 2.0F - totalWidth / 2.0F;
      float categoryY = y + 10.0F;
      Category[] var16 = Category.values();
      int var17 = var16.length;

      for(int var18 = 0; var18 < var17; ++var18) {
         Category category = var16[var18];
         if (MathUtil.isHovered((float)mouseX, (float)mouseY, startX, categoryY, categoryButtonWidth, categoryButtonHeight)) {
            this.category = category;
            this.scrollY = 0.0F;
            this.targetScrollY = 0.0F;
         }

         startX += categoryButtonWidth + categorySpacing;
      }

      Iterator var20 = this.objects.iterator();

      while(var20.hasNext()) {
         ModuleComponent m = (ModuleComponent)var20.next();
         if (m.function.getCategory() == this.category) {
            m.mouseClicked((int)mouseX, (int)mouseY, button);
         }
      }

      if (MathUtil.isInRegion((float)mouseX, (float)mouseY, x + width - 5.0F - 21.0F - Fonts.sfbold.getWidth("Клауд кфг", 7.0F), y + 5.0F - 6.0F + 5.0F + 5.0F, Fonts.sfbold.getWidth("Клауд кфг", 7.0F) + 21.0F + 3.0F, 12.0F)) {
         Luminar.useCloudCfg = !Luminar.useCloudCfg;
      }

      float themeY = (y - 5.0F - 32.0F) * whiteAnim;
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, x - 5.0F - 32.0F, y - 5.0F - 32.0F, 32.0F, 32.0F)) {
         whiteTheme = !whiteTheme;
      }

      if (MathUtil.isHovered((float)mouseX, (float)mouseY, x + width - 19.5F - 15.0F - Fonts.sfbold.getWidth("Клауд кфг", 7.0F) - 5.0F, y + 5.0F + 5.0F + 2.5F - 5.0F, 10.0F, 10.0F)) {
         if (PlayerUtils.connectedTo("funtime")) {
            locked = !locked;
         } else if (Luminar.instance.functions.island.isEnabled()) {
            Island.islandManager.add("Это действие можно сделать только на FunTime", 3);
         } else {
            IMinecraft.print2("Это действие можно сделать только на FunTime");
         }
      }

      if (this.maxScroll > 0.0F && MathUtil.isHovered((float)mouseX, (float)mouseY, x + width - 5.0F, y + 32.0F + this.scrollbarY, 3.0F, this.scrollbarHeight)) {
         this.isScrolling = true;
      }

      return super.func_231044_a_(mouseX, mouseY, button);
   }

   public boolean func_231048_c_(double mouseX, double mouseY, int button) {
      Iterator var6 = this.objects.iterator();

      while(var6.hasNext()) {
         ModuleComponent m = (ModuleComponent)var6.next();
         if (m.function.getCategory() == this.category) {
            m.mouseReleased((int)mouseX, (int)mouseY, button);
         }
      }

      this.isScrolling = false;
      return super.func_231048_c_(mouseX, mouseY, button);
   }

   public boolean func_231043_a_(double mouseX, double mouseY, double delta) {
      float width = 375.0F;
      float height = 300.0F;
      float x = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198107_o() / 2), width);
      float y = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198087_p() / 2), height);
      if (MathUtil.isHovered((float)mouseX, (float)mouseY, x, y, width, height)) {
         this.targetScrollY = (float)((double)this.targetScrollY + delta * -10.0D);
      }

      return super.func_231043_a_(mouseX, mouseY, delta);
   }

   public boolean func_231045_a_(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.isScrolling) {
         float height = 300.0F;
         float y = MathUtil.calculatePosition((float)(IMinecraft.mc.func_228018_at_().func_198087_p() / 2), height);
         float viewportHeight = height - 32.0F;
         float newScrollbarY = (float)(mouseY - (double)y - 32.0D - (double)(this.scrollbarHeight / 2.0F));
         newScrollbarY = Math.max(0.0F, Math.min(viewportHeight - this.scrollbarHeight, newScrollbarY));
         this.targetScrollY = newScrollbarY / (viewportHeight - this.scrollbarHeight) * this.maxScroll;
         return true;
      } else {
         return super.func_231045_a_(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   public boolean func_231046_a_(int keyCode, int scanCode, int modifiers) {
      Iterator var4 = this.objects.iterator();

      while(var4.hasNext()) {
         ModuleComponent m = (ModuleComponent)var4.next();
         if (m.function.getCategory() == this.category) {
            m.keyTyped(keyCode, scanCode, modifiers);
         }
      }

      return super.func_231046_a_(keyCode, scanCode, modifiers);
   }

   public boolean func_231042_a_(char codePoint, int modifiers) {
      Iterator var3 = this.objects.iterator();

      while(var3.hasNext()) {
         ModuleComponent m = (ModuleComponent)var3.next();
         if (m.function.getCategory() == this.category) {
            m.charTyped(codePoint, modifiers);
         }
      }

      return super.func_231042_a_(codePoint, modifiers);
   }
}
