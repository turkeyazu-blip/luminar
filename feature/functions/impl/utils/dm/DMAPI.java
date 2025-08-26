package ru.luminar.feature.functions.impl.utils.dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import ru.luminar.mixins.accessors.MinecraftAccessor;
import ru.luminar.utils.client.IMinecraft;

public class DMAPI implements IMinecraft {
   private static Socket socket;
   private static PrintWriter writer;
   private static BufferedReader reader;
   public static boolean registered = false;

   public static void init() {
      try {
         socket = new Socket();
         socket.connect(new InetSocketAddress("89.35.130.25", 25567), 5000);
         writer = new PrintWriter(socket.getOutputStream(), true);
         reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         writer.println("luminarapiR8fq5M");
         System.out.println("Connected to DM server");
      } catch (IOException var1) {
         System.err.println("DM connection error: " + var1.getMessage());
      }

   }

   public static void register() {
      if (writer != null) {
         String username = ((MinecraftAccessor)mc).getSession().func_111285_a();
         writer.println(".dm register " + username);
         registered = true;
      }

   }

   public static void sendDM(String recipient, String message) {
      if (writer != null && registered) {
         writer.println(".dm send " + recipient + " " + message);
      }

   }

   public static String checkMessages() {
      try {
         if (reader != null && reader.ready()) {
            return reader.readLine();
         }
      } catch (IOException var1) {
         System.err.println("Error reading DM: " + var1.getMessage());
      }

      return null;
   }

   public static String extractSenderFromDM(String message) {
      if (message != null && message.startsWith("Сообщение от ")) {
         int colonIndex = message.indexOf(58);
         return colonIndex == -1 ? null : message.substring(13, colonIndex).trim();
      } else {
         return null;
      }
   }

   public static void finish() {
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

         registered = false;
      } catch (IOException var1) {
         System.err.println("Error closing DM connection: " + var1.getMessage());
      }

   }
}
