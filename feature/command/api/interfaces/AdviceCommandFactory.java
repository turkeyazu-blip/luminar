package ru.luminar.feature.command.api.interfaces;

import ru.luminar.feature.command.api.impl.AdviceCommand;

public interface AdviceCommandFactory {
   AdviceCommand adviceCommand(CommandProvider var1);
}
