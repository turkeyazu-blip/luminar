package ru.luminar.feature.functions.impl.render;

import java.awt.Color;
import ru.luminar.Luminar;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.ThemeSelectSetting;
import ru.luminar.utils.themes.Theme;

public class ClientColor extends Function {
   public ThemeSelectSetting themes;
   public ColorSetting firstColor;
   public ColorSetting secondColor;

   public ClientColor() {
      super("ClientColor", Category.Render);
      this.themes = new ThemeSelectSetting("Цвет клиента", Luminar.themes, Luminar.instance.styleManager.getCurrentStyle());
      this.firstColor = (new ColorSetting("Первый цвет", -1)).setVisible(() -> {
         return this.themes.is(Luminar.instance.styleManager.getStyleByName("Кастом"));
      });
      this.secondColor = (new ColorSetting("Второй цвет", -1)).setVisible(() -> {
         return this.themes.is(Luminar.instance.styleManager.getStyleByName("Кастом"));
      });
      this.addSettings(new Setting[]{this.themes, this.firstColor, this.secondColor});
   }

   @Subscribe
   public void onUpdate(EventUpdate e) {
      if ((Boolean)this.firstColor.visible.get()) {
         Theme theme = Luminar.instance.styleManager.getStyleByName("Кастом");
         theme.setFirstColor(new Color((Integer)this.firstColor.get()));
         theme.setSecondColor(new Color((Integer)this.secondColor.get()));
      }

   }
}
