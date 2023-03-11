package com.notenoughmail.precisionprospecting.integration.vexxedvisuals;

import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathResourcePack;

import java.nio.file.Path;

public class ModFilePackResources extends PathResourcePack {

    protected final IModFile modFile;
    protected final String sourcePath;

    public ModFilePackResources(String packName, IModFile modFile, String sourcePath) {
        super(packName, modFile.findResource(sourcePath));
        this.modFile = modFile;
        this.sourcePath = sourcePath;
    }

    @Override
    protected Path resolve(String... paths) {
        String[] allPaths = new String[paths.length + 1];
        allPaths[0] = sourcePath;
        System.arraycopy(paths, 0, allPaths, 1, paths.length);
        return modFile.findResource(allPaths);
    }
}
