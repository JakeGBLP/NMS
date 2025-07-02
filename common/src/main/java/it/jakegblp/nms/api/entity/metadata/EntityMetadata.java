package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.NMSAdapter;
import it.jakegblp.nms.api.utils.MapUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * See: <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
@Getter
@Setter
@SuppressWarnings("unchecked")
public class EntityMetadata implements EntityMetadataView {

    public static final EntityMetadataView DEFAULT_ENTITY_METADATA = new EntityMetadataView() {};

    public final HashMap<BaseProperty, AbstractEntityData<? extends Entity, ?, ?>> entityDataMap;

    public EntityMetadata() {
        this(DEFAULT_ENTITY_METADATA.getEntityDataItems());
    }

    public EntityMetadata(HashMap<BaseProperty, AbstractEntityData<? extends Entity, ?, ?>> entityDataMap) {
        this.entityDataMap = entityDataMap;
    }

    public EntityMetadata(
            @NotNull EntityFlags entityFlags,
            int airTicks,
            @Nullable Component customName,
            boolean customNameVisible,
            boolean silent,
            boolean noGravity,
            @NotNull Pose pose,
            int ticksFrozen
    ) {
        this(MapUtils.createEntityDataMap(
                Map.entry(Property.ENTITY_FLAGS, EntityData.rawDefault(0, entityFlags, DEFAULT_ENTITY_METADATA.getEntityFlags(), EntityFlags.class, Byte.class)),
                Map.entry(Property.AIR_TICKS, EntityData.simpleDefault(1, airTicks, DEFAULT_ENTITY_METADATA.getAirTicks())),
                Map.entry(Property.CUSTOM_NAME, EntityData.rawDefaultOptional(2, customName, DEFAULT_ENTITY_METADATA.getCustomName().orElse(null), Component.class, NMSAdapter.nmsAdapter.getNMSComponentClass())),
                Map.entry(Property.CUSTOM_NAME_VISIBILITY, EntityData.simpleDefault(3, customNameVisible, DEFAULT_ENTITY_METADATA.isCustomNameVisible())),
                Map.entry(Property.SILENT, EntityData.simpleDefault(4, silent, DEFAULT_ENTITY_METADATA.isSilent())),
                Map.entry(Property.NO_GRAVITY, EntityData.simpleDefault(5, noGravity, DEFAULT_ENTITY_METADATA.hasNoGravity())),
                Map.entry(Property.POSE, EntityData.rawDefault(6, pose, DEFAULT_ENTITY_METADATA.getPose(), Pose.class, NMSAdapter.nmsAdapter.getNMSPoseClass())),
                Map.entry(Property.TICKS_FROZEN, EntityData.simpleDefault(7, ticksFrozen, DEFAULT_ENTITY_METADATA.getTicksFrozen()))
        ));
    }

    @Nullable
    public <T> T getProperty(BaseProperty property) {
        AbstractEntityData.View<T> entityData = (AbstractEntityData.View<T>) entityDataMap.get(property);
        if (entityData == null) return null;
        return entityData.getValue();
    }

    public <T> void setProperty(BaseProperty property, T value) {
        AbstractEntityData.Controller<T> entityDataController = (AbstractEntityData.Controller<T>) entityDataMap.get(property);
        if (entityDataController == null) return;
        this.entityDataMap.put(property, entityDataController.setValue(value));
    }

    @Override
    public @NotNull EntityFlags getEntityFlags() {
        return getProperty(Property.ENTITY_FLAGS);
    }

    public void setEntityFlags(EntityFlags entityFlags) {
        setProperty(Property.ENTITY_FLAGS, entityFlags);
    }

    @Override
    public int getAirTicks() {
        return getProperty(Property.AIR_TICKS);
    }

    public void setAirTicks(int airTicks) {
        setProperty(Property.AIR_TICKS, airTicks);
    }

    @Override
    public @NotNull Optional<Component> getCustomName() {
        return getProperty(Property.CUSTOM_NAME);
    }

    public void setCustomName(@Nullable Component name) {
        setProperty(Property.CUSTOM_NAME, name);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getProperty(Property.CUSTOM_NAME_VISIBILITY);
    }

    public void setCustomNameVisible(boolean visible) {
        setProperty(Property.CUSTOM_NAME_VISIBILITY, visible);
    }

    @Override
    public boolean isSilent() {
        return getProperty(Property.SILENT);
    }

    public void setSilent(boolean silent) {
        setProperty(Property.SILENT, silent);
    }

    @Override
    public boolean hasNoGravity() {
        return getProperty(Property.NO_GRAVITY);
    }

    public void setHasNoGravity(boolean noGravity) {
        setProperty(Property.NO_GRAVITY, noGravity);
    }

    @Override
    public @NotNull Pose getPose() {
        return getProperty(Property.POSE);
    }

    public void setPose(Pose pose) {
        setProperty(Property.POSE, pose);
    }

    @Override
    public int getTicksFrozen() {
        return getProperty(Property.TICKS_FROZEN);
    }

    public void setTicksFrozen(int ticks) {
        setProperty(Property.TICKS_FROZEN, ticks);
    }
}