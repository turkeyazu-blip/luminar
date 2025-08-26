package ru.luminar.feature.command.api.impl;

import ru.luminar.feature.command.api.interfaces.Prefix;

public class PrefixImpl implements Prefix {
   public static String prefix = ".";

   public String get() {
      return prefix;
   }
}
