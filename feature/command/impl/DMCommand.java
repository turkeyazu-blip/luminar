package ru.luminar.feature.command.impl;

import net.minecraft.util.text.TextFormatting;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.Parameters;
import ru.luminar.feature.functions.impl.utils.dm.DMAPI;
import ru.luminar.mixins.accessors.MinecraftAccessor;
import ru.luminar.utils.client.IMinecraft;

public class DMCommand implements Command {
   private final Logger logger;

   public DMCommand(Logger logger) {
      this.logger = logger;
   }

   public void execute(Parameters parameters) {
      String subCommand = (String)parameters.asString(0).orElse("");
      byte var4 = -1;
      switch(subCommand.hashCode()) {
      case -690213213:
         if (subCommand.equals("register")) {
            var4 = 1;
         }
         break;
      case 3526536:
         if (subCommand.equals("send")) {
            var4 = 0;
         }
         break;
      case 3540994:
         if (subCommand.equals("stop")) {
            var4 = 2;
         }
      }

      switch(var4) {
      case 0:
         if (((String)parameters.asString(2).orElse("")).isEmpty()) {
            this.logger.log(TextFormatting.RED + "Используйте: .dm send <ник> <сообщение>");
            return;
         }

         String recipient = (String)parameters.asString(1).get();
         String message = String.join(" ", parameters.collectMessage(2));
         if (recipient.equals(((MinecraftAccessor)IMinecraft.mc).getSession().func_111285_a())) {
            this.logger.log("Вы не можете отправить сообщение самому себе!");
            return;
         }

         DMAPI.sendDM(recipient, message);
         this.logger.log(TextFormatting.GREEN + "Личное сообщение отправлено!");
         break;
      case 1:
         DMAPI.init();
         DMAPI.register();
         break;
      case 2:
         DMAPI.finish();
         break;
      default:
         this.logger.log(TextFormatting.GREEN + "Доступные команды:");
         this.logger.log(TextFormatting.YELLOW + ".dm register " + TextFormatting.RESET + "- Подключиться к серверу");
         this.logger.log(TextFormatting.YELLOW + ".dm stop " + TextFormatting.RESET + "- Отключиться от сервера");
         this.logger.log(TextFormatting.YELLOW + ".dm send <ник> <сообщение> " + TextFormatting.RESET + "- Отправить личное сообщение");
      }

   }

   public String name() {
      return "dm";
   }

   public String description() {
      return "Система личных сообщений";
   }
}
