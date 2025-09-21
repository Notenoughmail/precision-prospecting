package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import io.github.notenoughmail.precisionprospecting.items.ProspectorType;
import net.dries007.tfc.util.Metal;

import java.util.function.Consumer;

import static io.github.notenoughmail.precisionprospecting.PatchouliProvider.*;

public class PatchiData {

    static void generate(Consumer<EncodableContent> ret) {

        final Category category = new Category(
                PrecisionProspecting.ID,
                "Precision Prospecting",
                "All about the tools added by Precision Prospecting",
                c -> c.icon(PrecProsItems.TOOLS.get(Metal.BRONZE).get(ProspectorType.MIN_PROS)).sortNum = 30
        );
        final Entry
                prosHammer = new PatchouliProvider.Entry(
                        ProspectorType.PROS_HAMMER.toString(),
                        "Prospector's Hammer",
                        category,
                        e -> {
                            e.sortNum = 1;
                            e.readByDefault = true;
                            e.icon(PrecProsItems.TOOLS.get(Metal.STEEL).get(ProspectorType.PROS_HAMMER))
                                    .spotlight(false, PrecProsItems.TOOLS.get(Metal.BISMUTH_BRONZE).get(ProspectorType.PROS_HAMMER))
                                        .title("Prospector's Hammer")
                                        .text("The ").thing("Prospector's Hammer").text(" is a near copy of its pick-y brother, with the notable exception of a smaller range, only searching the 13x13x13 area centered on the clicked block.")
                                    .recipe("precisionprospecting:knapping/prospector_hammer_head_mold")
                                        .knap()
                                        .text("A mold for the tool head can be ").link("getting_started/pottery", null, "knapped").text(" from some clay, as shown above, which can be used to ").link("getting_started/finding_ores", "casting", "cast").text(" low tier metals.")
                                    .recipe("precisionprospecting:anvil/metal/prospector_hammer/wrought_iron")
                                        .anvil()
                                        .text("A ").thing("Prospector's Hammer Head").text(" can also be ").link("mechanics/anvils", "working", "smithed").text(" out of a ").thing("double ingot").text(" of any tool metal. Craft the head with a stick to make a Prospector's Hammer")
                            ;
                        }
                ),
                prosDrill = new Entry(
                        ProspectorType.PROS_DRILL.toString(),
                        "Prospector's Drill",
                        category,
                        e -> {
                            e.sortNum = 2;
                            e.readByDefault = true;
                            e.icon(PrecProsItems.TOOLS.get(Metal.STEEL).get(ProspectorType.PROS_DRILL))
                                    .spotlight(false, PrecProsItems.TOOLS.get(Metal.BISMUTH_BRONZE).get(ProspectorType.PROS_DRILL))
                                        .title("Prospector's Drill")
                                        .text("The ").thing("Prospector's Drill").text(" is unique from other prospecting tools in that its scanning area is not a cube and not centered on the clicked block.")
                                        .br()
                                        .text("The scan area is a 7x25x7 box where the square faces are parallel to the clicked face. Additionally, the scan area is shifted away from the clicked face by 10 blocks.")
                                    .recipe("precisionprospecting:knapping/prospector_drill_head_mold")
                                        .knap()
                                        .text("A mold for the tool head can be ").link("getting_started/pottery", null, "knapped").text(" from clay, as seen above, which can be used to ").link("getting_started/finding_ores", "casting", "cast").text(" low tier metals.")
                                    .recipe("precisionprospecting:anvil/metal/prospector_drill/blue_steel")
                                        .anvil()
                                        .text("A ").thing("Prospector's Drill Head").text(" can also be ").link("mechanics/anvils", "working", "smithed").text(" out of a ").thing("tuyere").text(" of any tool metal.")
                                    .recipe("precisionprospecting:crafting/metal/prospector_drill/bismuth_bronze")
                                        .craft()
                                        .text("The head can then be crafted with two sticks to make a Prospector's Drill")
                            ;
                        }
                ),
                minPros = new Entry(
                        ProspectorType.MIN_PROS.toString(),
                        "Mineral Prospector",
                        category,
                        e -> {
                            e.sortNum = 3;
                            e.readByDefault = true;
                            e.icon(PrecProsItems.TOOLS.get(Metal.STEEL).get(ProspectorType.MIN_PROS))
                                    .spotlight(false, PrecProsItems.TOOLS.get(Metal.BLACK_BRONZE).get(ProspectorType.MIN_PROS))
                                        .title("Mineral Prospector")
                                        .text("The ").thing("Mineral Prospector").text(" scans a large range, a massive 45x45x45 centered on the clicked block, but only for non-metal ores.")
                                    .recipe("precisionprospecting:knapping/mineral_prospector_head_mold")
                                        .knap()
                                        .text("A mold for the tool head can be ").link("getting_started/pottery", null, "knapped").text(" from clay, as seen above, which can be used to ").link("getting_started/finding_ores", "casting", "cast").text(" low tier metals.")
                                    .recipe("precisionprospecting:anvil/metal/mineral_prospector/black_steel")
                                        .anvil()
                                        .text("A ").thing("Mineral Prospector Head").text(" can also be ").link("mechanics/anvils", "working", "smithed").text(" out of a ").thing("double ingot").text(" of any tool metal. Craft the head with a stick to make a Mineral Prospector.")
                            ;
                        }
                ),
                intro = new Entry(
                        PrecisionProspecting.ID,
                        "Precision Prospecting",
                        category,
                        e -> {
                            e.sortNum = 0;
                            e.readByDefault = true;
                            e.icon(PrecProsItems.TOOL_HEADS.get(Metal.BLUE_STEEL).get(ProspectorType.MIN_PROS))
                                    .textPage()
                                        .text("Do you have difficulty finding ores with a regular ").link("mechanics/prospecting", "propick", "Prospector's Pick").text("? Perhaps something here can be of help...")
                                        .br()
                                        .text("Precision Prospecting adds three additional prospecting tools, each with different ranges, which can be used to enhance your prospecting expeditions.")
                                    .textPage()
                                        .li().text("The ").link(prosHammer, null, "Prospector's Hammer").text(" is similar to a regular ").thing("Prospector's Pick").text(", just with a smaller range.")
                                        .li().text("The ").link(prosDrill, null, "Prospector's Drill").text(" is a directional prospector which has a non-cubic, off-center scanning area")
                                        .li().text("The ").link(minPros, null, "Mineral Prospector").text(" has a larger scanning area, but only detects ").thing("non-metal ores").text(".")
                            ;
                        }
                );
        for (Metal metal : PrecProsItems.TOOL_METALS) {
            prosHammer.recipeMapping(PrecProsItems.TOOLS.get(metal).get(ProspectorType.PROS_HAMMER), 1);
            prosDrill.recipeMapping(PrecProsItems.TOOLS.get(metal).get(ProspectorType.PROS_DRILL), 1);
            minPros.recipeMapping(PrecProsItems.TOOLS.get(metal).get(ProspectorType.MIN_PROS), 1);
        }
        ret.accept(category);
        ret.accept(intro);
        ret.accept(prosDrill);
        ret.accept(prosHammer);
        ret.accept(minPros);
    }
}
