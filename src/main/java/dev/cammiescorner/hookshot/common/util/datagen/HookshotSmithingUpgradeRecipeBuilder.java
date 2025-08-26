package dev.cammiescorner.hookshot.common.util.datagen;

import com.google.gson.JsonObject;
import dev.cammiescorner.hookshot.common.registry.HookshotRecipeSerializers;
import dev.cammiescorner.hookshot.common.upgrade.HookshotUpgrade;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class HookshotSmithingUpgradeRecipeBuilder extends SmithingTransformRecipeBuilder {

    private final HookshotUpgrade upgrade;

    protected HookshotSmithingUpgradeRecipeBuilder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, HookshotUpgrade upgrade) {
        super(HookshotRecipeSerializers.UPGRADE_SMITHING.get(), template, base, addition, category, Items.AIR);
        this.upgrade = upgrade;
    }

    public HookshotUpgrade getUpgrade() {
        return upgrade;
    }

    public static HookshotSmithingUpgradeRecipeBuilder upgrade(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, HookshotUpgrade upgrade) {
        return new HookshotSmithingUpgradeRecipeBuilder(template, base, addition, category, upgrade);
    }

    public record Result(SmithingTransformRecipeBuilder.Result delegate, HookshotUpgrade upgrade) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            delegate.serializeRecipeData(json);
            json.remove("result");
            json.addProperty("apply_upgrade", upgrade().getId().toString());
        }

        @Override
        public ResourceLocation getId() {
            return delegate.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return delegate.getType();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return delegate.serializeAdvancement();
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return delegate.getAdvancementId();
        }
    }
}
