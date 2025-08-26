package ru.luminar.feature.functions.impl.render;

import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.managers.island.IslandManager;

public class Island extends Function {
   public static BooleanSetting funcs = new BooleanSetting("Функции", true);
   public static BooleanSetting irc = new BooleanSetting("Сообщения из IRC", false);
   public static BooleanSetting auc = new BooleanSetting("Продажи на аукционе", true);
   public static BooleanSetting time = new BooleanSetting("Время с секундами", true);
   public static IslandManager islandManager;

   public Island() {
      super("Island", Category.Render);
      this.addSettings(new Setting[]{funcs, auc, irc, time});
   }
}
