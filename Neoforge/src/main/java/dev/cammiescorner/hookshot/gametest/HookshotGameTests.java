package dev.cammiescorner.hookshot.gametest;

import dev.cammiescorner.hookshot.common.attachment.HookOwnerAttachment;
import dev.cammiescorner.hookshot.common.entity.HookshotEntity;
import dev.cammiescorner.hookshot.common.registry.HookshotDataComponents;
import dev.cammiescorner.hookshot.common.registry.HookshotItems;
import dev.cammiescorner.hookshot.common.registry.HookshotRecipeSerializers;
import dev.cammiescorner.hookshot.common.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.common.util.UpgradesHelper;
import dev.cammiescorner.hookshot.common.util.recipe.HookshotShapelessRecipe;
import dev.cammiescorner.hookshot.common.util.recipe.HookshotSmithingUpgradeRecipe;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.List;

/**
 * GameTest suite for the NeoForge port. Run with {@code ./gradlew runGameTestServer},
 * or in the dev client via {@code /test runall}. See TESTING.md.
 *
 * <p>{@link GameTestHolder} both auto-registers the class and puts the tests in the
 * {@code hookshot} namespace (which {@code neoforge.enabledGameTestNamespaces} filters on);
 * {@link PrefixGameTestTemplate} stops the class name being prepended to template paths.
 */
@GameTestHolder(dev.cammiescorner.hookshot.Hookshot.MOD_ID)
@PrefixGameTestTemplate(false)
public class HookshotGameTests {

    private static final String PLATFORM = "platform";

    // ------------------------------------------------------------------
    // upgrade data component logic
    // ------------------------------------------------------------------

    @GameTest(template = PLATFORM)
    public static void upgrades_can_be_added_and_removed(GameTestHelper helper) {
        ItemStack stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());

        helper.assertTrue(UpgradesHelper.getUpgrades(stack).isEmpty(), "new hookshot should have no upgrades");
        helper.assertTrue(UpgradesHelper.addUpgrade(stack, HookshotUpgrades.AQUATIC.get()), "adding a new upgrade should succeed");
        helper.assertTrue(UpgradesHelper.hasUpgrade(stack, HookshotUpgrades.AQUATIC.get()), "upgrade should be present after adding");
        helper.assertTrue(!UpgradesHelper.addUpgrade(stack, HookshotUpgrades.AQUATIC.get()), "adding a duplicate upgrade should fail");
        helper.assertTrue(UpgradesHelper.getUpgrades(stack).size() == 1, "duplicate add should not grow the list");
        helper.assertTrue(UpgradesHelper.removeUpgrade(stack, HookshotUpgrades.AQUATIC.get()), "removing an existing upgrade should succeed");
        helper.assertTrue(UpgradesHelper.getUpgrades(stack).isEmpty(), "upgrade should be gone after removal");
        helper.assertTrue(!UpgradesHelper.removeUpgrade(stack, HookshotUpgrades.AQUATIC.get()), "removing a missing upgrade should fail");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void upgrades_set_rarity(GameTestHelper helper) {
        ItemStack stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());

        UpgradesHelper.addUpgrade(stack, HookshotUpgrades.SPEED.get());
        helper.assertTrue(stack.get(DataComponents.RARITY) == Rarity.RARE, "upgraded hookshot should be rare");

        UpgradesHelper.removeUpgrade(stack, HookshotUpgrades.SPEED.get());
        helper.assertTrue(stack.get(DataComponents.RARITY) == Rarity.COMMON, "un-upgraded hookshot should be common again");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void durability_upgrade_multiplies_max_damage(GameTestHelper helper) {
        ItemStack stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
        int base = stack.getMaxDamage();
        helper.assertTrue(base == 512, "default durability should be 512 (config default), was " + base);

        UpgradesHelper.addUpgrade(stack, HookshotUpgrades.DURABILITY.get());
        helper.assertTrue(stack.getMaxDamage() == 1024, "durability upgrade should double max damage, was " + stack.getMaxDamage());

        UpgradesHelper.removeUpgrade(stack, HookshotUpgrades.DURABILITY.get());
        helper.assertTrue(stack.getMaxDamage() == 512, "removing the upgrade should restore max damage, was " + stack.getMaxDamage());

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void unknown_upgrade_ids_are_ignored(GameTestHelper helper) {
        ItemStack stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
        stack.set(HookshotDataComponents.UPGRADES.get(), List.of(
                ResourceLocation.parse("hookshot:does_not_exist"),
                HookshotUpgrades.AQUATIC.get().getId()
        ));

        var upgrades = UpgradesHelper.getUpgrades(stack);
        helper.assertTrue(upgrades.size() == 1, "unknown upgrade ids should be skipped, got " + upgrades.size());
        helper.assertTrue(upgrades.contains(HookshotUpgrades.AQUATIC.get()), "known upgrade should survive");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void tooltip_lists_upgrade_names(GameTestHelper helper) {
        ItemStack stack = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
        UpgradesHelper.addUpgrade(stack, HookshotUpgrades.AQUATIC.get());
        UpgradesHelper.addUpgrade(stack, HookshotUpgrades.BLEED.get());

        var tooltip = UpgradesHelper.getUpgradesTooltip(stack);
        helper.assertTrue(tooltip.size() == 2, "tooltip should list both upgrades");
        helper.assertTrue(HookshotUpgrades.AQUATIC.get().getTranslationId().equals("hookshot.upgrade.hookshot.aquatic"),
                "unexpected translation key: " + HookshotUpgrades.AQUATIC.get().getTranslationId());

        helper.succeed();
    }

    // ------------------------------------------------------------------
    // recipes
    // ------------------------------------------------------------------

    private static Recipe<?> recipe(GameTestHelper helper, String id) {
        var holder = helper.getLevel().getRecipeManager().byKey(ResourceLocation.parse(id));
        helper.assertTrue(holder.isPresent(), "recipe " + id + " should be loaded");
        return holder.orElseThrow().value();
    }

    @GameTest(template = PLATFORM)
    public static void all_mod_recipes_are_loaded(GameTestHelper helper) {
        String[] colors = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
        for (String color : colors) {
            recipe(helper, "hookshot:" + color + "_hookshot_dying");
        }

        String[] upgrades = {"aquatic", "automatic", "bleed", "durability", "enderic", "range", "speed"};
        for (String upgrade : upgrades) {
            recipe(helper, "hookshot:" + upgrade + "_upgrade_from_smithing");
        }

        recipe(helper, "hookshot:white_hookshot");
        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void smithing_recipe_applies_upgrade(GameTestHelper helper) {
        var recipe = (HookshotSmithingUpgradeRecipe) recipe(helper, "hookshot:range_upgrade_from_smithing");

        var input = new SmithingRecipeInput(ItemStack.EMPTY, new ItemStack(HookshotItems.WHITE_HOOKSHOT.get()), new ItemStack(Items.CHAIN));
        helper.assertTrue(recipe.matches(input, helper.getLevel()), "recipe should match hookshot + chain with empty template");

        ItemStack result = recipe.assemble(input, helper.getLevel().registryAccess());
        helper.assertTrue(!result.isEmpty(), "assemble should produce a result");
        helper.assertTrue(UpgradesHelper.hasUpgrade(result, HookshotUpgrades.RANGE.get()), "result should have the range upgrade");
        helper.assertTrue(result.get(DataComponents.RARITY) == Rarity.RARE, "result should be rare");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void smithing_recipe_rejects_duplicate_upgrade(GameTestHelper helper) {
        var recipe = (HookshotSmithingUpgradeRecipe) recipe(helper, "hookshot:range_upgrade_from_smithing");

        ItemStack base = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
        UpgradesHelper.addUpgrade(base, HookshotUpgrades.RANGE.get());

        var input = new SmithingRecipeInput(ItemStack.EMPTY, base, new ItemStack(Items.CHAIN));
        helper.assertTrue(recipe.assemble(input, helper.getLevel().registryAccess()).isEmpty(),
                "applying the same upgrade twice should produce no result");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void smithing_recipe_preserves_damage_and_upgrades(GameTestHelper helper) {
        var recipe = (HookshotSmithingUpgradeRecipe) recipe(helper, "hookshot:speed_upgrade_from_smithing");

        ItemStack base = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
        UpgradesHelper.addUpgrade(base, HookshotUpgrades.RANGE.get());
        base.setDamageValue(100);

        ItemStack result = recipe.assemble(new SmithingRecipeInput(ItemStack.EMPTY, base, new ItemStack(Items.CHAIN)), helper.getLevel().registryAccess());
        helper.assertTrue(result.getDamageValue() == 100, "damage should carry over, was " + result.getDamageValue());
        helper.assertTrue(UpgradesHelper.hasUpgrade(result, HookshotUpgrades.RANGE.get()), "existing upgrade should carry over");
        helper.assertTrue(UpgradesHelper.hasUpgrade(result, HookshotUpgrades.SPEED.get()), "new upgrade should be applied");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void dye_recipe_recolors_and_keeps_components(GameTestHelper helper) {
        var recipe = (HookshotShapelessRecipe) recipe(helper, "hookshot:red_hookshot_dying");

        ItemStack hook = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
        UpgradesHelper.addUpgrade(hook, HookshotUpgrades.AQUATIC.get());
        hook.setDamageValue(42);

        CraftingInput input = CraftingInput.of(2, 1, List.of(hook, new ItemStack(Items.RED_DYE)));
        helper.assertTrue(recipe.matches(input, helper.getLevel()), "hookshot + red dye should match");

        ItemStack result = recipe.assemble(input, helper.getLevel().registryAccess());
        helper.assertTrue(result.is(HookshotItems.RED_HOOKSHOT.get()), "result should be the red hookshot");
        helper.assertTrue(result.getDamageValue() == 42, "damage should carry over, was " + result.getDamageValue());
        helper.assertTrue(UpgradesHelper.hasUpgrade(result, HookshotUpgrades.AQUATIC.get()), "upgrades should carry over");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void dye_recipe_requires_a_hookshot(GameTestHelper helper) {
        var recipe = (CraftingRecipe) recipe(helper, "hookshot:red_hookshot_dying");

        CraftingInput input = CraftingInput.of(1, 1, List.of(new ItemStack(Items.RED_DYE)));
        helper.assertTrue(!recipe.matches(input, helper.getLevel()), "dye alone should not match");

        helper.succeed();
    }

    // ------------------------------------------------------------------
    // recipe codec round-trips (the part most likely to break on version bumps)
    // ------------------------------------------------------------------

    /**
     * Note: tag ingredients are resolved to concrete item lists when sent over the network
     * (vanilla {@code Ingredient.CONTENTS_STREAM_CODEC} behaviour), so the decoded recipe is
     * compared semantically rather than for identical serialized form.
     */
    private static <T extends Recipe<?>> T roundTrip(GameTestHelper helper, RecipeSerializer<T> serializer, T original) {
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
        serializer.streamCodec().encode(buf, original);
        T decoded = serializer.streamCodec().decode(buf);
        helper.assertTrue(buf.readableBytes() == 0, "decoder should consume the whole buffer");
        return decoded;
    }

    @GameTest(template = PLATFORM)
    public static void smithing_recipe_network_round_trip(GameTestHelper helper) {
        var recipe = (HookshotSmithingUpgradeRecipe) recipe(helper, "hookshot:enderic_upgrade_from_smithing");
        @SuppressWarnings("unchecked")
        var serializer = (RecipeSerializer<HookshotSmithingUpgradeRecipe>) HookshotRecipeSerializers.UPGRADE_SMITHING.get();
        var decoded = roundTrip(helper, serializer, recipe);

        helper.assertTrue(decoded.getUpgrade() == recipe.getUpgrade(), "upgrade should survive the network round-trip");
        helper.assertTrue(decoded.getTemplate().isEmpty() == recipe.getTemplate().isEmpty(), "template emptiness should survive");

        ItemStack hookshot = new ItemStack(HookshotItems.LIME_HOOKSHOT.get());
        ItemStack addition = recipe.getAddition().getItems()[0].copy(); // whatever item this upgrade uses
        helper.assertTrue(decoded.getBase().test(hookshot), "decoded base ingredient should still accept hookshots");
        helper.assertTrue(decoded.getAddition().test(addition), "decoded addition ingredient should still accept its item");

        var input = new SmithingRecipeInput(ItemStack.EMPTY, hookshot, addition);
        helper.assertTrue(decoded.matches(input, helper.getLevel()), "decoded recipe should still match");
        helper.assertTrue(ItemStack.matches(
                        recipe.assemble(input, helper.getLevel().registryAccess()),
                        decoded.assemble(input, helper.getLevel().registryAccess())),
                "original and decoded recipes should assemble the same result");

        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void dye_recipe_network_round_trip(GameTestHelper helper) {
        var recipe = (HookshotShapelessRecipe) recipe(helper, "hookshot:blue_hookshot_dying");
        @SuppressWarnings("unchecked")
        var serializer = (RecipeSerializer<HookshotShapelessRecipe>) HookshotRecipeSerializers.DYE_CRAFTING_SHAPELESS.get();
        var decoded = roundTrip(helper, serializer, recipe);

        helper.assertTrue(decoded.getGroup().equals(recipe.getGroup()), "group should survive the network round-trip");
        helper.assertTrue(decoded.category() == recipe.category(), "category should survive the network round-trip");
        helper.assertTrue(ItemStack.matches(decoded.getResultItem(null), recipe.getResultItem(null)), "result should survive the network round-trip");

        ItemStack hook = new ItemStack(HookshotItems.WHITE_HOOKSHOT.get());
        UpgradesHelper.addUpgrade(hook, HookshotUpgrades.BLEED.get());
        CraftingInput input = CraftingInput.of(2, 1, List.of(hook, new ItemStack(Items.BLUE_DYE)));

        helper.assertTrue(decoded.matches(input, helper.getLevel()), "decoded recipe should still match hookshot + blue dye");
        helper.assertTrue(ItemStack.matches(
                        recipe.assemble(input, helper.getLevel().registryAccess()),
                        decoded.assemble(input, helper.getLevel().registryAccess())),
                "original and decoded recipes should assemble the same result");

        helper.succeed();
    }

    // ------------------------------------------------------------------
    // hook entity behaviour
    // ------------------------------------------------------------------

    private static Player mockPlayerHoldingHookshot(GameTestHelper helper, Vec3 relativePos, float yaw) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Vec3 pos = helper.absoluteVec(relativePos);
        player.moveTo(pos.x, pos.y, pos.z, yaw, 0F);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(HookshotItems.WHITE_HOOKSHOT.get()));
        return player;
    }

    private static List<HookshotEntity> hooksAround(GameTestHelper helper, Player player) {
        // tests run concurrently on neighbouring platforms — filter by owner so a test
        // never sees (or discards!) another test's hook entity
        return helper.getLevel().getEntitiesOfClass(HookshotEntity.class,
                new AABB(player.blockPosition()).inflate(32), hook -> hook.getOwner() == player);
    }

    @GameTest(template = PLATFORM)
    public static void using_hookshot_spawns_hook_and_sets_attachment(GameTestHelper helper) {
        Player player = mockPlayerHoldingHookshot(helper, new Vec3(4.5, 1.0, 4.5), 0F);

        var result = player.getMainHandItem().getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(result.getResult().consumesAction(), "using the hookshot should succeed");
        helper.assertTrue(HookOwnerAttachment.get(player).hasHook(), "hasHook attachment should be set");

        List<HookshotEntity> hooks = hooksAround(helper, player);
        helper.assertTrue(hooks.size() == 1, "exactly one hook entity should spawn, found " + hooks.size());

        hooks.forEach(HookshotEntity::discard);
        player.discard();
        helper.succeed();
    }

    @GameTest(template = PLATFORM)
    public static void hook_discards_when_owner_stops_holding(GameTestHelper helper) {
        Player player = mockPlayerHoldingHookshot(helper, new Vec3(4.5, 1.0, 4.5), 0F);
        player.getMainHandItem().getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        List<HookshotEntity> hooks = hooksAround(helper, player);
        helper.assertTrue(hooks.size() == 1, "hook should spawn");
        HookshotEntity hook = hooks.get(0);

        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        helper.succeedWhen(() -> {
            helper.assertTrue(hook.isRemoved(), "hook should discard once the owner stops holding a hookshot");
            helper.assertTrue(!HookOwnerAttachment.get(player).hasHook(), "hasHook should reset when the hook is removed");
            player.discard();
        });
    }

    @GameTest(template = PLATFORM, timeoutTicks = 200)
    public static void hook_attaches_to_wall_and_pulls_owner(GameTestHelper helper) {
        // 3x3 wall at x=7, facing the player
        for (int z = 3; z <= 5; z++) {
            for (int y = 1; y <= 3; y++) {
                helper.setBlock(new BlockPos(7, y, z), Blocks.SMOOTH_STONE);
            }
        }

        // facing +x (east) is yaw -90
        Player player = mockPlayerHoldingHookshot(helper, new Vec3(1.5, 1.0, 4.5), -90F);
        player.getMainHandItem().getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        helper.succeedWhen(() -> {
            helper.assertTrue(player.getDeltaMovement().x > 0.02,
                    "player should be pulled toward the wall, delta=" + player.getDeltaMovement());
            hooksAround(helper, player).forEach(HookshotEntity::discard);
            player.discard();
        });
    }
}
