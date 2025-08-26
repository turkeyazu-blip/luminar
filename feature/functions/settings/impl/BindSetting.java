package ru.luminar.feature.functions.settings.impl;

import java.util.function.Supplier;
import ru.luminar.feature.functions.settings.Setting;

public class BindSetting extends Setting<Integer> {
   public BindSetting(String name, Integer defaultVal) {
      super(name, defaultVal);
   }

   public BindSetting setVisible(Supplier<Boolean> bool) {
      return (BindSetting)super.setVisible(bool);
   }
}
