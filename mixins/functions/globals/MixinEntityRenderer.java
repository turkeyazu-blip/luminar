package ru.luminar.mixins.functions.globals;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({EntityRenderer.class})
public abstract class MixinEntityRenderer<T extends Entity> {
}
