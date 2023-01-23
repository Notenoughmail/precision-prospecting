/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import com.notenoughmail.precisionprospecting.Tags;
import net.minecraft.world.item.Tier;

public class MineralProspectorItem extends ProspectorItem{

    // Did some testing using the KubeJS integration
    // Radii of 32 causes noticeable lag when prospecting
    // Radii of 25 seems fine? but is incredibly large making it kinda useless?
    // Could make it a config option, could do the same for them all, but then Patchouli could be inaccurate
    // Curse you, Bonedoll, I gotta balance things again
    // 45's a nice number, let's go with that
    public MineralProspectorItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties, 40, 22, 22, 0, Tags.Blocks.PROSPECTABLE_MINERAL);
    }
}
