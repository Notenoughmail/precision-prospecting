/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import com.notenoughmail.precisionprospecting.Tags;
import com.notenoughmail.precisionprospecting.config.PrecProsConfig;
import net.minecraft.world.item.Tier;

public class MineralProspectorItem extends ProspectorItem{

    public MineralProspectorItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties, 40, PrecProsConfig.minProsPrimaryRadius, PrecProsConfig.minProsSecondaryRadius, PrecProsConfig.minProsDisplacement, Tags.Blocks.PROSPECTABLE_MINERAL);
    }
}
