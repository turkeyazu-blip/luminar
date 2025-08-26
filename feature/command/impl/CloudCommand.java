package ru.luminar.feature.command.impl;

import java.io.File;
import java.util.Arrays;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.Luminar;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.Parameters;
import ru.luminar.feature.managers.config.cloud.CloudAPI;
import ru.luminar.feature.managers.config.cloud.CloudConfigManager;
import ru.luminar.feature.managers.config.files.ConfigManager;

public class CloudCommand implements Command {
   private final Logger logger;
   private final CloudConfigManager configCloudManager;
   private final ConfigManager configManager;

   public CloudCommand(Logger logger) {
      this.logger = logger;
      this.configCloudManager = CloudConfigManager.getInstance();
      this.configManager = ConfigManager.getInstance();
   }

   public void execute(Parameters parameters) {
      String subCommand = (String)parameters.asString(0).orElse("");
      String var3;
      byte var4;
      if (Luminar.useCloudCfg) {
         var3 = subCommand.toLowerCase();
         var4 = -1;
         switch(var3.hashCode()) {
         case -1335458389:
            if (var3.equals("delete")) {
               var4 = 3;
            }
            break;
         case -934641255:
            if (var3.equals("reload")) {
               var4 = 2;
            }
            break;
         case 3327206:
            if (var3.equals("load")) {
               var4 = 1;
            }
            break;
         case 3522941:
            if (var3.equals("save")) {
               var4 = 0;
            }
         }

         switch(var4) {
         case 0:
            (new Thread(() -> {
               if (!CloudAPI.connected) {
                  CloudAPI.init();
               }

               try {
                  Thread.sleep(250L);
               } catch (InterruptedException var2) {
                  throw new RuntimeException(var2);
               }

               this.handleSaveCloud();
            })).start();
            break;
         case 1:
            (new Thread(() -> {
               if (!CloudAPI.connected) {
                  CloudAPI.init();
               }

               try {
                  Thread.sleep(250L);
               } catch (InterruptedException var3) {
                  throw new RuntimeException(var3);
               }

               this.handleLoadCloud(parameters);
            })).start();
            break;
         case 2:
            (new Thread(() -> {
               if (!CloudAPI.connected) {
                  CloudAPI.init();
               }

               try {
                  Thread.sleep(250L);
               } catch (InterruptedException var3) {
                  throw new RuntimeException(var3);
               }

               this.handleReloadCloud(parameters);
            })).start();
            break;
         case 3:
            if (!CloudAPI.connected) {
               CloudAPI.init();
            }

            (new Thread(() -> {
               try {
                  Thread.sleep(250L);
               } catch (InterruptedException var3) {
                  throw new RuntimeException(var3);
               }

               this.handleDeleteCloud(parameters);
            })).start();
            break;
         default:
            this.showHelpCloud();
         }
      } else {
         var3 = subCommand.toLowerCase();
         var4 = -1;
         switch(var3.hashCode()) {
         case 3322014:
            if (var3.equals("list")) {
               var4 = 2;
            }
            break;
         case 3327206:
            if (var3.equals("load")) {
               var4 = 1;
            }
            break;
         case 3522941:
            if (var3.equals("save")) {
               var4 = 0;
            }
         }

         switch(var4) {
         case 0:
            this.handleSave(parameters);
            break;
         case 1:
            this.handleLoad(parameters);
            break;
         case 2:
            this.handleList();
            break;
         default:
            this.showHelp();
         }
      }

   }

   private void handleSave(Parameters parameters) {
      if (parameters.asString(1).isEmpty()) {
         this.logger.log(TextFormatting.RED + "Используйте: .cfg save <название>");
      } else {
         this.configManager.saveConfig((String)parameters.asString(1).get());
      }
   }

   private void handleLoad(Parameters parameters) {
      if (parameters.asString(1).isEmpty()) {
         this.logger.log(TextFormatting.RED + "Используйте: .cfg load <название>");
      } else {
         this.configManager.loadConfig((String)parameters.asString(1).get());
      }
   }

   private void handleList() {
      File[] configs = ConfigManager.cfgDir.listFiles((dir, name) -> {
         return name.endsWith(".cfg") && !name.equals("last.cfg") && !name.equals("cloud_settings.cfg");
      });
      if (configs != null && configs.length != 0) {
         this.logger.log(TextFormatting.GREEN + "Доступные локальные конфиги:");
         Arrays.stream(configs).map(File::getName).map((name) -> {
            return name.replace(".cfg", "");
         }).sorted().forEach((name) -> {
            this.logger.log("- " + name);
         });
      } else {
         this.logger.log(TextFormatting.GREEN + "Не найдено ни одного локального конфига");
      }
   }

   private void showHelp() {
      this.logger.log(TextFormatting.GREEN + "Локальные конфиги:");
      this.logger.log(TextFormatting.YELLOW + ".cfg save <название> " + TextFormatting.RESET + "- Сохранить текущий конфиг");
      this.logger.log(TextFormatting.YELLOW + ".cfg load <названия> " + TextFormatting.RESET + "- Загрузить конфиг");
      this.logger.log(TextFormatting.YELLOW + ".cfg list " + TextFormatting.RESET + "- Список доступных конфигов");
   }

   private void handleSaveCloud() {
      String configData = this.configCloudManager.getCurrentConfigJson();
      CloudAPI.sendCommand("SAVE::" + configData);
      this.logger.log(TextFormatting.GREEN + "Сохранение конфига в облако...");
   }

   private void handleLoadCloud(Parameters parameters) {
      if (parameters.asString(1).isEmpty()) {
         this.logger.log(TextFormatting.RED + "Используйте: .cfg load <ключ>");
      } else {
         String accessKey = (String)parameters.asString(1).get();
         CloudAPI.sendCommand("LOAD::" + accessKey);
         this.logger.log(TextFormatting.GREEN + "Загрузка конфига из облака...");
      }
   }

   private void handleDeleteCloud(Parameters parameters) {
      if (parameters.asString(2).isEmpty()) {
         this.logger.log(TextFormatting.RED + "Используйте: .cfg delete <ключ> <админ_ключ>");
      } else {
         String accessKey = (String)parameters.asString(1).get();
         String adminKey = (String)parameters.asString(2).get();
         CloudAPI.sendCommand("DELETE::" + accessKey + "::" + adminKey);
         this.logger.log(TextFormatting.GREEN + "Запрос на удаление конфига...");
      }
   }

   private void handleReloadCloud(Parameters parameters) {
      if (parameters.asString(2).isEmpty()) {
         this.logger.log(TextFormatting.RED + "Используйте: .cfg reload <ключ> <админ_ключ>");
      } else {
         String accessKey = (String)parameters.asString(1).get();
         String adminKey = (String)parameters.asString(2).get();
         CloudAPI.sendCommand("RELOAD::" + accessKey + "::" + adminKey);
         this.logger.log(TextFormatting.GREEN + "Запрос на удаление конфига...");
      }
   }

   private void showHelpCloud() {
      this.logger.log(TextFormatting.GREEN + "Облачные конфиги:");
      this.logger.log(TextFormatting.YELLOW + ".cfg save " + TextFormatting.RESET + "- Сохранить текущий конфиг");
      this.logger.log(TextFormatting.YELLOW + ".cfg load <ключ> " + TextFormatting.RESET + "- Загрузить конфиг");
      this.logger.log(TextFormatting.YELLOW + ".cfg reload <ключ> <админ_ключ> " + TextFormatting.RESET + "- Обновить конфиг");
      this.logger.log(TextFormatting.YELLOW + ".cfg delete <ключ> <админ_ключ> " + TextFormatting.RESET + "- Удалить конфиг");
   }

   public String name() {
      return "cfg";
   }

   public String description() {
      return "Облачное хранилище конфигов";
   }
}
