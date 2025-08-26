package ru.luminar.feature.functions.settings;

import java.util.function.Supplier;

public class Setting<Value> implements ISetting {
   public Value defaultVal;
   String settingName;
   public Supplier<Boolean> visible = () -> {
      return true;
   };

   public Setting(String name, Value defaultVal) {
      this.settingName = name;
      this.defaultVal = defaultVal;
   }

   public String getName() {
      return this.settingName;
   }

   public void set(Value val) {
      this.defaultVal = val;
   }

   public void setName(String val) {
      this.settingName = val;
   }

   public Setting<?> setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
      return this;
   }

   public Value get() {
      return this.defaultVal;
   }
}
