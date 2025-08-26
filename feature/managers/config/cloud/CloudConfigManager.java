package ru.luminar.feature.managers.config.cloud;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BindSetting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.themes.Theme;

public class CloudConfigManager implements IMinecraft {
   private static CloudConfigManager instance;
   private final Logger logger = Logger.getLogger(CloudConfigManager.class.getName());
   private final JsonParser jsonParser = new JsonParser();
   private JsonObject currentConfig = new JsonObject();

   public static CloudConfigManager getInstance() {
      if (instance == null) {
         instance = new CloudConfigManager();
         instance.createDefaultConfig();
      }

      return instance;
   }

   private void createDefaultConfig() {
      this.currentConfig.add("functions", new JsonObject());
      this.currentConfig.add("styles", new JsonObject());
   }

   public void loadFromCloud(String jsonData) {
      try {
         JsonObject newConfig = this.jsonParser.parse(jsonData).getAsJsonObject();
         this.currentConfig = newConfig;
         this.applyConfig(newConfig);
         IMinecraft.print2(TextFormatting.GREEN + "Конфиг успешно загружен из облака!");
      } catch (Exception var3) {
         TextFormatting var10000 = TextFormatting.RED;
         IMinecraft.print2(var10000 + "Ошибка загрузки: " + var3.getMessage());
         this.logger.log(Level.SEVERE, "Cloud config load error", var3);
      }

   }

   public void autoSaveOnExit() {
      String configJson = this.getCurrentConfigJson();
      CloudAPI.saveLastConfigForPlayer(configJson);
   }

   public void tryLoadLastConfig() {
      if (Luminar.useCloudCfg) {
         CloudAPI.requestLastConfig();
      }

   }

   private void applyConfig(JsonObject config) {
      JsonObject functions;
      if (config.has("styles")) {
         functions = config.getAsJsonObject("styles");
         functions.entrySet().forEach((entry) -> {
            String styleName = (String)entry.getKey();
            JsonObject styleData = ((JsonElement)entry.getValue()).getAsJsonObject();
            Theme style = Luminar.instance.styleManager.getStyleByName(styleName);
            if (style != null) {
               if (styleData.has("selected") && styleData.get("selected").getAsBoolean()) {
                  Luminar.instance.styleManager.setCurrentStyle(style);
                  Luminar.instance.functions.clientColor.themes.setTheme(style);
               }

               if ("Кастом".equals(styleName)) {
                  String secondColorStr;
                  Color secondColor;
                  if (styleData.has("firstColor")) {
                     secondColorStr = styleData.get("firstColor").getAsString();
                     secondColor = this.parseColorFromString(secondColorStr);
                     if (secondColor != null) {
                        style.setFirstColor(secondColor);
                     }
                  }

                  if (styleData.has("secondColor")) {
                     secondColorStr = styleData.get("secondColor").getAsString();
                     secondColor = this.parseColorFromString(secondColorStr);
                     if (secondColor != null) {
                        style.setSecondColor(secondColor);
                     }
                  }
               }

            }
         });
      }

      if (config.has("functions")) {
         functions = config.getAsJsonObject("functions");
         Luminar.instance.functions.functions.forEach((func) -> {
            String funcName = func.getName().toLowerCase();
            if (functions.has(funcName)) {
               JsonObject funcData = functions.getAsJsonObject(funcName);
               this.applyFunctionSettings(func, funcData);
            }

         });
      }

   }

   private Color parseColorFromString(String colorStr) {
      try {
         if (colorStr.startsWith("java.awt.Color[")) {
            String[] parts = colorStr.replace("java.awt.Color[", "").replace("]", "").split(",");
            int r = Integer.parseInt(parts[0].split("=")[1]);
            int g = Integer.parseInt(parts[1].split("=")[1]);
            int b = Integer.parseInt(parts[2].split("=")[1]);
            return new Color(r, g, b);
         }

         if (colorStr.startsWith("#")) {
            return Color.decode(colorStr);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }

   private void applyFunctionSettings(Function function, JsonObject data) {
      if (data.has("state")) {
         function.setState(data.get("state").getAsBoolean(), true);
      }

      if (data.has("bind")) {
         function.setBind(data.get("bind").getAsInt());
      }

      function.getSettings().forEach((setting) -> {
         if (data.has(setting.getName())) {
            this.applySetting(setting, data.get(setting.getName()));
         }

      });
      if (data.has("settings")) {
         JsonObject settings = data.getAsJsonObject("settings");
         function.getSettings().forEach((setting) -> {
            if (settings.has(setting.getName())) {
               this.applySetting(setting, settings.get(setting.getName()));
            }

         });
      }

   }

   private void applySetting(Setting<?> setting, JsonElement value) {
      if (setting instanceof BooleanSetting) {
         ((BooleanSetting)setting).set(value.getAsBoolean());
      } else if (setting instanceof SliderSetting) {
         ((SliderSetting)setting).set(value.getAsFloat());
      } else if (setting instanceof ModeSetting) {
         ((ModeSetting)setting).setMode(value.getAsString());
      } else if (setting instanceof ColorSetting) {
         ((ColorSetting)setting).set(value.getAsInt());
      } else if (setting instanceof BindSetting) {
         ((BindSetting)setting).set(value.getAsInt());
      }

   }

   public String getCurrentConfigJson() {
      this.updateCurrentConfig();
      return this.currentConfig.toString();
   }

   private void updateCurrentConfig() {
      JsonObject functions = new JsonObject();
      Luminar.instance.functions.functions.forEach((func) -> {
         JsonObject funcData = new JsonObject();
         funcData.addProperty("state", func.isEnabled());
         funcData.addProperty("bind", func.bind);
         JsonObject settings = new JsonObject();
         func.getSettings().forEach((setting) -> {
            if (setting instanceof BooleanSetting) {
               settings.addProperty(setting.getName(), (Boolean)((BooleanSetting)setting).get());
            } else if (setting instanceof SliderSetting) {
               settings.addProperty(setting.getName(), (Number)((SliderSetting)setting).get());
            } else if (setting instanceof ModeSetting) {
               settings.addProperty(setting.getName(), (String)((ModeSetting)setting).get());
            } else if (setting instanceof ColorSetting) {
               settings.addProperty(setting.getName(), (Number)((ColorSetting)setting).get());
            } else if (setting instanceof BindSetting) {
               settings.addProperty(setting.getName(), (Number)((BindSetting)setting).get());
            }

         });
         funcData.add("settings", settings);
         functions.add(func.getName().toLowerCase(), funcData);
      });
      this.currentConfig.add("functions", functions);
      JsonObject styles = new JsonObject();
      Luminar.instance.styleManager.getStyleList().forEach((style) -> {
         JsonObject styleData = new JsonObject();
         styleData.addProperty("selected", Luminar.instance.styleManager.getCurrentStyle() == style);
         if ("Кастом".equals(style.getStyleName())) {
            styleData.addProperty("firstColor", style.getFirstColor().toString());
            styleData.addProperty("secondColor", style.getSecondColor().toString());
         }

         styles.add(style.getStyleName(), styleData);
      });
      this.currentConfig.add("styles", styles);
   }
}
