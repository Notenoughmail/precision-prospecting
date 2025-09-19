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
                                    .pages(
                                            new TextPage().text("Woah!")
                                    );
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
                                    .pages(
                                            new TextPage()
                                                    .text("Woah!")
                                    );
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
                                    .pages(
                                            new TextPage()
                                                    .text("Woah!")
                                    );
                        }
                ),
                intro = new Entry(
                        "introduction",
                        "Introduction",
                        category,
                        e -> {
                            e.sortNum = 0;
                            e.readByDefault = true;
                            e.icon(PrecProsItems.TOOL_HEADS.get(Metal.BLUE_STEEL).get(ProspectorType.MIN_PROS))
                                    .pages(
                                            new TextPage()
                                                    .text("Precision Prospecting adds three additional kinds of prospecting tools with different ranges which can be used to enhance your prospecting expeditions."),
                                            new TextPage()
                                                    .li().text("The ").link(prosHammer, null, "Prospector's Hammer").text(" is similar to a regular ").link("mechanics/prospecting", "propick", "Prospector's Pick").text(" just with a smaller range.")
                                                    .li().text("The ").link(prosDrill, null, "Prospector's Drill").text(" is a directional prospector which has a non-cuboid, off-center scanning area")
                                                    .li().text("The ").link(minPros, null, "Mineral Prospector").text(" has a larger scanning area, but only detects ").thing("non-metal ores").text(".")
                                    );
                        }
                );
        ret.accept(category);
        ret.accept(intro);
        ret.accept(prosDrill);
        ret.accept(prosHammer);
        ret.accept(minPros);
    }
}
