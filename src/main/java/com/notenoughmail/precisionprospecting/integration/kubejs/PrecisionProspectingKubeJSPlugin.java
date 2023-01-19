package com.notenoughmail.precisionprospecting.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;

public class PrecisionProspectingKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        RegistryObjectBuilderTypes.ITEM.addType("prospector_hammer", ProsHammerItemBuilder.class, ProsHammerItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("prospector_drill", ProsDrillItemBuilder.class, ProsDrillItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("custom_prospector", CustomProsItemBuilder.class, CustomProsItemBuilder::new);
    }
}
