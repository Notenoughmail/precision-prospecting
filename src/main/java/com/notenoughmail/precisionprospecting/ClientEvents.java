package com.notenoughmail.precisionprospecting;

import com.notenoughmail.precisionprospecting.items.Registration;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEvents {

    public static void init() {

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEvents::registerItemColorHandlers);
    }

    private static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register(new ContainedFluidModel.Colors(), Registration.FIRED_MINERAL_PROSPECTOR_MOLD.get());
        event.register(new ContainedFluidModel.Colors(), Registration.FIRED_PROSDRILL_MOLD.get());
        event.register(new ContainedFluidModel.Colors(), Registration.FIRED_PROSHAMMER_MOLD.get());
    }
}
