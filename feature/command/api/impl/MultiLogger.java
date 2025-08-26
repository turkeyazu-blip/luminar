package ru.luminar.feature.command.api.impl;

import java.util.Iterator;
import java.util.List;
import ru.luminar.feature.command.api.interfaces.Logger;

public class MultiLogger implements Logger {
   final List<Logger> loggers;

   public <E> MultiLogger(List<Logger> es) {
      this.loggers = es;
   }

   public void log(String message) {
      Iterator var2 = this.loggers.iterator();

      while(var2.hasNext()) {
         Logger logger = (Logger)var2.next();
         logger.log(message);
      }

   }
}
