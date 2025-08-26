package ru.luminar.feature.command.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.text.TextFormatting;
import ru.luminar.feature.command.api.interfaces.AdviceCommandFactory;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.CommandDispatcher;
import ru.luminar.feature.command.api.interfaces.CommandProvider;
import ru.luminar.feature.command.api.interfaces.CommandWithAdvice;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.MultiNamedCommand;
import ru.luminar.feature.command.api.interfaces.ParametersFactory;
import ru.luminar.feature.command.api.interfaces.Prefix;

public class StandaloneCommandDispatcher implements CommandDispatcher, CommandProvider {
   private static final String DELIMITER = " ";
   final Prefix prefix;
   final ParametersFactory parametersFactory;
   final Logger logger;
   final Map<String, Command> aliasToCommandMap;

   public StandaloneCommandDispatcher(List<Command> commands, AdviceCommandFactory adviceCommandFactory, Prefix prefix, ParametersFactory parametersFactory, Logger logger) {
      this.prefix = prefix;
      this.parametersFactory = parametersFactory;
      this.logger = logger;
      this.aliasToCommandMap = this.commandsToAliasToCommandMap(this.commandsWithAdviceCommand(adviceCommandFactory, commands));
   }

   public DispatchResult dispatch(String message) {
      String prefix = this.prefix.get();
      if (!message.startsWith(prefix)) {
         return DispatchResult.NOT_DISPATCHED;
      } else {
         String commandPart = message.substring(prefix.length());
         String[] split = commandPart.split(" ", 2);
         String commandName = split[0];
         if (!this.aliasToCommandMap.containsKey(commandName)) {
            return DispatchResult.NOT_DISPATCHED;
         } else {
            Command command = (Command)this.aliasToCommandMap.get(commandName);

            try {
               String parameters = split.length > 1 ? split[1] : "";
               command.execute(this.parametersFactory.createParameters(parameters, " "));
            } catch (Exception var8) {
               this.handleCommandException(var8, command);
            }

            return DispatchResult.DISPATCHED;
         }
      }
   }

   public Command command(String alias) {
      return (Command)this.aliasToCommandMap.get(alias);
   }

   private Map<String, Command> commandsToAliasToCommandMap(List<Command> commands) {
      return (Map)commands.stream().flatMap(this::commandToWrappedCommandStream).collect(Collectors.toMap(StandaloneCommandDispatcher.FlatMapCommand::getAlias, StandaloneCommandDispatcher.FlatMapCommand::getCommand, (existing, replacement) -> {
         return replacement;
      }));
   }

   private Stream<StandaloneCommandDispatcher.FlatMapCommand> commandToWrappedCommandStream(Command command) {
      Stream<StandaloneCommandDispatcher.FlatMapCommand> wrappedCommandStream = Stream.of(new StandaloneCommandDispatcher.FlatMapCommand(command.name(), command));
      if (command instanceof MultiNamedCommand) {
         MultiNamedCommand multiNamedCommand = (MultiNamedCommand)command;
         return Stream.concat(wrappedCommandStream, multiNamedCommand.aliases().stream().map((alias) -> {
            return new StandaloneCommandDispatcher.FlatMapCommand(alias, command);
         }));
      } else {
         return wrappedCommandStream;
      }
   }

   private void handleCommandException(Exception e, Command command) {
      if (e instanceof CommandException) {
         CommandException cmde = (CommandException)e;
         this.logger.log(cmde.message);
      } else {
         String errorMessage = "";
         if (e instanceof NullPointerException) {
            errorMessage = "Такой команды не существует.";
         }

         if (errorMessage.isEmpty()) {
            return;
         }

         this.logger.log(errorMessage);
      }

      if (command instanceof CommandWithAdvice) {
         this.logger.log(String.format(TextFormatting.GRAY + "Введите %sadvice %s", this.prefix.get(), command.name()));
      }

   }

   private String extractParametersFromMessage(String message, String[] split) {
      return message.substring((split.length != 1 ? " ".length() : 0) + split[0].length());
   }

   private List<Command> commandsWithAdviceCommand(AdviceCommandFactory adviceCommandFactory, List<Command> commands) {
      List<Command> commandsWithAdvices = new ArrayList(commands);
      commandsWithAdvices.add(adviceCommandFactory.adviceCommand(this));
      return commandsWithAdvices;
   }

   private static class FlatMapCommand {
      String alias;
      Command command;

      public String getAlias() {
         return this.alias;
      }

      public Command getCommand() {
         return this.command;
      }

      public FlatMapCommand(String al, Command cmd) {
         this.alias = al;
         this.command = cmd;
      }
   }
}
