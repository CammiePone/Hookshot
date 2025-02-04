package dev.cammiescorner.hookshot.datagen.client;

import dev.cammiescorner.hookshot.data.HookshotBlockTags;
import dev.cammiescorner.hookshot.data.HookshotDamageTypes;
import dev.cammiescorner.hookshot.data.HookshotItemTags;
import dev.cammiescorner.hookshot.registry.HookshotEntities;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import dev.cammiescorner.hookshot.registry.HookshotSoundEvents;
import dev.cammiescorner.hookshot.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import dev.upcraft.sparkweave.api.datagen.SparkweaveLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.locale.Language;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class HookshotEnglishLanguageProvider extends SparkweaveLanguageProvider {

    public HookshotEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, Language.DEFAULT);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(HookshotItems.WHITE_HOOKSHOT.get(), "White Hookshot");
        translationBuilder.add(HookshotItems.ORANGE_HOOKSHOT.get(), "Orange Hookshot");
        translationBuilder.add(HookshotItems.MAGENTA_HOOKSHOT.get(), "Magenta Hookshot");
        translationBuilder.add(HookshotItems.LIGHT_BLUE_HOOKSHOT.get(), "Light Blue Hookshot");
        translationBuilder.add(HookshotItems.YELLOW_HOOKSHOT.get(), "Yellow Hookshot");
        translationBuilder.add(HookshotItems.LIME_HOOKSHOT.get(), "Lime Hookshot");
        translationBuilder.add(HookshotItems.PINK_HOOKSHOT.get(), "Pink Hookshot");
        translationBuilder.add(HookshotItems.GRAY_HOOKSHOT.get(), "Gray Hookshot");
        translationBuilder.add(HookshotItems.LIGHT_GRAY_HOOKSHOT.get(), "Light Gray Hookshot");
        translationBuilder.add(HookshotItems.CYAN_HOOKSHOT.get(), "Cyan Hookshot");
        translationBuilder.add(HookshotItems.PURPLE_HOOKSHOT.get(), "Purple Hookshot");
        translationBuilder.add(HookshotItems.BLUE_HOOKSHOT.get(), "Blue Hookshot");
        translationBuilder.add(HookshotItems.BROWN_HOOKSHOT.get(), "Brown Hookshot");
        translationBuilder.add(HookshotItems.GREEN_HOOKSHOT.get(), "Green Hookshot");
        translationBuilder.add(HookshotItems.RED_HOOKSHOT.get(), "Red Hookshot");
        translationBuilder.add(HookshotItems.BLACK_HOOKSHOT.get(), "Black Hookshot");

        tag(translationBuilder, HookshotBlockTags.UNHOOKABLE, "Unable to attach hookshot");

        tag(translationBuilder, HookshotItemTags.HOOKSHOTS, "Hookshots");
        tag(translationBuilder, HookshotItemTags.HOOKSHOT_REPAIR_ITEMS, "Hookshot Repair Items");

        damageType(translationBuilder, HookshotDamageTypes.BLEEDING, "%s bled to death", "%s bled to death whilst fighting %s", null);

        translationBuilder.add(HookshotEntities.HOOKSHOT.get(), "Hookshot");

        upgrade(translationBuilder, HookshotUpgrades.AQUATIC, "Aquatic");
        upgrade(translationBuilder, HookshotUpgrades.AUTOMATIC, "Automatic");
        upgrade(translationBuilder, HookshotUpgrades.BLEED, "Bleed");
        upgrade(translationBuilder, HookshotUpgrades.DURABILITY, "Durability");
        upgrade(translationBuilder, HookshotUpgrades.ENDERIC, "Enderic");
        upgrade(translationBuilder, HookshotUpgrades.RANGE, "Range");
        upgrade(translationBuilder, HookshotUpgrades.SPEED, "Speed");

        sound(translationBuilder, HookshotSoundEvents.HOOKSHOT_REEL, "Hookshot Reel");

        translationBuilder.add("config.hookshot.use_classic_hookshot_logic", "Use Old Hookshot Fire/Retract Logic");
        translationBuilder.add("config.hookshot.hookshot_cancels_fall_damage", "Hookshot Cancels Fall Damage");
        translationBuilder.add("config.hookshot.hookshot_cooldown", "Cooldown after using Hookshot");
        translationBuilder.add("config.hookshot.hookshot_affects_vehicle", "Hookshot affects Player's vehicle");
        translationBuilder.add("config.hookshot.default_durability", "Hookshot Durability");
        translationBuilder.add("config.hookshot.default_range", "Default Hookshot Range");
        translationBuilder.add("config.hookshot.default_speed", "Default Hookshot Speed");
        translationBuilder.add("config.hookshot.durability_upgrade_multiplier", "Durability Upgrade Multiplier");
        translationBuilder.add("config.hookshot.range_upgrade_multiplier", "Range Upgrade Multiplier");
        translationBuilder.add("config.hookshot.speed_upgrade_multiplier", "Speed Upgrade Multiplier");
    }

    private void upgrade(TranslationBuilder translationBuilder, Supplier<? extends HookshotUpgrade> upgrade, String translation) {
        translationBuilder.add(upgrade.get().getTranslationId(), translation);
    }
}
