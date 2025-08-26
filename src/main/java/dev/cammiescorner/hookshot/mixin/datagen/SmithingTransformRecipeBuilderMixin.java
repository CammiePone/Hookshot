package dev.cammiescorner.hookshot.mixin.datagen;

import dev.cammiescorner.hookshot.common.util.datagen.HookshotSmithingUpgradeRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SmithingTransformRecipeBuilder.class)
public class SmithingTransformRecipeBuilderMixin {

    @ModifyArg(method = "save(Ljava/util/function/Consumer;Lnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private Object onSave(Object original) {
        if((Object)this instanceof HookshotSmithingUpgradeRecipeBuilder self) {
            return new HookshotSmithingUpgradeRecipeBuilder.Result((SmithingTransformRecipeBuilder.Result) original, self.getUpgrade());
        }

        return original;
    }
}
