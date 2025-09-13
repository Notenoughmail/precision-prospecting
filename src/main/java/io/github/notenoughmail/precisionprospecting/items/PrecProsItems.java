package io.github.notenoughmail.precisionprospecting.items;

import io.github.notenoughmail.precisionprospecting.PrecisionProspecting;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrecProsItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PrecisionProspecting.ID);

    public static final List<Metal> TOOL_METALS = List.of(
            Metal.BISMUTH_BRONZE,
            Metal.BLACK_BRONZE,
            Metal.BRONZE,
            Metal.COPPER,
            Metal.WROUGHT_IRON,
            Metal.STEEL,
            Metal.BLACK_STEEL,
            Metal.BLUE_STEEL,
            Metal.RED_STEEL
    );

    public static final Map<Metal, Map<ProspectorType, DeferredItem<Item>>> TOOLS = TOOL_METALS.stream().collect(Collectors.toMap(
            Function.identity(),
            metal -> Helpers.mapOf(
                    ProspectorType.class,
                    type -> ITEMS.registerItem(
                            "metal/" + type + "/" + metal.getSerializedName(),
                            props -> type.create(metal.toolTier(), props),
                            base(metal).attributes(ToolItem.productAttributes(metal.toolTier(), 0.5F, -2.8F))
            ))
    ));

    public static final Map<Metal, Map<ProspectorType, DeferredItem<Item>>> TOOL_HEADS = TOOL_METALS.stream().collect(Collectors.toMap(
            Function.identity(),
            metal -> Helpers.mapOf(
                    ProspectorType.class,
                    type -> ITEMS.registerItem(
                            "metal/" + type + "_head/" + metal.getSerializedName(),
                            Item::new,
                            base(metal)
                    )
            )
    ));

    public static final Map<ProspectorType, DeferredItem<Item>> FIRED_MOLDS = Helpers.mapOf(ProspectorType.class, type -> ITEMS.registerItem(
            "ceramic/" + type + "_head_mold",
            props -> new MoldItem(type.moldCapacity::getAsInt, TFCTags.Fluids.USABLE_IN_TOOL_HEAD_MOLD, props)
    ));

    public static final Map<ProspectorType, DeferredItem<Item>> UNFIRED_MOLDS = Helpers.mapOf(ProspectorType.class, type -> ITEMS.registerSimpleItem("ceramic/unfired_" + type + "_head_mold"));

    private static Item.Properties base(Metal metal) {
        return new Item.Properties().rarity(metal.rarity());
    }
}
