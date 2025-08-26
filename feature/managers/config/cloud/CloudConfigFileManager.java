package ru.luminar.feature.managers.config.cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import ru.luminar.Luminar;

public class CloudConfigFileManager {
   private static final String CONFIG_FILE = "cloud_settings.cfg";
   private final File configFile;

   public CloudConfigFileManager() {
      this.configFile = new File(Minecraft.func_71410_x().field_71412_D + "\\luminar\\configs", "cloud_settings.cfg");
      this.createIfNotExists();
   }

   private void createIfNotExists() {
      if (!this.configFile.exists()) {
         try {
            this.configFile.getParentFile().mkdirs();
            this.configFile.createNewFile();
            this.writeConfigValue(false);
         } catch (IOException var2) {
            System.err.println("Failed to create cloud config file: " + var2.getMessage());
         }
      }

   }

   public void writeConfigValue(boolean useCloud) {
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(this.configFile));

         try {
            writer.write("useCloudCfg=" + useCloud);
         } catch (Throwable var6) {
            try {
               writer.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         writer.close();
      } catch (IOException var7) {
         System.err.println("Failed to write cloud config: " + var7.getMessage());
      }

   }

   public boolean readConfigValue() {
      try {
         BufferedReader reader = new BufferedReader(new FileReader(this.configFile));

         label33: {
            boolean var3;
            try {
               String line = reader.readLine();
               if (line == null || !line.startsWith("useCloudCfg=")) {
                  break label33;
               }

               var3 = Boolean.parseBoolean(line.substring(12));
            } catch (Throwable var5) {
               try {
                  reader.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            reader.close();
            return var3;
         }

         reader.close();
      } catch (IOException var6) {
         System.err.println("Failed to read cloud config: " + var6.getMessage());
      }

      return false;
   }

   public void syncWithLuminar() {
      Luminar.useCloudCfg = this.readConfigValue();
   }

   public void updateFromLuminar() {
      this.writeConfigValue(Luminar.useCloudCfg);
   }
}
