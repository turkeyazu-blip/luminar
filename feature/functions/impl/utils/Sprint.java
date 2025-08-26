package ru.luminar.feature.functions.impl.utils;

import ru.luminar.events.Event;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;

public class Sprint extends Function {
   public Sprint() {
      super("Sprint", Category.Utils);
   }

   @Subscribe
   public void onEvent(Event e) {
      if (e instanceof EventUpdate) {
         mc.field_71474_y.field_151444_V.func_225593_a_(mc.field_71474_y.field_74351_w.func_151470_d());
      }

   }
}
