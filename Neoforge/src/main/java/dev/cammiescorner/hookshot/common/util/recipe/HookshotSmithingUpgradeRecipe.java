package dev.cammiescorner.hookshot.common.util.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.hookshot.common.registry.HookshotItems;
import dev.cammiescorner.hookshot.common.registry.HookshotRecipeSerializers;
import dev.cammiescorner.hookshot.common.registry.HookshotRegistries;
import dev.cammiescorner.hookshot.common.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.common.upgrade.HookshotUpgrade;
import dev.cammiescorner.hookshot.common.util.UpgradesHelper;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class HookshotSmithingUpgradeRecipe implements SmithingRecipe {

    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final HookshotUpgrade upgrade;
    @Nullable
    private ItemStack result;

    public HookshotSmithingUpgradeRecipe(Ingredient template, Ingredient base, Ingredient addition, HookshotUpgrade upgrade) {
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.upgrade = upgrade;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return template.test(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return base.test(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return addition.test(stack);
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return this.template.test(input.template()) && this.base.test(input.base()) && this.addition.test(input.addition());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack result = input.base().copy();

        if (!UpgradesHelper.addUpgrade(result, this.upgrade)) {
            return ItemStack.EMPTY;
        }

        return result;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        if (result == null) {
            result = Util.make(new ItemStack(HookshotItems.WHITE_HOOKSHOT.get()), stack -> UpgradesHelper.addUpgrade(stack, upgrade));
        }

        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HookshotRecipeSerializers.UPGRADE_SMITHING.get();
    }

    @Override
    public boolean isIncomplete() {
        // unlike vanilla smithing, the template is allowed to be empty
        return Stream.of(this.base, this.addition).anyMatch(Ingredient::isEmpty);
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getTemplate() {
        return template;
    }

    public Ingredient getAddition() {
        return addition;
    }

    public HookshotUpgrade getUpgrade() {
        return upgrade;
    }

    public static class Serializer implements RecipeSerializer<HookshotSmithingUpgradeRecipe> {

        private static final MapCodec<HookshotSmithingUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.optionalFieldOf("template", Ingredient.EMPTY).forGetter(HookshotSmithingUpgradeRecipe::getTemplate),
                Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(HookshotSmithingUpgradeRecipe::getBase),
                Ingredient.CODEC_NONEMPTY.fieldOf("addition").forGetter(HookshotSmithingUpgradeRecipe::getAddition),
                HookshotUpgrades.REGISTRY.byNameCodec().fieldOf("apply_upgrade").forGetter(HookshotSmithingUpgradeRecipe::getUpgrade)
        ).apply(instance, HookshotSmithingUpgradeRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, HookshotSmithingUpgradeRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, HookshotSmithingUpgradeRecipe::getTemplate,
                Ingredient.CONTENTS_STREAM_CODEC, HookshotSmithingUpgradeRecipe::getBase,
                Ingredient.CONTENTS_STREAM_CODEC, HookshotSmithingUpgradeRecipe::getAddition,
                ByteBufCodecs.registry(HookshotRegistries.HOOKSHOT_UPGRADES), HookshotSmithingUpgradeRecipe::getUpgrade,
                HookshotSmithingUpgradeRecipe::new
        );

        @Override
        public MapCodec<HookshotSmithingUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HookshotSmithingUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
