package ru.luminar.feature.managers.config.cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.mixins.accessors.MinecraftAccessor;
import ru.luminar.utils.client.IMinecraft;

public class CloudAPI implements IMinecraft {
   private static Socket socket;
   private static PrintWriter writer;
   private static BufferedReader reader;
   public static boolean connected = false;
   private static final ExecutorService executor = Executors.newSingleThreadExecutor();
   private static CloudAPI.CloudMessageListener messageListener;

   public static void init() {
      try {
         socket = new Socket();
         socket.connect(new InetSocketAddress("89.35.130.25", 25568), 5000);
         socket.setKeepAlive(true);
         writer = new PrintWriter(socket.getOutputStream(), true);
         reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         writer.println("luminar_cloud_v2");
         connected = true;
         startMessageListener();
      } catch (IOException var1) {
         TextFormatting var10000 = TextFormatting.RED;
         IMinecraft.print2(var10000 + "Ошибка подключения: " + var1.getMessage());
      }

   }

   private static void startMessageListener() {
      messageListener = new CloudAPI.CloudMessageListener();
      executor.execute(messageListener);
   }

   public static void sendCommand(String command) {
      if (writer != null && connected) {
         writer.println(command);
      }

   }

   public static void finish() {
      connected = false;

      try {
         if (writer != null) {
            writer.close();
         }

         if (reader != null) {
            reader.close();
         }

         if (socket != null) {
            socket.close();
         }

         executor.shutdown();
         messageListener = null;
      } catch (IOException var1) {
         TextFormatting var10000 = TextFormatting.RED;
         IMinecraft.print2(var10000 + "Ошибка отключения: " + var1.getMessage());
      }

   }

   public static void saveLastConfigForPlayer(String configData) {
      String playerName = ((MinecraftAccessor)mc).getSession().func_111285_a();
      sendCommand("SAVE_LAST::" + playerName + "::" + configData);
   }

   public static void requestLastConfig() {
      String playerName = ((MinecraftAccessor)mc).getSession().func_111285_a();
      sendCommand("GET_LAST::" + playerName);
   }

   private static class CloudMessageListener implements Runnable {
      public void run() {
         while(true) {
            try {
               if (CloudAPI.connected) {
                  if (CloudAPI.reader.ready()) {
                     String response = CloudAPI.reader.readLine();
                     this.handleResponse(response);
                  }

                  Thread.sleep(50L);
                  continue;
               }
            } catch (Exception var2) {
               if (CloudAPI.connected) {
                  TextFormatting var10000 = TextFormatting.RED;
                  IMinecraft.print2(var10000 + "Ошибка соединения: " + var2.getMessage());
               }
            }

            return;
         }
      }

      private void handleResponse(String response) {
         if (response.startsWith("SAVE_SUCCESS::")) {
            String[] parts = response.split("::");
            if (parts.length == 3) {
               String accessKey = parts[1];
               String adminKey = parts[2];
               IMinecraft.print2Clickable(accessKey, adminKey);
            }
         } else if (response.startsWith("LOAD::")) {
            CloudConfigManager.getInstance().loadFromCloud(response.substring(6));
         } else {
            TextFormatting var10000;
            if (response.startsWith("DELETE_SUCCESS::")) {
               var10000 = TextFormatting.GREEN;
               IMinecraft.print2(var10000 + "Конфиг удален: " + response.substring(16));
            } else if (response.startsWith("ERROR::")) {
               var10000 = TextFormatting.RED;
               IMinecraft.print2(var10000 + response.substring(7));
            } else if (response.startsWith("LAST_CONFIG::")) {
               CloudConfigManager.getInstance().loadFromCloud(response.substring(13));
            } else if (!response.startsWith("LAST_SAVED::") && response.startsWith("RELOAD_SUCCESS::")) {
               var10000 = TextFormatting.GREEN;
               IMinecraft.print2(var10000 + "Обновили конфиг " + response.substring(16));
            }
         }

      }
   }
}
