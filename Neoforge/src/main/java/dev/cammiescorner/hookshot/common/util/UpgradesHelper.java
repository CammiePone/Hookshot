package dev.cammiescorner.hookshot.common.util;

import com.google.common.collect.ImmutableList;
import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.HookshotConfig;
import dev.cammiescorner.hookshot.common.item.HookshotItem;
import dev.cammiescorner.hookshot.common.registry.HookshotDataComponents;
import dev.cammiescorner.hookshot.common.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.common.upgrade.HookshotUpgrade;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UpgradesHelper {

    public static boolean hasUpgrade(@Nullable ItemStack stack, HookshotUpgrade upgrade) {
        return getUpgrades(stack).contains(upgrade);
    }

    public static List<HookshotUpgrade> getUpgrades(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return List.of();
        }

        List<ResourceLocation> ids = stack.getOrDefault(HookshotDataComponents.UPGRADES.get(), List.of());
        if (ids.isEmpty()) {
            return List.of();
        }

        ImmutableList.Builder<HookshotUpgrade> builder = ImmutableList.builder();
        for (ResourceLocation id : ids) {
            HookshotUpgrade upgrade = HookshotUpgrades.REGISTRY.get(id);
            if (upgrade == null) {
                Hookshot.LOGGER.error("ignoring unknown upgrade {}", id);
                continue;
            }

            builder.add(upgrade);
        }

        return builder.build();
    }

    public static void setUpgrades(ItemStack stack, List<HookshotUpgrade> upgrades) {
        List<ResourceLocation> ids = upgrades.stream().map(HookshotUpgrade::getId).toList();
        if (ids.isEmpty()) {
            stack.remove(HookshotDataComponents.UPGRADES.get());
        } else {
            stack.set(HookshotDataComponents.UPGRADES.get(), ids);
        }

        applyDerivedComponents(stack, upgrades);
    }

    public static boolean addUpgrade(ItemStack stack, HookshotUpgrade upgrade) {
        var upgrades = new ArrayList<>(getUpgrades(stack));
        if (upgrades.contains(upgrade)) {
            return false;
        }

        upgrades.add(upgrade);
        setUpgrades(stack, upgrades);

        return true;
    }

    public static boolean removeUpgrade(ItemStack stack, HookshotUpgrade upgrade) {
        var upgrades = new ArrayList<>(getUpgrades(stack));
        if (!upgrades.contains(upgrade)) {
            return false;
        }

        upgrades.remove(upgrade);
        setUpgrades(stack, upgrades);

        return true;
    }

    public static List<Component> getUpgradesTooltip(ItemStack stack) {
        return getUpgrades(stack).stream().map(HookshotUpgrade::getName).toList();
    }

    /**
     * In 1.20.1 rarity came from an {@code Item#getRarity} override and the durability upgrade
     * from an {@code ItemStack#getMaxDamage} mixin; with 1.21.1 data components both are
     * simply written onto the stack whenever the upgrade list changes.
     */
    private static void applyDerivedComponents(ItemStack stack, List<HookshotUpgrade> upgrades) {
        if (!(stack.getItem() instanceof HookshotItem)) {
            return;
        }

        stack.set(DataComponents.RARITY, upgrades.isEmpty() ? Rarity.COMMON : Rarity.RARE);

        int maxDamage = HookshotConfig.defaultDurability();
        if (upgrades.contains(HookshotUpgrades.DURABILITY.get())) {
            maxDamage = Mth.ceil(maxDamage * HookshotConfig.durabilityUpgradeMultiplier());
        }
        stack.set(DataComponents.MAX_DAMAGE, maxDamage);
    }
}
