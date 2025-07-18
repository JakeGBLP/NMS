package it.jakegblp.nms.api.entity.metadata.entity.areaeffectcloud;

import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import it.jakegblp.nms.api.entity.metadata.entity.EntityFlags;
import it.jakegblp.nms.api.entity.metadata.entity.EntityMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AreaEffectCloudMetadata extends EntityMetadata implements AreaEffectCloudMetadataView, Cloneable {

    @Nullable
    protected Float radius;
    @Nullable
    protected Boolean ignoreRadius;
    @Nullable
    protected Integer particle;

    public AreaEffectCloudMetadata(AreaEffectCloudMetadataView view) {
        super(view);
        this.radius = view.getRadius();
        this.ignoreRadius = view.getIgnoreRadius();
        this.particle = view.getParticle();
    }

    public AreaEffectCloudMetadata(
            @Nullable EntityFlags entityFlags,
            @Nullable Integer airTicks,
            @Nullable Component customName,
            @Nullable Boolean customNameVisible,
            @Nullable Boolean silent,
            @Nullable Boolean noGravity,
            @Nullable Pose pose,
            @Nullable Integer ticksFrozen,
            @Nullable Float radius,
            @Nullable Boolean ignoreRadius,
            @Nullable Integer particle
    ) {
        super(entityFlags, airTicks, customName, customNameVisible, silent, noGravity, pose, ticksFrozen);
        this.radius = radius;
        this.ignoreRadius = ignoreRadius;
        this.particle = particle;
    }

    @Override
    public <T> void set(MetadataKey<? extends Entity, T> key, @Nullable T value) {
        switch (key.getIndex()) {
            case 8 -> radius = (Float) value;
            case 9 -> ignoreRadius = (Boolean) value;
            case 10 -> particle = (Integer) value;
            default -> super.set(key, value);
        }
    }

    @Override
    public AreaEffectCloudMetadata clone() {
        return new AreaEffectCloudMetadata(this);
    }
}
