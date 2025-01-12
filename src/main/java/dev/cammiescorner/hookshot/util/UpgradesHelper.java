package dev.cammiescorner.hookshot.util;

import com.google.common.collect.ImmutableList;
import dev.cammiescorner.hookshot.Hookshot;
import dev.cammiescorner.hookshot.registry.HookshotRegistries;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import dev.upcraft.sparkweave.api.util.logging.SparkweaveLoggerFactory;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class UpgradesHelper {

    private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

    public static boolean hasUpgrade(ItemStack stack, HookshotUpgrade upgrade) {
        return getUpgrades(stack).contains(upgrade);
    }

    public static List<HookshotUpgrade> getUpgrades(ItemStack stack) {
        var tag = stack.getTagElement(Hookshot.MOD_ID);
        if (tag != null) {
            ImmutableList.Builder<HookshotUpgrade> builder = ImmutableList.builder();
            var upgrades = tag.getList("Upgrades", Tag.TAG_STRING);

            for (int i = 0; i < upgrades.size(); i++) {
                var str = upgrades.getString(i);
                var id = ResourceLocation.tryParse(str);
                if (id == null) {
                    LOGGER.error("{} is not a valid resource location", str, new IllegalArgumentException(str));
                    upgrades.remove(i--);
                    continue;
                }

                var upgrade = HookshotRegistries.HOOKSHOT_UPGRADES_REGISTRY.get(id);
                if (upgrade == null) {
                    LOGGER.error("removing unknown upgrade {}", str, new NoSuchElementException(str));
                    upgrades.remove(i--);
                    continue;
                }

                builder.add(upgrade);
            }

            return builder.build();
        }

        return List.of();
    }

    public static void setUpgrades(ItemStack stack, List<HookshotUpgrade> upgrades) {
        var list = new ListTag();
        upgrades.forEach(upgrade -> list.add(StringTag.valueOf(upgrade.getId().toString())));
        stack.getOrCreateTagElement(Hookshot.MOD_ID).put("Upgrades", list);
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
}
