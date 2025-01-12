package dev.cammiescorner.hookshot.mixin.datagen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.hookshot.util.datagen.HookshotShapelessRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ShapelessRecipeBuilder.class)
public class ShapelessRecipeBuilderMixin {

    @WrapOperation(method = "save", at = @At(value = "NEW", target = "(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/item/Item;ILjava/lang/String;Lnet/minecraft/world/item/crafting/CraftingBookCategory;Ljava/util/List;Lnet/minecraft/advancements/Advancement$Builder;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/data/recipes/ShapelessRecipeBuilder$Result;"))
    private ShapelessRecipeBuilder.Result onSave(ResourceLocation id, Item result, int count, String group, CraftingBookCategory category, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId, Operation<ShapelessRecipeBuilder.Result> original) {
        if((Object) this instanceof HookshotShapelessRecipeBuilder self) {
            return new HookshotShapelessRecipeBuilder.Result(id, result, count, group, category, ingredients, self.getHookshot(), advancement, advancementId);
        }

        return original.call(id, result, count, group, category, ingredients, advancement, advancementId);
    }

}
