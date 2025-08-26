package ru.luminar.feature.functions.impl.render;

import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;

public class Fullbright extends Function {
   public Fullbright() {
      super("Fullbright", Category.Render);
   }

   @Subscribe
   public void onUpdate(EventUpdate e) {
      mc.field_71474_y.field_74333_Y = 1000.0D;
   }

   public void onDisable() {
      super.onDisable();
      mc.field_71474_y.field_74333_Y = 1.0D;
   }
}
