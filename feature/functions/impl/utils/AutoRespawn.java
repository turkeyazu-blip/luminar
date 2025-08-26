package ru.luminar.feature.functions.impl.utils;

import net.minecraft.client.gui.screen.DeathScreen;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;

public class AutoRespawn extends Function {
   public AutoRespawn() {
      super("AutoRespawn", Category.Utils);
   }

   @Subscribe
   public void onUpdate(EventUpdate e) {
      if (mc.field_71439_g != null) {
         if (mc.field_71462_r instanceof DeathScreen) {
            mc.field_71439_g.func_71004_bE();
         }

      }
   }
}
