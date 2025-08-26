package ru.luminar.feature.functions.impl.render;

import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;

public class HitColor extends Function {
   public static ColorSetting color = new ColorSetting("Цвет", -1);
   public static SliderSetting opacity = new SliderSetting("Прозрачность", 0.6F, 0.0F, 0.99F, 0.01F);

   public HitColor() {
      super("HitColor", Category.Render);
      this.addSettings(new Setting[]{color});
   }
}
