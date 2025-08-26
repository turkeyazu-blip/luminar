package ru.luminar.feature.managers.config.files;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.client.Minecraft;
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

public class ConfigManager {
   private static ConfigManager instance;
   public static final File cfgDir;
   private static final File LAST_CONFIG_FILE;
   private JsonObject currentConfig = new JsonObject();
   private final JsonParser jsonParser = new JsonParser();

   public static ConfigManager getInstance() {
      if (instance == null) {
         instance = new ConfigManager();
         instance.createDefaultConfig();
      }

      return instance;
   }

   private void createDefaultConfig() {
      this.currentConfig.add("functions", new JsonObject());
      this.currentConfig.add("styles", new JsonObject());
   }

   public void saveLastConfigLocally() {
      try {
         String configJson = this.getCurrentConfigJson();
         Files.write(LAST_CONFIG_FILE.toPath(), configJson.getBytes(), new OpenOption[0]);
      } catch (IOException var2) {
         System.err.println("Failed to save last config: " + var2.getMessage());
      }

   }

   public boolean loadLastConfigLocally() {
      if (!LAST_CONFIG_FILE.exists()) {
         return false;
      } else {
         try {
            String configJson = new String(Files.readAllBytes(LAST_CONFIG_FILE.toPath()));
            this.loadConfiguration(configJson);
            return true;
         } catch (IOException var2) {
            System.err.println("Failed to load last config: " + var2.getMessage());
            return false;
         }
      }
   }

   public void loadConfiguration(String jsonData) {
      try {
         JsonObject newConfig = this.jsonParser.parse(jsonData).getAsJsonObject();
         this.currentConfig = newConfig;
         this.applyConfig(newConfig);
         IMinecraft.print2(TextFormatting.GREEN + "Конфиг успешно загружен!");
      } catch (Exception var3) {
         TextFormatting var10000 = TextFormatting.RED;
         IMinecraft.print2(var10000 + "Ошибка загрузки: " + var3.getMessage());
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

   public void deleteLastConfig() {
      if (LAST_CONFIG_FILE.exists()) {
         LAST_CONFIG_FILE.delete();
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

   public void saveConfig(String configName) {
      try {
         File configFile = new File(cfgDir, configName + ".cfg");
         String configJson = this.getCurrentConfigJson();
         Files.write(configFile.toPath(), configJson.getBytes(), new OpenOption[0]);
         IMinecraft.print2(TextFormatting.GREEN + "Конфиг " + configName + " успешно сохранен!");
      } catch (IOException var4) {
         TextFormatting var10000 = TextFormatting.RED;
         IMinecraft.print2(var10000 + "Ошибка сохранения конфига: " + var4.getMessage());
      }

   }

   public boolean loadConfig(String configName) {
      File configFile = new File(cfgDir, configName + ".cfg");
      if (!configFile.exists()) {
         IMinecraft.print2(TextFormatting.RED + "Конфиг " + configName + " не найден!");
         return false;
      } else {
         try {
            String configJson = new String(Files.readAllBytes(configFile.toPath()));
            this.loadConfiguration(configJson);
            IMinecraft.print2(TextFormatting.GREEN + "Конфиг " + configName + " успешно загружен!");
            return true;
         } catch (IOException var4) {
            TextFormatting var10000 = TextFormatting.RED;
            IMinecraft.print2(var10000 + "Ошибка загрузки конфига: " + var4.getMessage());
            return false;
         }
      }
   }

   public void deleteConfig(String configName) {
      File configFile = new File(cfgDir, configName + ".cfg");
      if (configFile.exists()) {
         if (configFile.delete()) {
            IMinecraft.print2(TextFormatting.GREEN + "Конфиг " + configName + " удален!");
         } else {
            IMinecraft.print2(TextFormatting.RED + "Не удалось удалить конфиг " + configName);
         }
      }

   }

   public String[] getConfigList() {
      return cfgDir.list((dir, name) -> {
         return name.endsWith(".cfg");
      });
   }

   static {
      cfgDir = new File(Minecraft.func_71410_x().field_71412_D + "\\luminar\\configs");
      LAST_CONFIG_FILE = new File(Minecraft.func_71410_x().field_71412_D + "\\luminar\\configs\\last.cfg");
   }
}
