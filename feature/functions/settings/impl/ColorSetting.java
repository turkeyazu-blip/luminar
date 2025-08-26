package ru.luminar.feature.functions.settings.impl;

import java.util.function.Supplier;
import ru.luminar.feature.functions.settings.Setting;

public class ColorSetting extends Setting<Integer> {
   public float defaultValue;

   public ColorSetting(String name, Integer defaultVal) {
      super(name, defaultVal);
      this.defaultValue = (float)defaultVal;
   }

   public ColorSetting setVisible(Supplier<Boolean> bool) {
      return (ColorSetting)super.setVisible(bool);
   }
}
