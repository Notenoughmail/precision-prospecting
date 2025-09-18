package io.github.notenoughmail.precisionprospecting;

import com.mojang.serialization.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// TODO: Implement this
public class PatchouliProvider implements Provider, DataProvider {
    
    protected PackOutput.PathProvider out;
    protected CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected ExistingFileHelper efh;

    final List<EncodablePage<?>> entries = new ArrayList<>();
    
    @Override
    public void add(Consumer<? super DataProvider> ret, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        out = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, PatchouliAPI.MOD_ID);
        this.lookupProvider = lookupProvider;
        this.efh = efh;
        ret.accept(this);
    }
    
    void addPages(HolderLookup.Provider provider) {
        
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return lookupProvider.thenCompose(provider -> {
            addPages(provider);
            return CompletableFuture.allOf(
                    entries.stream()
                            .map(e -> e.encode(output, provider, out))
                            .toArray(CompletableFuture[]::new)
            );
        });
    }

    @Override
    public String getName() {
        return "PatchouliPageProvider";
    }
    
    interface EncodablePage<T extends EncodablePage<T>> {
        Codec<T> codec(HolderLookup.Provider provider);

        ResourceLocation id();

        default T thiz() {
            return (T) this;
        }

        default CompletableFuture<?> encode(CachedOutput output, HolderLookup.Provider provider, PackOutput.PathProvider out) {
            return DataProvider.saveStable(output, provider, codec(provider), thiz(), out.json(id()));
        }
    }
}
