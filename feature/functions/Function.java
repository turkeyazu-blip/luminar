package ru.luminar.feature.functions;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.utils.client.IMinecraft;

public abstract class Function implements IMinecraft {
   final String name;
   final Category category;
   String description;
   public boolean enabled;
   public int bind = -1;
   final List<Setting<?>> settings = new ObjectArrayList();
   private boolean pendingRemoval = false;

   public Function(String name, Category category, int bind, String description) {
      this.name = name;
      this.category = category;
      this.bind = bind;
      this.description = description;
   }

   public Function(String name, Category category, String description) {
      this.name = name;
      this.category = category;
      this.description = description;
   }

   public Function(String name, Category category, int bind) {
      this.name = name;
      this.category = category;
      this.bind = bind;
      this.description = "У этого модуля нет описания";
   }

   public Function(String name, Category category) {
      this.name = name;
      this.category = category;
      this.description = "У этого модуля нет описания";
   }

   public void addSettings(Setting<?>... settings) {
      this.settings.addAll(List.of(settings));
   }

   public void onEnable() {
      Luminar.instance.eventBus.register(this);
      MinecraftForge.EVENT_BUS.register(this);
   }

   public void onDisable() {
      Luminar.instance.eventBus.unregister(this);
      MinecraftForge.EVENT_BUS.unregister(this);
   }

   public final void toggle() {
      this.enabled = !this.enabled;
      if (!this.enabled) {
         this.onDisable();
         if ((Boolean)Island.funcs.get()) {
            Island.islandManager.add(this.name + " выключен", 3);
         }
      } else {
         this.onEnable();
         if ((Boolean)Island.funcs.get()) {
            Island.islandManager.add(this.name + " включен", 3);
         }
      }

   }

   public final void setState(boolean newState, boolean config) {
      if (this.enabled != newState) {
         this.enabled = newState;

         try {
            if (this.enabled) {
               this.onEnable();
            } else {
               this.onDisable();
            }
         } catch (Exception var4) {
            this.handleException(this.enabled ? "onEnable" : "onDisable", var4);
         }

      }
   }

   private void handleException(String methodName, Exception e) {
      if (mc.field_71439_g != null) {
         e.printStackTrace();
      } else {
         System.out.println("[" + this.name + " Error" + methodName + "() Message: " + e.getMessage());
         e.printStackTrace();
      }

   }

   public Category getCategory() {
      return this.category;
   }

   public String getName() {
      return this.name;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setBind(int i) {
      this.bind = i;
   }

   public List<Setting<?>> getSettings() {
      return this.settings;
   }

   public void onKey(int key) {
   }

   public boolean isPendingRemoval() {
      return this.pendingRemoval;
   }

   public void setPendingRemoval(boolean pendingRemoval) {
      this.pendingRemoval = pendingRemoval;
   }
}
