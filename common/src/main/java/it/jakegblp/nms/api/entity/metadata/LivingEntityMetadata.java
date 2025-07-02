package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.NMSAdapter;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;


@Getter
@Setter
@SuppressWarnings("unchecked")
public class LivingEntityMetadata extends EntityMetadata implements LivingEntityMetadataView {

    public static final LivingEntityMetadataView DEFAULT_LIVING_ENTITY_METADATA = new LivingEntityMetadataView() {};

    public LivingEntityMetadata() {
        super(DEFAULT_LIVING_ENTITY_METADATA.getEntityDataItems());
    }

    public LivingEntityMetadata(HashMap<BaseProperty, AbstractEntityData<? extends Entity, ?, ?>> entityDataMap) {
        super(entityDataMap);
    }

    public LivingEntityMetadata(
            @NotNull EntityFlags entityFlags,
            int airTicks,
            @Nullable Component customName,
            boolean customNameVisible,
            boolean silent,
            boolean noGravity,
            @NotNull Pose pose,
            int ticksFrozen,
            @NotNull HandStates handStates,
            float health,
            int potionEffectColor,
            boolean potionEffectAmbient,
            int arrowCount,
            int beeStingerCount,
            @Nullable BlockVector sleepingBedLocation
    ) {
        super(entityFlags, airTicks, customName, customNameVisible, silent, noGravity, pose, ticksFrozen);
        entityDataMap.put(LivingEntityMetadataView.Property.HAND_STATES, EntityData.simpleDefault(8, handStates, DEFAULT_LIVING_ENTITY_METADATA.getHandStates()));
        entityDataMap.put(LivingEntityMetadataView.Property.HEALTH, EntityData.simpleDefault(9, health, DEFAULT_LIVING_ENTITY_METADATA.getHealth()));
        entityDataMap.put(LivingEntityMetadataView.Property.POTION_EFFECT_COLOR, EntityData.simpleDefault(10, potionEffectColor, DEFAULT_LIVING_ENTITY_METADATA.getPotionEffectColor()));
        entityDataMap.put(LivingEntityMetadataView.Property.POTION_EFFECT_AMBIENT, EntityData.simpleDefault(11, potionEffectAmbient, DEFAULT_LIVING_ENTITY_METADATA.isPotionEffectAmbient()));
        entityDataMap.put(LivingEntityMetadataView.Property.ARROW_COUNT, EntityData.simpleDefault(12, arrowCount, DEFAULT_LIVING_ENTITY_METADATA.getArrowCount()));
        entityDataMap.put(LivingEntityMetadataView.Property.BEE_STINGER_COUNT, EntityData.simpleDefault(13, beeStingerCount, DEFAULT_LIVING_ENTITY_METADATA.getBeeStingerCount()));
        entityDataMap.put(LivingEntityMetadataView.Property.SLEEPING_BED_LOCATION, EntityData.rawDefaultOptional(14, sleepingBedLocation, DEFAULT_LIVING_ENTITY_METADATA.getSleepingBedLocation().orElse(null), BlockVector.class, NMSAdapter.nmsAdapter.getNMSBlockVectorClass()));
    }

    public @NotNull HandStates getHandStates() {
        return getProperty(LivingEntityMetadataView.Property.HAND_STATES);
    }

    public void setHandStates(HandStates handStates) {
        setProperty(LivingEntityMetadataView.Property.HAND_STATES, handStates);
    }

    public float getHealth() {
        return getProperty(LivingEntityMetadataView.Property.HEALTH);
    }

    public void setHealth(float health) {
        setProperty(LivingEntityMetadataView.Property.HEALTH, health);
    }

    public Color getColor() {
        return Color.fromRGB(getProperty(LivingEntityMetadataView.Property.POTION_EFFECT_COLOR));
    }

    public void setColor(Color color) {
        setProperty(LivingEntityMetadataView.Property.POTION_EFFECT_COLOR, color.asRGB());
    }

    public boolean isPotionEffectAmbient() {
        return getProperty(LivingEntityMetadataView.Property.POTION_EFFECT_AMBIENT);
    }

    public void setPotionEffectAmbient(boolean ambient) {
        setProperty(LivingEntityMetadataView.Property.POTION_EFFECT_AMBIENT, ambient);
    }

    public int getArrowCount() {
        return getProperty(LivingEntityMetadataView.Property.ARROW_COUNT);
    }

    public void setArrowCount(int count) {
        setProperty(LivingEntityMetadataView.Property.ARROW_COUNT, count);
    }

    public int getBeeStingerCount() {
        return getProperty(LivingEntityMetadataView.Property.BEE_STINGER_COUNT);
    }

    public void setBeeStingerCount(int count) {
        setProperty(LivingEntityMetadataView.Property.BEE_STINGER_COUNT, count);
    }

    public @NotNull Optional<BlockVector> getSleepingBedLocation() {
        return Optional.ofNullable(getProperty(LivingEntityMetadataView.Property.SLEEPING_BED_LOCATION));
    }

    public void setSleepingBedLocation(@Nullable BlockVector location) {
        setProperty(LivingEntityMetadataView.Property.SLEEPING_BED_LOCATION, location);
    }

}