package ru.luminar.feature.managers;

import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import ru.luminar.Luminar;
import ru.luminar.events.Event;
import ru.luminar.events.impl.other.EventWorldChange;
import ru.luminar.events.impl.packet.PacketEvent;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.impl.render.Render2DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Functions;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.feature.functions.impl.utils.AutoSwap;
import ru.luminar.feature.functions.impl.utils.ElytraSwap;
import ru.luminar.feature.functions.impl.utils.dm.DMAPI;
import ru.luminar.feature.managers.config.cloud.CloudAPI;
import ru.luminar.ui.GuiScreen;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.math.MathUtil;

public class EventManager implements IMinecraft {
   @Subscribe
   public void onEvent(Event event) {
      if (event instanceof EventWorldChange) {
      }

      if (event instanceof Render2DEvent) {
         Render2DEvent e = (Render2DEvent)event;
         if (Luminar.instance.functions.island.isEnabled()) {
            Island.islandManager.draw(e.getMatrixStack());
         }

         GuiScreen.whiteAnim = MathUtil.lerp(GuiScreen.whiteAnim, GuiScreen.whiteTheme ? 0.0F : 1.0F, 10.0F);
         GuiScreen.whiteAnim2 = MathUtil.lerp(GuiScreen.whiteAnim2, GuiScreen.whiteTheme ? 1.0F : 2.0F, 10.0F);
      }

      String sender;
      if (event instanceof EventUpdate) {
         if (Luminar.useCloudCfg && !CloudAPI.connected) {
            CloudAPI.init();
         }

         Functions functions = Luminar.instance.functions;
         if (GuiScreen.locked) {
            functions.functions.forEach((f) -> {
               if (f instanceof ElytraSwap || f instanceof AutoSwap) {
                  f.setPendingRemoval(true);
                  if (f.isEnabled()) {
                     f.toggle();
                  }
               }

            });
         } else {
            functions.functions.forEach((f) -> {
               if ((f instanceof ElytraSwap || f instanceof AutoSwap) && f.isPendingRemoval()) {
                  f.setPendingRemoval(false);
               }

            });
         }

         if (mc.field_71462_r instanceof GuiScreen) {
            ((GuiScreen)mc.field_71462_r).restoreFunctions();
         }

         String dm;
         if (DMAPI.registered) {
            while((dm = DMAPI.checkMessages()) != null) {
               IMinecraft.printDM("§d[DM] §f" + dm);
               sender = DMAPI.extractSenderFromDM(dm);
               if (sender != null) {
                  Island.islandManager.add("Новое сообщение от " + sender + "!", 3);
               }
            }
         }
      }

      if (event instanceof PacketEvent) {
         PacketEvent e = (PacketEvent)event;
         IPacket var11 = e.getPacket();
         if (var11 instanceof SChatPacket) {
            SChatPacket c = (SChatPacket)var11;
            sender = c.func_148915_c().getString().toLowerCase();
            if ((Boolean)Island.auc.get() && sender.contains("у вас купили")) {
               String[] parts = sender.split("у вас купили | за \\$| на /ah");
               String itemName = parts[1].trim();
               String price = parts[2].replaceAll("[^0-9]", "").trim();
               Island.islandManager.add("У вас купили " + itemName + " за $" + price + "!", 3);
            }
         }
      }

   }
}
