package ru.luminar.utils.draggables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;

public class DragManager {
   public static HashMap<String, Dragging> draggables = new HashMap();
   public static final File DRAG_DATA;
   private static final Gson GSON;

   public static void save() {
      if (!DRAG_DATA.exists()) {
         try {
            DRAG_DATA.createNewFile();
         } catch (IOException var2) {
            throw new RuntimeException(var2);
         }
      }

      try {
         Files.writeString(DRAG_DATA.toPath(), GSON.toJson(draggables.values()), new OpenOption[0]);
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }

   public static void load() {
      if (!DRAG_DATA.exists()) {
         try {
            DRAG_DATA.createNewFile();
         } catch (IOException var6) {
            throw new RuntimeException(var6);
         }
      } else {
         Dragging[] draggings;
         try {
            draggings = (Dragging[])GSON.fromJson(Files.readString(DRAG_DATA.toPath()), Dragging[].class);
         } catch (IOException var7) {
            var7.printStackTrace();
            return;
         }

         Dragging[] var1 = draggings;
         int var2 = draggings.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Dragging dragging = var1[var3];
            if (dragging == null) {
               return;
            }

            Dragging currentDrag = (Dragging)draggables.get(dragging.getName());
            if (currentDrag != null) {
               currentDrag.setX(dragging.getX());
               currentDrag.setY(dragging.getY());
               draggables.put(dragging.getName(), currentDrag);
            }
         }

      }
   }

   public static void onRelease(int button, MainWindow res) {
      Iterator var2 = draggables.values().iterator();

      while(var2.hasNext()) {
         Dragging dragging = (Dragging)var2.next();
         dragging.onRelease(button, res);
      }

   }

   public static void onClick(double mouseX, double mouseY, int button) {
      Iterator var5 = draggables.values().iterator();

      while(var5.hasNext()) {
         Dragging dragging = (Dragging)var5.next();
         if (dragging.onClick(mouseX, mouseY, button)) {
            break;
         }
      }

   }

   static {
      DRAG_DATA = new File(Minecraft.func_71410_x().field_71412_D, "\\luminar\\draggables.file");
      GSON = (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
   }
}
