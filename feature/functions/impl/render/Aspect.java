package ru.luminar.feature.functions.impl.render;

import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;

public class Aspect extends Function {
   public ModeSetting mode = new ModeSetting("Пресет", "16:9", new String[]{"16:9", "4:3", "1:1", "21:9", "Кастом"});
   public SliderSetting scale = (new SliderSetting("Размер", 1.0F, 0.1F, 2.0F, 0.05F)).setVisible(() -> {
      return this.mode.is("Кастом");
   });
   public float res;

   public Aspect() {
      super("AspectRatio", Category.Render);
      this.addSettings(new Setting[]{this.mode, this.scale});
   }

   @Subscribe
   public void onUpdate(EventUpdate e) {
      if (this.mode.is("16:9")) {
         this.res = 1.0F;
      } else if (this.mode.is("4:3")) {
         this.res = 0.75F;
      } else if (this.mode.is("1:1")) {
         this.res = 0.5625F;
      } else if (this.mode.is("21:9")) {
         this.res = 1.3125F;
      } else if (this.mode.is("Кастом")) {
         this.res = (Float)this.scale.get();
      }

   }
}
