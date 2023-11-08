/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import com.notenoughmail.precisionprospecting.config.PrecProsConfig;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.world.item.Tier;

public class ProsHammerItem extends ProspectorItem {
    public ProsHammerItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties, 15, PrecProsConfig.prosHammerPrimaryRadius, PrecProsConfig.prosHammerSecondaryRadius, PrecProsConfig.prosHammerDisplacement, TFCTags.Blocks.PROSPECTABLE);
    }
}
