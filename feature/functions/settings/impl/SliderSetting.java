package ru.luminar.feature.functions.settings.impl;

import java.util.function.Supplier;
import ru.luminar.feature.functions.settings.Setting;

public class SliderSetting extends Setting<Float> {
   public float min;
   public float max;
   public float increment;
   public float defaultValue;

   public SliderSetting(String name, float defaultVal, float min, float max, float increment) {
      super(name, defaultVal);
      this.defaultValue = defaultVal;
      this.min = min;
      this.max = max;
      this.increment = increment;
   }

   public SliderSetting setVisible(Supplier<Boolean> bool) {
      return (SliderSetting)super.setVisible(bool);
   }
}
