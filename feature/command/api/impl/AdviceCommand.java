package ru.luminar.feature.command.api.impl;

import java.util.Iterator;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.CommandProvider;
import ru.luminar.feature.command.api.interfaces.CommandWithAdvice;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.Parameters;

public class AdviceCommand implements Command {
   final CommandProvider commandProvider;
   final Logger logger;

   public AdviceCommand(CommandProvider commandProvider, Logger logger) {
      this.commandProvider = commandProvider;
      this.logger = logger;
   }

   public void execute(Parameters parameters) {
      String commandName = (String)parameters.asString(0).orElseThrow(() -> {
         return new CommandException("Вы не указали имя команды");
      });
      Command command = this.commandProvider.command(commandName);
      if (!(command instanceof CommandWithAdvice)) {
         throw new CommandException(TextFormatting.RED + "К данной команде нет советов!");
      } else {
         CommandWithAdvice commandWithAdvice = (CommandWithAdvice)command;
         this.logger.log(TextFormatting.WHITE + "Пример использования команды:");
         Iterator var5 = commandWithAdvice.adviceMessage().iterator();

         while(var5.hasNext()) {
            String advice = (String)var5.next();
            this.logger.log(TextFormatting.GRAY + advice);
         }

      }
   }

   public String name() {
      return "advice";
   }

   public String description() {
      return "null";
   }
}
