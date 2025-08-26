package ru.luminar.feature.functions.impl.utils.globalsapi;

import java.util.concurrent.ConcurrentHashMap;

public class ClientAPI {
   public static final ConcurrentHashMap<String, String> USERS = new ConcurrentHashMap();

   public static void update(String data) {
      if (data != null) {
         USERS.clear();
         String[] userDatas = data.split("&");
         String[] var2 = userDatas;
         int var3 = userDatas.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String userData = var2[var4];
            String[] parts = userData.split("/");
            if (parts.length >= 2) {
               USERS.put(parts[0], parts[1]);
            }
         }

      }
   }

   public String getClient(String nick) {
      return (String)USERS.get(nick);
   }
}
