package io.github.notenoughmail.precisionprospecting.items;

import io.github.notenoughmail.precisionprospecting.PrecisionProspecting;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.world.item.Item;

import java.util.function.BiFunction;
import java.util.function.IntSupplier;

import static io.github.notenoughmail.precisionprospecting.PrecisionProspecting.CONFIG;

public enum ProspectorType {
    PROS_HAMMER((tier, props) -> new ProspectorItem(tier, props, CONFIG.prosHammerPrimaryRadius, CONFIG.prosHammerSecondaryRadius, CONFIG.prosHammerDisplacement, TFCTags.Blocks.PROSPECTABLE, 15), "prospector_hammer", CONFIG.prosHammerMoldCapacity),
    PROS_DRILL((tier, props) -> new ProspectorItem(tier, props, CONFIG.prosDrillPrimaryRadius, CONFIG.prosDrillSecondaryRadius, CONFIG.prosDrillDisplacement, TFCTags.Blocks.PROSPECTABLE, 30), "prospector_drill", CONFIG.prosDrillMoldCapcaity),
    MIN_PROS((tier, props) -> new ProspectorItem(tier, props, CONFIG.minProsPrimaryRadius, CONFIG.minProsSecondaryRadius, CONFIG.minProsDisplacement, PrecisionProspecting.PROSPECTABLE_MINERAL, 40), "mineral_prospector", CONFIG.minProsMoldCapacity);

    public static final ProspectorType[] VALUES = values();

    private final BiFunction<LevelTier, Item.Properties, Item> creator;
    private final String name;
    public final IntSupplier moldCapacity;

    ProspectorType(BiFunction<LevelTier, Item.Properties, Item> creator, String name, IntSupplier moldCapacity) {
        this.creator = creator;
        this.name = name;
        this.moldCapacity = moldCapacity;
    }

    public Item create(LevelTier tier, Item.Properties properties) {
        return creator.apply(tier, properties);
    }


    @Override
    public String toString() {
        return name;
    }
}
