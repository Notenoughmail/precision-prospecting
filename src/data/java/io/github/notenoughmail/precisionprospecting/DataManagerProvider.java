package io.github.notenoughmail.precisionprospecting;

import com.google.common.collect.ImmutableMap;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public abstract class DataManagerProvider<T> implements DataProvider {

    private final DataManager<T> manager;
    private final PackOutput.PathProvider path;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final ImmutableMap.Builder<ResourceLocation, T> elements;

    protected DataManagerProvider(DataManager<T> manager, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.manager = manager;
        path = output.createPathProvider(PackOutput.Target.DATA_PACK, TerraFirmaCraft.MOD_ID + "/" + manager.getName());
        this.lookupProvider = lookupProvider;
        elements = ImmutableMap.builder();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return lookupProvider.thenCompose(provider -> {
            addData(provider);
            return CompletableFuture.allOf(
                    elements.buildOrThrow().entrySet().stream()
                            .map(e -> DataProvider.saveStable(output, provider, manager.codec(), e.getValue(), path.json(e.getKey())))
                            .toArray(CompletableFuture[]::new)
            );
        });
    }

    @Override
    public String getName() {
        return "DataManager(" + manager.getName() + ")";
    }

    protected final void add(String path, T value) {
        elements.put(PrecisionProspecting.id(path), value);
    }

    protected abstract void addData(HolderLookup.Provider provider);
}
