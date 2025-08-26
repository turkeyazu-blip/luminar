package ru.luminar.feature.functions.impl.utils.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.mixins.accessors.MinecraftAccessor;
import ru.luminar.utils.client.IMinecraft;

public class IRC implements IMinecraft {
   public static Socket socket;
   static OutputStream output;
   static InputStream input;
   public static PrintWriter writer;
   static BufferedReader reader;
   public static boolean inited;
   private static boolean wasMuted = false;

   public static void init() {
      try {
         socket = new Socket();
         socket.connect(new InetSocketAddress("89.35.130.25", 25566), 5000);
         socket.setKeepAlive(true);
         output = socket.getOutputStream();
         input = socket.getInputStream();
         writer = new PrintWriter(output, true);
         reader = new BufferedReader(new InputStreamReader(input));
         writer.println("luminarapi9Kp2Yx");
         String nickname = ((MinecraftAccessor)mc).getSession().func_111285_a();
         String hwid = getHwid();
         writer.println(".hwid " + nickname + " " + hwid);
         inited = true;
      } catch (IOException var2) {
         System.err.println("IRC connection error: " + var2.getMessage());
         var2.printStackTrace();
      }

   }

   private static String getHwid() {
      try {
         Process process = Runtime.getRuntime().exec(new String[]{"wmic", "csproduct", "get", "uuid"});
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         String hwid = "";

         String line;
         while((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.equalsIgnoreCase("UUID")) {
               hwid = line;
               break;
            }
         }

         return hwid.isEmpty() ? "unknown" : hwid;
      } catch (Exception var4) {
         return "unknown";
      }
   }

   public static void sendMessage(String message) {
      if (writer != null) {
         PrintWriter var10000 = writer;
         String var10001 = ((MinecraftAccessor)mc).getSession().func_111285_a();
         var10000.println(var10001 + ": " + message);
      }

   }

   public static String readMessages() {
      if (inited && writer != null && reader != null) {
         try {
            if (reader.ready()) {
               String message = reader.readLine();
               if (message != null) {
                  if (message.startsWith("MUTED")) {
                     String[] parts = message.split(" ", 3);

                     try {
                        long remainingSeconds = parts.length > 1 ? Long.parseLong(parts[1]) : 0L;
                        String reason = parts.length > 2 ? parts[2] : "Не указана";
                        String timeLeft;
                        if (remainingSeconds > 3600L) {
                           timeLeft = String.format("%d ч %d мин", remainingSeconds / 3600L, remainingSeconds % 3600L / 60L);
                        } else if (remainingSeconds > 60L) {
                           timeLeft = String.format("%d мин %d сек", remainingSeconds / 60L, remainingSeconds % 60L);
                        } else {
                           timeLeft = String.format("%d сек", remainingSeconds);
                        }

                        if (!wasMuted) {
                           String hwid = getHwid();
                           writer.println(".hwid_muted " + hwid);
                           wasMuted = true;
                        }

                        return TextFormatting.RED + "Вы замучены! Причина: " + reason + ". Осталось: " + timeLeft;
                     } catch (NumberFormatException var7) {
                        return TextFormatting.RED + "Ошибка формата времени мута";
                     }
                  }

                  if (message.startsWith("BANNED")) {
                     mc.func_71400_g();
                  }
               }

               return message;
            } else {
               return null;
            }
         } catch (IOException var8) {
            System.out.println("Ошибка чтения: " + var8.getMessage());
            return null;
         }
      } else {
         return null;
      }
   }

   public static void finish() {
      try {
         if (output != null) {
            output.close();
         }

         if (input != null) {
            input.close();
         }

         if (writer != null) {
            writer.close();
         }

         if (reader != null) {
            reader.close();
         }

         if (socket != null) {
            socket.close();
         }

         inited = false;
      } catch (IOException var1) {
         System.err.println("Error closing IRC connection: " + var1.getMessage());
      }

   }
}
