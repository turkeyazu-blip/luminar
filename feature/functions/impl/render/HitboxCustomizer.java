package ru.luminar.feature.functions.impl.render;

import java.awt.Color;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;

public class HitboxCustomizer extends Function {
   public static BooleanSetting lines = new BooleanSetting("Линии", true);
   public static BooleanSetting clientColor = new BooleanSetting("Клиентский цвет", true);
   public static ColorSetting colorr = (new ColorSetting("Цвет линий", -1)).setVisible(() -> {
      return (Boolean)lines.get() && !(Boolean)clientColor.get();
   });
   public static BooleanSetting fill = new BooleanSetting("Заполнять", false);
   public static boolean canRender = false;
   public ColorSetting colorfilll = (new ColorSetting("Цвет линий", -1)).setVisible(() -> {
      return (Boolean)fill.get() && !(Boolean)clientColor.get();
   });
   public static BooleanSetting hideLook = new BooleanSetting("Убрать линию взгляда", true);
   public static SliderSetting alpha = new SliderSetting("Прозрачность", 1.0F, 0.0F, 1.0F, 0.05F);
   public static SliderSetting alphaFill = new SliderSetting("Прозрачность заливки", 1.0F, 0.0F, 1.0F, 0.05F);
   public Color color;
   public Color colorfill;

   public HitboxCustomizer() {
      super("HitboxCustomizer", Category.Render);
      this.color = Color.WHITE;
      this.colorfill = Color.WHITE;
      this.addSettings(new Setting[]{lines, clientColor, colorr, alpha, fill, alphaFill, hideLook});
   }

   @Subscribe
   public void onEvent(EventUpdate e) {
      this.color = new Color((Integer)colorr.get());
      this.colorfill = new Color((Integer)this.colorfilll.get());
   }
}
