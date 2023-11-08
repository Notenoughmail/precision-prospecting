/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import com.notenoughmail.precisionprospecting.config.PrecProsConfig;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.world.item.Tier;

public class ProsDrillItem extends ProspectorItem {
    public ProsDrillItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties, 30, PrecProsConfig.prosDrillPrimaryRadius, PrecProsConfig.prosDrillSecondaryRadius, PrecProsConfig.prosDrillDisplacement, TFCTags.Blocks.PROSPECTABLE);
    }
}
