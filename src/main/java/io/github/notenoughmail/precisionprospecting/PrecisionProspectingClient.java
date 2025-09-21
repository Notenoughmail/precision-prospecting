package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = PrecisionProspecting.ID, dist = Dist.CLIENT)
public class PrecisionProspectingClient {

    public PrecisionProspectingClient(IEventBus modBus, ModContainer container) {
        modBus.addListener(this::registerItemColorHandlers);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        PrecProsItems.FIRED_MOLDS.values().forEach(item -> event.register(ContainedFluidModel.COLOR, item));
    }
}
