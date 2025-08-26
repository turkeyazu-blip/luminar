package ru.luminar.feature.command.api.impl;

import ru.luminar.feature.command.api.interfaces.AdviceCommandFactory;
import ru.luminar.feature.command.api.interfaces.CommandProvider;
import ru.luminar.feature.command.api.interfaces.Logger;

public class AdviceCommandFactoryImpl implements AdviceCommandFactory {
   final Logger logger;

   public AdviceCommandFactoryImpl(Logger logger) {
      this.logger = logger;
   }

   public AdviceCommand adviceCommand(CommandProvider commandProvider) {
      return new AdviceCommand(commandProvider, this.logger);
   }
}
