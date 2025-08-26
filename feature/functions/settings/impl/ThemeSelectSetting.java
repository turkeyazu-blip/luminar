package ru.luminar.feature.functions.settings.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.utils.themes.Theme;

public class ThemeSelectSetting extends Setting<Theme> {
   public List<Theme> themes;
   public Theme defaultValue;
   private List<Theme> originalThemes;
   private Map<String, Integer> originalOrder;

   public ThemeSelectSetting(String name, List<Theme> themes, Theme defaultVal) {
      super(name, defaultVal);
      this.defaultValue = defaultVal;
      this.themes = themes;
      this.originalThemes = themes;
      this.originalOrder = new HashMap();

      for(int i = 0; i < this.originalThemes.size(); ++i) {
         this.originalOrder.put(((Theme)this.originalThemes.get(i)).getStyleName(), i);
      }

      this.removeFromThemes(defaultVal);
   }

   public ColorSetting setVisible(Supplier<Boolean> bool) {
      return (ColorSetting)super.setVisible(bool);
   }

   public void setTheme(Theme theme) {
      Theme currentValue = (Theme)this.get();
      this.set(theme);
      if (!currentValue.equals(theme)) {
         this.returnPreviousTheme(currentValue);
         this.removeFromThemes(theme);
      }

   }

   private void returnPreviousTheme(Theme previousTheme) {
      if (!this.containsTheme(this.themes, previousTheme)) {
         List<Theme> newList = new ArrayList(this.themes);
         Integer originalPos = (Integer)this.originalOrder.get(previousTheme.getStyleName());
         if (originalPos != null) {
            int insertPos = this.findInsertPosition(newList, originalPos);
            newList.add(insertPos, previousTheme);
         } else {
            newList.add(previousTheme);
         }

         this.themes = newList;
      }
   }

   private int findInsertPosition(List<Theme> currentList, int originalPosition) {
      for(int i = 0; i < currentList.size(); ++i) {
         Theme t = (Theme)currentList.get(i);
         Integer pos = (Integer)this.originalOrder.get(t.getStyleName());
         if (pos != null && pos > originalPosition) {
            return i;
         }
      }

      return currentList.size();
   }

   private boolean containsTheme(List<Theme> list, Theme theme) {
      Iterator var3 = list.iterator();

      Theme t;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         t = (Theme)var3.next();
      } while(!t.equals(theme));

      return true;
   }

   public void removeFromThemes(Theme theme) {
      this.themes.removeIf((t) -> {
         return t.equals(theme);
      });
   }

   public void resetThemes() {
      this.themes = new ArrayList(this.originalThemes);
   }

   public int getThemeIndex() {
      for(int i = 0; i < this.themes.size(); ++i) {
         if (((Theme)this.themes.get(i)).equals(this.get())) {
            return i;
         }
      }

      return 0;
   }

   public boolean is(Theme theme) {
      return ((Theme)this.get()).equals(theme);
   }
}
