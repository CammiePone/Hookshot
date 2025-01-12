package dev.cammiescorner.hookshot.util.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import dev.cammiescorner.hookshot.registry.HookshotRecipeSerializers;
import dev.cammiescorner.hookshot.registry.HookshotRegistries;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import dev.cammiescorner.hookshot.util.UpgradesHelper;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class HookshotSmithingUpgradeRecipe implements SmithingRecipe {

    private final ItemStack result = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
    private final ResourceLocation id;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final HookshotUpgrade upgrade;

    public HookshotSmithingUpgradeRecipe(ResourceLocation id, Ingredient template, Ingredient base, Ingredient addition, HookshotUpgrade upgrade) {
        this.id = id;
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
    public boolean matches(Container container, Level level) {
        return this.template.test(container.getItem(SmithingMenu.TEMPLATE_SLOT)) && this.base.test(container.getItem(SmithingMenu.BASE_SLOT)) && this.addition.test(container.getItem(SmithingMenu.ADDITIONAL_SLOT));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        ItemStack result = container.getItem(SmithingMenu.BASE_SLOT).copy();

        if (!UpgradesHelper.addUpgrade(result, this.upgrade)) {
            return ItemStack.EMPTY;
        }

        return result;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HookshotRecipeSerializers.UPGRADE_SMITHING.get();
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::isEmpty);
    }

    public static class Serializer implements RecipeSerializer<HookshotSmithingUpgradeRecipe> {

        @Override
        public HookshotSmithingUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            var template = Ingredient.fromJson(GsonHelper.getNonNull(serializedRecipe, "template"));
            var base = Ingredient.fromJson(GsonHelper.getNonNull(serializedRecipe, "base"), false);
            var addition = Ingredient.fromJson(GsonHelper.getNonNull(serializedRecipe, "addition"));
            var upgrade = HookshotSmithingUpgradeRecipe.upgradeFromJson(serializedRecipe, "apply_upgrade");

            return new HookshotSmithingUpgradeRecipe(recipeId, template, base, addition, upgrade);
        }

        @Override
        public HookshotSmithingUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var template = Ingredient.fromNetwork(buffer);
            var base = Ingredient.fromNetwork(buffer);
            var addition = Ingredient.fromNetwork(buffer);
            var upgrade = buffer.readById(HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY);

            return new HookshotSmithingUpgradeRecipe(recipeId, template, base, addition, upgrade);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, HookshotSmithingUpgradeRecipe recipe) {
            recipe.template.toNetwork(buffer);
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeId(HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY, recipe.upgrade);
        }
    }

    public static HookshotUpgrade upgradeFromJson(JsonObject jsonObject, String key) {
        String id = GsonHelper.getAsString(jsonObject, key);
        return HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY.getOptional(ResourceLocation.tryParse(id)).orElseThrow(() -> new JsonSyntaxException("Unknown upgrade '%s'".formatted(id)));
    }
}
