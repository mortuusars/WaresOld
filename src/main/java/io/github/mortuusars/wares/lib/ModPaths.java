package io.github.mortuusars.wares.lib;

import io.github.mortuusars.wares.Wares;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.mortuusars.wares.Wares.LOGGER;

public final class ModPaths {
    public static final Path MOD_ROOT = ModList.get().getModFileById(Wares.MOD_ID).getFile().getFilePath();

    public static final Path WARES_CONFIG_DIR = FileUtils.getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve(Wares.MOD_ID), Wares.MOD_ID);

    public static final Path WARES = createPath("wares");
    public static final Path DEFAULT_WARES = createPath("wares/default");

    private static Path createPath(String directory){
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toAbsolutePath().toString(), Wares.MOD_ID, directory);
        createDirectory(path, directory);
        return path;
    }

    private static void createDirectory(Path path, String dirName) {
        try { Files.createDirectories(path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) { LOGGER.error("Failed to create directory - '{}', Error: {} ", dirName, e.getMessage());}
    }
}
