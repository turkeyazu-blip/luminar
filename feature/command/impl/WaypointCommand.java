package ru.luminar.feature.command.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.util.math.vector.Vector3i;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.Parameters;

public class WaypointCommand implements Command {
   final Logger logger;
   public static final Map<String, Vector3i> waypoints = new LinkedHashMap();

   public WaypointCommand(Logger logger) {
      this.logger = logger;
   }

   public void execute(Parameters parameters) {
      String pars = (String)parameters.asString(0).orElse("");
      byte var4 = -1;
      switch(pars.hashCode()) {
      case -934610812:
         if (pars.equals("remove")) {
            var4 = 1;
         }
         break;
      case 96417:
         if (pars.equals("add")) {
            var4 = 0;
         }
         break;
      case 3322014:
         if (pars.equals("list")) {
            var4 = 3;
         }
         break;
      case 94746189:
         if (pars.equals("clear")) {
            var4 = 2;
         }
      }

      switch(var4) {
      case 0:
         this.addGPS(parameters);
         break;
      case 1:
         this.removeGPS(parameters);
         break;
      case 2:
         waypoints.clear();
         this.logger.log("Очистил все метки");
         break;
      case 3:
         waypoints.forEach((name, vector) -> {
            this.logger.log(name + " " + vector.toString());
         });
         break;
      default:
         this.logger.log("Укажите тип команды: add, remove, clear, list");
      }

   }

   public static void addGPS(String name, int x, int y, int z) {
      waypoints.put(name, new Vector3i(x, y, z));
   }

   public static void removeGPS(String name) {
      if (waypoints.containsKey(name)) {
         waypoints.remove(name);
      }

   }

   private void addGPS(Parameters parameters) {
      String args = parameters.collectMessage(1);
      String[] parts = args.split(", ");
      if (parts.length != 4) {
         this.logger.log("Используйте формат: имя, x, y, z");
      }

      try {
         String wayName = parts[0];
         int x = Integer.parseInt(parts[1]);
         int y = Integer.parseInt(parts[2]);
         int z = Integer.parseInt(parts[3]);
         waypoints.put(wayName, new Vector3i(x, y, z));
         this.logger.log("Вейпоинт " + wayName + " добавлен");
      } catch (NumberFormatException var8) {
         this.logger.log("Координаты должны быть числами");
      }

   }

   private void removeGPS(Parameters parameters) {
      String pars = parameters.collectMessage(1);
      if (waypoints.keySet().contains(pars)) {
         this.logger.log("Вейпоинт " + pars + " удален");
         waypoints.remove(pars);
      } else {
         this.logger.log("Данного вейпоинта не существует");
      }

   }

   public String name() {
      return "way";
   }

   public String description() {
      return "Устанавливает метки к указаным координатам";
   }
}
