package dev.cammiescorner.hookshot.datagen.common;

import dev.cammiescorner.hookshot.data.HookshotItemTags;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import dev.cammiescorner.hookshot.registry.HookshotRegistries;
import dev.cammiescorner.hookshot.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import dev.cammiescorner.hookshot.util.datagen.HookshotShapelessRecipeBuilder;
import dev.cammiescorner.hookshot.util.datagen.HookshotSmithingUpgradeRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class HookshotRecipeProvider extends FabricRecipeProvider {

    public HookshotRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HookshotItems.WHITE_HOOKSHOT.get())
                .pattern(" A ")
                .pattern("ICI")
                .pattern("RPR")
                .define('A', ItemTags.ARROWS)
                .define('I', ConventionalItemTags.IRON_INGOTS)
                .define('C', Blocks.CHAIN)
                .define('R', ConventionalItemTags.REDSTONE_DUSTS)
                .define('P', Blocks.PISTON)
                .group(HookshotItems.WHITE_HOOKSHOT.getId().toString())
                .unlockedBy("has_piston", has(Blocks.PISTON))
                .save(exporter);

        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.WHITE_HOOKSHOT).group(HookshotItems.WHITE_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.WHITE_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.WHITE_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.WHITE_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.ORANGE_HOOKSHOT).group(HookshotItems.ORANGE_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.ORANGE_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.ORANGE_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.ORANGE_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.MAGENTA_HOOKSHOT).group(HookshotItems.MAGENTA_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.MAGENTA_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.MAGENTA_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.MAGENTA_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.LIGHT_BLUE_HOOKSHOT).group(HookshotItems.LIGHT_BLUE_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.LIGHT_BLUE_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.LIGHT_BLUE_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.LIGHT_BLUE_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.YELLOW_HOOKSHOT).group(HookshotItems.YELLOW_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.YELLOW_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.YELLOW_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.YELLOW_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.LIME_HOOKSHOT).group(HookshotItems.LIME_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.LIME_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.LIME_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.LIME_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.PINK_HOOKSHOT).group(HookshotItems.PINK_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.PINK_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.PINK_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.PINK_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.GRAY_HOOKSHOT).group(HookshotItems.GRAY_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.GRAY_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.GRAY_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.GRAY_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.LIGHT_GRAY_HOOKSHOT).group(HookshotItems.LIGHT_GRAY_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.LIGHT_GRAY_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.LIGHT_GRAY_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.LIGHT_GRAY_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.CYAN_HOOKSHOT).group(HookshotItems.CYAN_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.CYAN_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.CYAN_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.CYAN_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.PURPLE_HOOKSHOT).group(HookshotItems.PURPLE_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.PURPLE_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.PURPLE_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.PURPLE_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.BLUE_HOOKSHOT).group(HookshotItems.BLUE_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.BLUE_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.BLUE_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.BLUE_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.BROWN_HOOKSHOT).group(HookshotItems.BROWN_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.BROWN_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.BROWN_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.BROWN_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.GREEN_HOOKSHOT).group(HookshotItems.GREEN_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.GREEN_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.GREEN_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.GREEN_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.RED_HOOKSHOT).group(HookshotItems.RED_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.RED_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.RED_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.RED_HOOKSHOT.get()).withSuffix("_dying"));
        HookshotShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Ingredient.of(HookshotItemTags.HOOKSHOTS), HookshotItems.BLACK_HOOKSHOT).group(HookshotItems.BLACK_HOOKSHOT.getId().toString()).requires(Ingredient.of(ConventionalItemTags.BLACK_DYES)).unlockedBy("has_dye", has(ConventionalItemTags.BLACK_DYES)).save(exporter, RecipeBuilder.getDefaultRecipeId(HookshotItems.BLACK_HOOKSHOT.get()).withSuffix("_dying"));

        hookshotUpgrade(exporter, Items.PRISMARINE_SHARD, HookshotUpgrades.AQUATIC);
        hookshotUpgrade(exporter, Blocks.OBSERVER, HookshotUpgrades.AUTOMATIC);
        hookshotUpgrade(exporter, ItemTags.ARROWS, HookshotUpgrades.BLEED);
        hookshotUpgrade(exporter, Blocks.OBSIDIAN, HookshotUpgrades.DURABILITY);
        hookshotUpgrade(exporter, Items.ENDER_EYE, HookshotUpgrades.ENDERIC);
        hookshotUpgrade(exporter, Blocks.PISTON, HookshotUpgrades.QUICK);
        hookshotUpgrade(exporter, Blocks.CHAIN, HookshotUpgrades.RANGE);
    }

    @SuppressWarnings("DataFlowIssue")
    public static void hookshotUpgrade(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike addition, Supplier<? extends HookshotUpgrade> upgrade) {
        HookshotSmithingUpgradeRecipeBuilder.upgrade(Ingredient.EMPTY, Ingredient.of(HookshotItemTags.HOOKSHOTS), Ingredient.of(addition), RecipeCategory.MISC, upgrade.get()).unlocks("has_upgrade_item", has(addition)).save(finishedRecipeConsumer, HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY.getKey(upgrade.get()).withSuffix("_upgrade_from_smithing"));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void hookshotUpgrade(Consumer<FinishedRecipe> finishedRecipeConsumer, TagKey<Item> addition, Supplier<? extends HookshotUpgrade> upgrade) {
        HookshotSmithingUpgradeRecipeBuilder.upgrade(Ingredient.EMPTY, Ingredient.of(HookshotItemTags.HOOKSHOTS), Ingredient.of(addition), RecipeCategory.MISC, upgrade.get()).unlocks("has_upgrade_item", has(addition)).save(finishedRecipeConsumer, HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY.getKey(upgrade.get()).withSuffix("_upgrade_from_smithing"));
    }
}
