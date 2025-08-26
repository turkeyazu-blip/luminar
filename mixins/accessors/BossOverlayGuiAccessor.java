package ru.luminar.mixins.accessors;

import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gui.ClientBossInfo;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BossOverlayGui.class})
public interface BossOverlayGuiAccessor {
   @Accessor("events")
   Map<UUID, ClientBossInfo> getEvents();
}
