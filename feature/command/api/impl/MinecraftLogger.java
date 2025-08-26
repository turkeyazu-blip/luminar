package ru.luminar.feature.command.api.impl;

import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.utils.client.IMinecraft;

public class MinecraftLogger implements Logger, IMinecraft {
   public void log(String message) {
      this.print(message);
   }
}
