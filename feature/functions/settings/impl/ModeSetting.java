package ru.luminar.feature.functions.settings.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import ru.luminar.feature.functions.settings.Setting;

public class ModeSetting extends Setting<String> {
   public String[] strings;
   public String defaultValue;
   private String[] originalStrings;
   private Map<String, Integer> originalOrder;

   public ModeSetting(String name, String defaultVal, String... strings) {
      super(name, defaultVal);
      this.defaultValue = defaultVal;
      this.strings = strings;
      this.originalStrings = (String[])strings.clone();
      this.originalOrder = new HashMap();

      for(int i = 0; i < this.originalStrings.length; ++i) {
         this.originalOrder.put(this.originalStrings[i], i);
      }

      this.removeFromStrings(defaultVal);
   }

   public int getIndex() {
      int index = 0;
      String[] var2 = this.strings;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String val = var2[var4];
         if (val.equalsIgnoreCase((String)this.get())) {
            return index;
         }

         ++index;
      }

      return 0;
   }

   public boolean is(String s) {
      return ((String)this.get()).equalsIgnoreCase(s);
   }

   public ModeSetting setVisible(Supplier<Boolean> bool) {
      return (ModeSetting)super.setVisible(bool);
   }

   public void resetStrings() {
      this.strings = (String[])this.originalStrings.clone();
   }

   public void setMode(String val) {
      String currentValue = (String)this.get();
      this.set(val);
      if (!currentValue.equalsIgnoreCase(val)) {
         this.returnPreviousValue(currentValue);
         this.removeFromStrings(val);
      }

   }

   private void returnPreviousValue(String previousValue) {
      if (!this.containsIgnoreCase(this.strings, previousValue)) {
         List<String> newList = new ArrayList(Arrays.asList(this.strings));
         Integer originalPos = (Integer)this.originalOrder.get(previousValue);
         if (originalPos != null) {
            int insertPos = this.findInsertPosition(newList, originalPos);
            newList.add(insertPos, previousValue);
         } else {
            newList.add(previousValue);
         }

         this.strings = (String[])newList.toArray(new String[0]);
      }
   }

   private int findInsertPosition(List<String> currentList, int originalPosition) {
      for(int i = 0; i < currentList.size(); ++i) {
         String s = (String)currentList.get(i);
         Integer pos = (Integer)this.originalOrder.get(s);
         if (pos != null && pos > originalPosition) {
            return i;
         }
      }

      return currentList.size();
   }

   private boolean containsIgnoreCase(String[] array, String value) {
      String[] var3 = array;
      int var4 = array.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String s = var3[var5];
         if (s.equalsIgnoreCase(value)) {
            return true;
         }
      }

      return false;
   }

   public void removeFromStrings(String val) {
      List<String> list = new ArrayList(Arrays.asList(this.strings));
      list.removeIf((s) -> {
         return s.equalsIgnoreCase(val);
      });
      this.strings = (String[])list.toArray(new String[0]);
   }
}
