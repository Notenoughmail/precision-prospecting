/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the License at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package com.notenoughmail.precisionprospecting.items;

import net.minecraft.world.item.Tier;

public class ProsHammerItem extends ProspectorItem {
    public ProsHammerItem(Tier tier, float attackDamage, float attackSpeed, Properties properties, String mod) {
        super(tier, attackDamage, attackSpeed, properties, 15, 6, 6, 0, mod);
    }
}
