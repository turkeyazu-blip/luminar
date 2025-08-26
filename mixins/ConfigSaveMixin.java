package ru.luminar.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.utils.globalsapi.ServerAPI;
import ru.luminar.feature.managers.config.cloud.CloudAPI;
import ru.luminar.feature.managers.config.cloud.CloudConfigManager;
import ru.luminar.feature.managers.config.files.ConfigManager;
import ru.luminar.utils.draggables.DragManager;

@Mixin(
   targets = {"net.minecraft.client.main.Main$2"}
)
public abstract class ConfigSaveMixin {
   @Inject(
      method = {"run()V"},
      at = {@At("HEAD")}
   )
   private void onGameThreadStart(CallbackInfo ci) {
      DragManager.save();
      Luminar.instance.cloudConfigFileManager.updateFromLuminar();
      if (CloudAPI.connected) {
         CloudConfigManager.getInstance().autoSaveOnExit();
      }

      if (!Luminar.useCloudCfg) {
         ConfigManager.getInstance().saveLastConfigLocally();
      }

      if (ServerAPI.socket.getChannel().isOpen()) {
         ServerAPI.finish();
      }

   }
}
