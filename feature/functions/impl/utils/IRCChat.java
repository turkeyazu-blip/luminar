package ru.luminar.feature.functions.impl.utils;

import ru.luminar.Luminar;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.feature.functions.impl.utils.irc.IRC;
import ru.luminar.utils.client.IMinecraft;

public class IRCChat extends Function {
   public IRCChat() {
      super("IRCChat", Category.Utils);
   }

   @Subscribe
   public void onUpdate(EventUpdate e) {
      if (IRC.socket == null || IRC.socket.isClosed()) {
         IRC.init();
      }

      while(true) {
         String message;
         while((message = IRC.readMessages()) != null) {
            if (Luminar.instance.functions.island.isEnabled() && (Boolean)Island.irc.get()) {
               Island.islandManager.add(message.replace("c[ADMIN]", "[ADMIN]"), 2);
            } else {
               IMinecraft.printIRC(message);
            }
         }

         return;
      }
   }

   public void onDisable() {
      if (IRC.socket != null && !IRC.socket.isClosed()) {
         IRC.finish();
      }

      super.onDisable();
   }
}
