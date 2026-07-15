package dev.cammiescorner.hookshot.common.data;

import dev.cammiescorner.hookshot.Hookshot;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class HookshotItemTags {

    public static final TagKey<Item> HOOKSHOTS = TagKey.create(Registries.ITEM, Hookshot.id("hookshots"));
    public static final TagKey<Item> HOOKSHOT_REPAIR_ITEMS = TagKey.create(Registries.ITEM, Hookshot.id("hookshot_repair_items"));

}
