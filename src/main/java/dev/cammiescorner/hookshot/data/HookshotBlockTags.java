package dev.cammiescorner.hookshot.data;

import dev.cammiescorner.hookshot.Hookshot;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class HookshotBlockTags {
    public static final TagKey<Block> UNHOOKABLE = TagKey.create(Registries.BLOCK, Hookshot.id("unhookable"));
}
