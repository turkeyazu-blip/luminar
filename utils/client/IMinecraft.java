package ru.luminar.utils.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import ru.luminar.Luminar;
import ru.luminar.utils.draw.ColorUtils;

public interface IMinecraft {
   Minecraft mc = Minecraft.func_71410_x();
   Tessellator tessellator = Tessellator.func_178181_a();
   BufferBuilder buffer = tessellator.func_178180_c();

   default void print(String input) {
      if (mc.field_71439_g != null) {
         ITextComponent text = gradient("Luminar", Luminar.instance.styleManager.getCurrentStyle().getColor(0), Luminar.instance.styleManager.getCurrentStyle().getColor(90), true).func_230529_a_(new StringTextComponent(TextFormatting.DARK_GRAY + " ⇨ " + TextFormatting.RESET + input));
         mc.field_71456_v.func_146158_b().func_146227_a(text);
      }
   }

   static void printIRC(String input) {
      if (mc.field_71439_g != null) {
         ITextComponent text = new StringTextComponent(TextFormatting.DARK_GRAY + "[IRC] " + TextFormatting.RESET + input);
         mc.field_71456_v.func_146158_b().func_146227_a(text);
      }
   }

   static void printDM(String input) {
      if (mc.field_71439_g != null) {
         ITextComponent text = new StringTextComponent(input);
         mc.field_71456_v.func_146158_b().func_146227_a(text);
      }
   }

   static void print2(String input) {
      if (mc.field_71439_g != null) {
         ITextComponent text = gradient("Luminar", Luminar.instance.styleManager.getCurrentStyle().getColor(0), Luminar.instance.styleManager.getCurrentStyle().getColor(90), true).func_230529_a_(new StringTextComponent(TextFormatting.DARK_GRAY + " ⇨ " + TextFormatting.RESET + input));
         mc.field_71456_v.func_146158_b().func_146227_a(text);
      }
   }

   private static StringTextComponent gradient(String message, int firstColor, int endColor, boolean... params) {
      StringTextComponent result = new StringTextComponent("");

      for(int i = 0; i < message.length(); ++i) {
         int interpolatedColor = ColorUtils.interpolateColor(firstColor, endColor, (float)i * 1.1F / (float)message.length());
         Style style;
         if (params.length > 0 && params[0]) {
            style = Style.field_240709_b_.func_240718_a_(Color.func_240743_a_(interpolatedColor)).func_240713_a_(true);
         } else {
            style = Style.field_240709_b_.func_240718_a_(Color.func_240743_a_(interpolatedColor));
         }

         result.func_230529_a_((new StringTextComponent(String.valueOf(message.charAt(i)))).func_230530_a_(style));
      }

      return result;
   }

   static void print2Clickable(String accessKey, String adminKey) {
      if (mc.field_71439_g != null) {
         ITextComponent text = gradient("Luminar", Luminar.instance.styleManager.getCurrentStyle().getColor(0), Luminar.instance.styleManager.getCurrentStyle().getColor(90), true).func_230529_a_(new StringTextComponent(TextFormatting.DARK_GRAY + " ⇨ " + TextFormatting.RESET)).func_230529_a_(createClickableText(TextFormatting.GREEN + "Конфиг сохранен!\nКлюч: " + TextFormatting.GREEN, accessKey)).func_230529_a_(createClickableText(TextFormatting.RED + "\nАдмин-ключ: " + TextFormatting.RED, adminKey));
         mc.field_71456_v.func_146158_b().func_146227_a(text);
      }
   }

   private static ITextComponent createClickableText(String prefix, String key) {
      StringTextComponent clickablePart = new StringTextComponent(key);
      clickablePart.func_230530_a_(Style.field_240709_b_.func_240715_a_(new ClickEvent(Action.COPY_TO_CLIPBOARD, key)).func_240716_a_(new HoverEvent(net.minecraft.util.text.event.HoverEvent.Action.field_230550_a_, new StringTextComponent(TextFormatting.GRAY + "Нажмите чтобы скопировать"))));
      return (new StringTextComponent(prefix)).func_230529_a_(clickablePart);
   }
}
