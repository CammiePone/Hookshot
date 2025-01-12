package dev.cammiescorner.hookshot.datagen.client;

import dev.cammiescorner.hookshot.data.HookshotBlockTags;
import dev.cammiescorner.hookshot.data.HookshotDamageTypes;
import dev.cammiescorner.hookshot.data.HookshotItemTags;
import dev.cammiescorner.hookshot.registry.HookshotEntities;
import dev.cammiescorner.hookshot.registry.HookshotItems;
import dev.cammiescorner.hookshot.registry.HookshotSoundEvents;
import dev.cammiescorner.hookshot.registry.HookshotUpgrades;
import dev.cammiescorner.hookshot.upgrade.HookshotUpgrade;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class HookshotEnglishLanguageProvider extends FabricLanguageProvider {

    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public HookshotEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, Language.DEFAULT);
        this.registriesFuture = registriesFuture;
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
        upgrade(translationBuilder, HookshotUpgrades.QUICK, "Quick");
        upgrade(translationBuilder, HookshotUpgrades.RANGE, "Range");

        sound(translationBuilder, HookshotSoundEvents.HOOKSHOT_REEL, "Hookshot Reel");

        translationBuilder.add("config.hookshot.useClassicHookshotLogic", "Use Old Hookshot Fire/Retract Logic");
        translationBuilder.add("config.hookshot.unhookableBlacklist", "Unhookable Tag Acts Like Blacklist");
        translationBuilder.add("config.hookshot.hookshotCancelsFallDamage", "Hookshot Cancels Fall Damage");
        translationBuilder.add("config.hookshot.quickUpgradeAffectsPullSpeed", "Quick Upgrade Affects Hookshot Pull Speed");
        translationBuilder.add("config.hookshot.defaultMaxRange", "Unmodified Max Hookshot Range");
        translationBuilder.add("config.hookshot.defaultMaxSpeed", "Unmodified Max Hookshot Speed");
        translationBuilder.add("config.hookshot.rangeMultiplier", "Range Upgrade Multiplier");
        translationBuilder.add("config.hookshot.quickMultiplier", "Quick Upgrade Multiplier");
        translationBuilder.add("config.hookshot.durabilityMultiplier", "Durability Upgrade Multiplier");
        translationBuilder.add("config.hookshot.defaultMaxDurability", "Hookshot Durability");
        translationBuilder.add("config.hookshot.hookshotCooldown", "Cooldown after using Hookshot");
    }

    private void sound(TranslationBuilder translationBuilder, Supplier<? extends SoundEvent> sound, String translation) {
        translationBuilder.add(Util.makeDescriptionId("subtitles", sound.get().getLocation()), translation);
    }

    private void upgrade(TranslationBuilder translationBuilder, Supplier<? extends HookshotUpgrade> upgrade, String translation) {
        translationBuilder.add(upgrade.get().getTranslationId(), translation);
    }

    private void tag(TranslationBuilder builder, TagKey<?> tag, String translation) {
        var registryName = tag.registry().location().toShortLanguageKey().replace('/', '.');
        var tagName = Util.makeDescriptionId("tag." + registryName, tag.location());
        builder.add(tagName, translation);
    }

    private void damageType(TranslationBuilder builder, ResourceKey<DamageType> typeKey, String defaultTranslation, @Nullable String killedByTranslation, @Nullable String killedWithItemTranslation) {
        registriesFuture.thenAccept(registries -> {
            var damageTypes = registries.lookupOrThrow(Registries.DAMAGE_TYPE);
            var type = damageTypes.getOrThrow(typeKey).value();

            if(type.deathMessageType() != DeathMessageType.DEFAULT) {
                throw new IllegalArgumentException("Death message type not currently supported: " + type.deathMessageType());
            }

            var translationKey = "death.attack." + type.msgId();
            builder.add(translationKey, defaultTranslation);
            builder.add(translationKey + ".player", Objects.requireNonNullElse(killedByTranslation, defaultTranslation));
            builder.add(translationKey + ".item", Objects.requireNonNullElse(killedWithItemTranslation, defaultTranslation));
        });
    }
}
