package ru.luminar.feature.command.api.impl;

public class CommandException extends RuntimeException {
   String message;

   public CommandException(String s) {
      this.message = s;
   }
}
