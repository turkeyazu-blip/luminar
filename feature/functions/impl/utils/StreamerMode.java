package ru.luminar.feature.functions.impl.utils;

import ru.luminar.Luminar;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.mixins.accessors.MinecraftAccessor;

public class StreamerMode extends Function {
   public StreamerMode() {
      super("StreamerMode", Category.Utils);
   }

   public static String replace(String input) {
      if (mc.field_71441_e == null) {
         return input;
      } else if (Luminar.instance != null && Luminar.instance.functions.streamerMode.isEnabled()) {
         input = input.replace(((MinecraftAccessor)mc).getSession().func_111285_a(), "luminar");
         if (input.contains("Анархия-")) {
            input = input.replaceAll("Анархия-\\d+", "luminarvisuals.t.me");
         }

         return input;
      } else {
         return input;
      }
   }
}
