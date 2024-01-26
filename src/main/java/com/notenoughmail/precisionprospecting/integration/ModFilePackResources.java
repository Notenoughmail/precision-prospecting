package com.notenoughmail.precisionprospecting.integration;

import com.notenoughmail.precisionprospecting.PrecisionProspecting;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;

public class ModFilePackResources extends PathPackResources {

    protected final IModFile modFile;
    protected final String sourcePath;
    private final boolean isHidden;

    public ModFilePackResources(String packName, IModFile modFile, String sourcePath) {
        this(packName, modFile, sourcePath, false);
    }

    public ModFilePackResources(String packName, IModFile modFile, String sourcePath, boolean isHidden) {
        super(packName, true, modFile.findResource(sourcePath));
        this.modFile = modFile;
        this.sourcePath = sourcePath;
        this.isHidden = isHidden;
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    @Override
    protected Path resolve(String... paths) {
        String[] allPaths = new String[paths.length + 1];
        allPaths[0] = sourcePath;
        System.arraycopy(paths, 0, allPaths, 1, paths.length);
        return modFile.findResource(allPaths);
    }
}
