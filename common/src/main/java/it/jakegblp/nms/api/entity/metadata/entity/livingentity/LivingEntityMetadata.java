package it.jakegblp.nms.api.entity.metadata.entity.livingentity;

import it.jakegblp.nms.api.entity.metadata.entity.EntityFlags;
import it.jakegblp.nms.api.entity.metadata.entity.EntityMetadata;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.nms.api.utils.NullabilityUtils.cloneIfNotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class LivingEntityMetadata extends EntityMetadata implements LivingEntityMetadataView, Cloneable {

    protected @Nullable HandStates handStates;
    protected @Nullable Float health;
    protected @Nullable Integer potionEffectColor;
    protected @Nullable Boolean potionEffectAmbient;
    protected @Nullable Integer arrowCount;
    protected @Nullable Integer beeStingerCount;
    protected @Nullable BlockVector sleepingBedLocation;

    public LivingEntityMetadata(LivingEntityMetadataView view) {
        super(view);
        this.handStates = cloneIfNotNull(view.getHandStates());
        this.health = view.getHealth();
        this.potionEffectColor = view.getPotionEffectColor();
        this.potionEffectAmbient = view.getPotionEffectAmbient();
        this.arrowCount = view.getArrowCount();
        this.beeStingerCount = view.getBeeStingerCount();
        this.sleepingBedLocation = cloneIfNotNull(view.getSleepingBedLocation());
    }

    public LivingEntityMetadata(
            @Nullable EntityFlags entityFlags,
            @Nullable Integer airTicks,
            @Nullable Component customName,
            @Nullable Boolean customNameVisible,
            @Nullable Boolean silent,
            @Nullable Boolean noGravity,
            @Nullable Pose pose,
            @Nullable Integer ticksFrozen,
            @Nullable HandStates handStates,
            @Nullable Float health,
            @Nullable Integer potionEffectColor,
            @Nullable Boolean potionEffectAmbient,
            @Nullable Integer arrowCount,
            @Nullable Integer beeStingerCount,
            @Nullable BlockVector sleepingBedLocation
    ) {
        super(entityFlags, airTicks, customName, customNameVisible, silent, noGravity, pose, ticksFrozen);
        this.handStates = handStates;
        this.health = health;
        this.potionEffectColor = potionEffectColor;
        this.potionEffectAmbient = potionEffectAmbient;
        this.arrowCount = arrowCount;
        this.beeStingerCount = beeStingerCount;
        this.sleepingBedLocation = sleepingBedLocation;
    }

    @Override
    public <T> void set(MetadataKey<? extends Entity, T> key, @Nullable T value) {
        switch (key.getIndex()) {
            case 8 -> handStates = (HandStates) value;
            case 9 -> health = (Float) value;
            case 10 -> potionEffectColor = (Integer) value;
            case 11 -> potionEffectAmbient = (Boolean) value;
            case 12 -> arrowCount = (Integer) value;
            case 13 -> beeStingerCount = (Integer) value;
            case 14 -> sleepingBedLocation = (BlockVector) value;
            default -> super.set(key, value);
        }
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public LivingEntityMetadata clone() {
        return new LivingEntityMetadata(this);
    }
}