package ru.luminar.feature.command.api.interfaces;

import java.util.ArrayList;
import java.util.Collection;

public interface Command {
   void execute(Parameters var1);

   String name();

   String description();

   default Collection<String> getSuggestions(String[] args, String s) {
      return new ArrayList();
   }

   default Collection<String> getCommandTypes(String[] args, String s) {
      return new ArrayList();
   }
}
