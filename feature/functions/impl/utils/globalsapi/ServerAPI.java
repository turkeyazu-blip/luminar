package ru.luminar.feature.functions.impl.utils.globalsapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import ru.luminar.mixins.accessors.MinecraftAccessor;
import ru.luminar.utils.client.IMinecraft;

public class ServerAPI implements IMinecraft {
   public static Socket socket;
   static OutputStream output;
   static InputStream input;
   static PrintWriter writer;
   static BufferedReader reader;
   public static boolean connected;
   static String lastName;

   public static void init() {
      try {
         socket = new Socket();
         socket.connect(new InetSocketAddress("89.35.130.25", 25565), 5000);
         output = socket.getOutputStream();
         input = socket.getInputStream();
         writer = new PrintWriter(output, true);
         reader = new BufferedReader(new InputStreamReader(input));
         writer.println("luminarapi7H3xQ");
         connected = true;
         System.out.println("Successfully connected to server");
      } catch (IOException var1) {
         System.err.println("Connection error: " + var1.getMessage());
         var1.printStackTrace();
      }

   }

   public static void updateName() {
      if (writer != null) {
         writer.println(((MinecraftAccessor)mc).getSession().func_111285_a() + "/luminar");
         lastName = ((MinecraftAccessor)mc).getSession().func_111285_a();
      }

   }

   public static String getClients() {
      if (writer != null && reader != null) {
         try {
            if (((MinecraftAccessor)mc).getSession().func_111285_a() != lastName) {
               writer.println("/finish " + lastName);
               updateName();
            }

            writer.println("/getClients");
            String response = reader.readLine();
            writer.println(response);
            return response;
         } catch (IOException var1) {
            System.out.println("Error getting clients: " + var1.getMessage());
            init();
            return null;
         }
      } else {
         return "Connection not established";
      }
   }

   public static void finish() {
      try {
         if (writer != null) {
            writer.println("/finish " + ((MinecraftAccessor)mc).getSession().func_111285_a());
         }

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

         connected = false;
      } catch (IOException var1) {
      }

   }
}
