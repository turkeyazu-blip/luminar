package ru.luminar.utils.client;

import java.util.UUID;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import net.minecraft.network.play.server.SUpdateBossInfoPacket.Operation;
import net.minecraft.util.StringUtils;

public class ClientUtil {
   public static boolean pvp;
   private static UUID uuid;

   public static boolean isPvp() {
      return pvp;
   }

   public static void updateBossInfo(SUpdateBossInfoPacket packet) {
      if (packet.func_186902_b() == Operation.ADD) {
         if (StringUtils.func_76338_a(packet.func_186907_c().getString()).toLowerCase().contains("pvp")) {
            pvp = true;
            uuid = packet.func_186908_a();
         }
      } else if (packet.func_186902_b() == Operation.REMOVE && packet.func_186908_a().equals(uuid)) {
         pvp = false;
      }

   }
}
