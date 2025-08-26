package ru.luminar.feature.command.impl;

import net.minecraft.util.text.TextFormatting;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.Parameters;
import ru.luminar.feature.functions.impl.utils.irc.IRC;

public class IRCCommand implements Command {
   final Logger logger;

   public IRCCommand(Logger logger) {
      this.logger = logger;
   }

   public void execute(Parameters parameters) {
      if (!IRC.inited) {
         this.logger.log(TextFormatting.RED + "IRC не подключен!");
      } else {
         String firstArg = ((String)parameters.asString(0).orElse("")).toLowerCase();
         if (firstArg.isEmpty()) {
            this.logger.log(TextFormatting.RED + "Используйте: .irc <message>");
            this.logger.log(TextFormatting.GOLD + "Доступные команды:");
            this.logger.log(TextFormatting.YELLOW + ".irc mute <ник> <время_сек> <причина> - замутить игрока");
            this.logger.log(TextFormatting.YELLOW + ".irc unmute <ник> - размутить игрока");
         } else if (!firstArg.equals("mute") && !firstArg.equals("unmute") && !firstArg.equals("ban") && !firstArg.equals("unban")) {
            String message = String.join(" ", parameters.collectMessage(0));
            IRC.sendMessage(message);
            this.logger.log("Сообщение отправлено!");
         } else {
            this.handleAdminCommand(parameters, firstArg);
         }
      }
   }

   private void handleAdminCommand(Parameters parameters, String command) {
      byte var4 = -1;
      switch(command.hashCode()) {
      case -840405966:
         if (command.equals("unmute")) {
            var4 = 1;
         }
         break;
      case 3363353:
         if (command.equals("mute")) {
            var4 = 0;
         }
      }

      String target;
      switch(var4) {
      case 0:
         if (((String)parameters.asString(3).orElse("")).isEmpty()) {
            this.logger.log(TextFormatting.RED + "Используйте: .irc mute <ник> <время_сек> <причина>");
            return;
         }

         target = (String)parameters.asString(1).orElse("");
         String time = (String)parameters.asString(2).orElse("");
         String reason = String.join(" ", parameters.collectMessage(3));
         if (!time.matches("\\d+")) {
            this.logger.log(TextFormatting.RED + "Время должно быть числом (в секундах)");
            return;
         }

         IRC.sendMessage(".mute " + target + " " + time + " " + reason);
         this.logger.log(TextFormatting.GREEN + "Запрос на мут игрока " + target + " отправлен");
         break;
      case 1:
         if (((String)parameters.asString(1).orElse("")).isEmpty()) {
            this.logger.log(TextFormatting.RED + "Используйте: .irc unmute <ник>");
            return;
         }

         target = (String)parameters.asString(1).orElse("");
         IRC.sendMessage(".unmute " + target);
         this.logger.log(TextFormatting.GREEN + "Запрос на размут игрока " + target + " отправлен");
      }

   }

   public String name() {
      return "irc";
   }

   public String description() {
      return "";
   }
}
