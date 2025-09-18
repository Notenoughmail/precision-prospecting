package io.github.notenoughmail.precisionprospecting;

import io.github.notenoughmail.precisionprospecting.items.PrecProsItems;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class PrecProsClientEvents {

    public static void init(IEventBus modBus) {
        modBus.addListener(PrecProsClientEvents::registerItemColorHandlers);
    }

    private static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        PrecProsItems.FIRED_MOLDS.values().forEach(item -> event.register(ContainedFluidModel.COLOR, item));
    }
}
