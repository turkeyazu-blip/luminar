package ru.luminar.utils.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.resources.I18n;

public class KeyStorage {
   public static final Map<String, Integer> keyMap = new HashMap();
   public static final Map<Integer, String> reverseKeyMap = new HashMap();

   public static String getKey(int integer) {
      if (integer <= 5 && integer > -1) {
         String var10000;
         switch(integer) {
         case 0:
            var10000 = I18n.func_135052_a("key.mouse.left", new Object[0]);
            break;
         case 1:
            var10000 = I18n.func_135052_a("key.mouse.right", new Object[0]);
            break;
         case 2:
            var10000 = I18n.func_135052_a("key.mouse.middle", new Object[0]);
            break;
         default:
            var10000 = "MOUSE" + integer;
         }

         return var10000;
      } else {
         return getReverseKey(integer);
      }
   }

   public static String getReverseKey(int key) {
      return (String)reverseKeyMap.getOrDefault(key, "");
   }

   public static Integer getKey(String key) {
      return (Integer)keyMap.getOrDefault(key, -1);
   }

   private static void putMappings() {
      keyMap.put("A", 65);
      keyMap.put("B", 66);
      keyMap.put("C", 67);
      keyMap.put("D", 68);
      keyMap.put("E", 69);
      keyMap.put("F", 70);
      keyMap.put("G", 71);
      keyMap.put("H", 72);
      keyMap.put("I", 73);
      keyMap.put("J", 74);
      keyMap.put("K", 75);
      keyMap.put("L", 76);
      keyMap.put("M", 77);
      keyMap.put("N", 78);
      keyMap.put("O", 79);
      keyMap.put("P", 80);
      keyMap.put("Q", 81);
      keyMap.put("R", 82);
      keyMap.put("S", 83);
      keyMap.put("T", 84);
      keyMap.put("U", 85);
      keyMap.put("V", 86);
      keyMap.put("W", 87);
      keyMap.put("X", 88);
      keyMap.put("Y", 89);
      keyMap.put("Z", 90);
      keyMap.put("0", 48);
      keyMap.put("1", 49);
      keyMap.put("2", 50);
      keyMap.put("3", 51);
      keyMap.put("4", 52);
      keyMap.put("5", 53);
      keyMap.put("6", 54);
      keyMap.put("7", 55);
      keyMap.put("8", 56);
      keyMap.put("9", 57);
      keyMap.put("F1", 290);
      keyMap.put("F2", 291);
      keyMap.put("F3", 292);
      keyMap.put("F4", 293);
      keyMap.put("F5", 294);
      keyMap.put("F6", 295);
      keyMap.put("F7", 296);
      keyMap.put("F8", 297);
      keyMap.put("F9", 298);
      keyMap.put("F10", 299);
      keyMap.put("F11", 300);
      keyMap.put("F12", 301);
      keyMap.put("NUMPAD1", 321);
      keyMap.put("NUMPAD2", 322);
      keyMap.put("NUMPAD3", 323);
      keyMap.put("NUMPAD4", 324);
      keyMap.put("NUMPAD5", 325);
      keyMap.put("NUMPAD6", 326);
      keyMap.put("NUMPAD7", 327);
      keyMap.put("NUMPAD8", 328);
      keyMap.put("NUMPAD9", 329);
      keyMap.put("NUMPAD0", 320);
      keyMap.put("Space", 32);
      keyMap.put("Enter", 257);
      keyMap.put("Escape", 256);
      keyMap.put("Home", 268);
      keyMap.put("Insert", 260);
      keyMap.put("Delete", 261);
      keyMap.put("End", 269);
      keyMap.put("Papeup", 266);
      keyMap.put("Pagedown", 267);
      keyMap.put("Right", 262);
      keyMap.put("Left", 263);
      keyMap.put("Down", 264);
      keyMap.put("Up", 265);
      keyMap.put("RShift", 344);
      keyMap.put("LShift", 340);
      keyMap.put("RCtrl", 345);
      keyMap.put("LCtrl", 341);
      keyMap.put("RAlt", 346);
      keyMap.put("LAlt", 342);
      keyMap.put("RWIN", 347);
      keyMap.put("WIN", 343);
      keyMap.put("Menu", 348);
      keyMap.put("Capslock", 280);
      keyMap.put("Tab", 258);
      keyMap.put("Numlock", 282);
      keyMap.put("Scrolllock", 281);
      keyMap.put("KP_Decimal", 330);
      keyMap.put("KP_Divide", 331);
      keyMap.put("KP_Multiply", 332);
      keyMap.put("KP_Subtract", 333);
      keyMap.put("KP_Add", 334);
      keyMap.put("KP_Enter", 335);
      keyMap.put("KP_Equal", 336);
      keyMap.put("Semicolon", 59);
      keyMap.put("Apostrophe", 39);
      keyMap.put("Slash", 47);
      keyMap.put("Minus", 45);
      keyMap.put("LBracket", 91);
      keyMap.put("RBracket", 93);
      keyMap.put("Equal", 61);
      keyMap.put("Back", 259);
      keyMap.put("Backslash", 92);
      keyMap.put("Period", 46);
      keyMap.put("Comma", 44);
      keyMap.put("Pause", 284);
      keyMap.put("PrintScreen", 283);
      keyMap.put("Grave", 96);
   }

   private static void reverseMappings() {
      Iterator var0 = keyMap.entrySet().iterator();

      while(var0.hasNext()) {
         Entry<String, Integer> entry = (Entry)var0.next();
         reverseKeyMap.put((Integer)entry.getValue(), (String)entry.getKey());
      }

   }

   static {
      putMappings();
      reverseMappings();
   }
}
