package ru.luminar.ui;

import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import sun.misc.Unsafe;

public class SessionChanger {
   private static final Unsafe unsafe = getUnsafe();

   public static void changeSession(Minecraft mc, Session newSession) {
      Field sessionField = null;

      try {
         sessionField = Minecraft.class.getDeclaredField("user");
      } catch (NoSuchFieldException var5) {
         var5.printStackTrace();
      }

      if (sessionField != null) {
         long offset = unsafe.objectFieldOffset(sessionField);
         unsafe.putObject(mc, offset, newSession);
      }
   }

   private static Unsafe getUnsafe() {
      try {
         Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
         theUnsafe.setAccessible(true);
         return (Unsafe)theUnsafe.get((Object)null);
      } catch (Exception var1) {
         throw new RuntimeException("Could not get Unsafe instance", var1);
      }
   }
}
