package ru.luminar.feature.command.api.impl;

import ru.luminar.feature.command.api.interfaces.Parameters;
import ru.luminar.feature.command.api.interfaces.ParametersFactory;

public class ParametersFactoryImpl implements ParametersFactory {
   public Parameters createParameters(String message, String delimiter) {
      return new ParametersImpl(message.split(delimiter));
   }
}
