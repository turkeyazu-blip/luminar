package ru.luminar.feature.functions.settings.impl;

import java.util.function.Supplier;
import ru.luminar.feature.functions.settings.Setting;

public class BooleanSetting extends Setting<Boolean> {
   private boolean enabled;
   public float anim;
   public boolean defaultValue;

   public BooleanSetting(String name, Boolean defaultVal) {
      super(name, defaultVal);
      this.defaultValue = defaultVal;
   }

   public BooleanSetting setVisible(Supplier<Boolean> bool) {
      return (BooleanSetting)super.setVisible(bool);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void toggle() {
      this.enabled = !this.enabled;
   }
}
