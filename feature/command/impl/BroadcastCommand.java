package ru.luminar.feature.command.impl;

import java.util.Optional;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.Parameters;
import ru.luminar.feature.functions.impl.utils.irc.IRC;

public class BroadcastCommand implements Command {
   final Logger logger;

   public BroadcastCommand(Logger logger) {
      this.logger = logger;
   }

   public void execute(Parameters parameters) {
      if (!IRC.inited) {
         this.logger.log(TextFormatting.RED + "IRC не подключен!");
      } else {
         String param = (String)parameters.asString(0).orElse("");
         byte var4 = -1;
         switch(param.hashCode()) {
         case 106079:
            if (param.equals("key")) {
               var4 = 0;
            }
         default:
            switch(var4) {
            case 0:
               Optional var10000 = parameters.asString(1);
               IRC.sendMessage(".broadcast key " + (String)var10000.orElse(""));
               break;
            default:
               if (((String)parameters.asString(0).orElse("")).isEmpty()) {
                  this.logger.log(TextFormatting.RED + "Используйте: .broadcast <message>");
                  return;
               }

               String message = String.join(" ", parameters.collectMessage(0));
               IRC.sendMessage(".broadcast " + message);
            }

         }
      }
   }

   public String name() {
      return "broadcast";
   }

   public String description() {
      return "";
   }
}
