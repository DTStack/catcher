package com.dtstack.catcher.common.utils;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathUtils {
    private PathUtils() {}
    
    static final FileSystem ACTUAL_DEFAULT = FileSystems.getDefault();
    
    static volatile FileSystem DEFAULT = ACTUAL_DEFAULT;
    
    public static Path get(String first, String... more) {
        return DEFAULT.getPath(first, more);
    }

    public static Path get(URI uri) {
        if (uri.getScheme().equalsIgnoreCase("file")) {
            return DEFAULT.provider().getPath(uri);
        } else {
            return Paths.get(uri);
        }
    }

    public static Path get(Path[] roots, String path) {
        for (Path root : roots) {
            Path normalizedRoot = root.normalize();
            Path normalizedPath = normalizedRoot.resolve(path).normalize();
            if(normalizedPath.startsWith(normalizedRoot)) {
                return normalizedPath;
            }
        }
        return null;
    }

    public static Path get(Path[] roots, URI uri) {
        return get(roots, PathUtils.get(uri).normalize().toString());
    }

    public static FileSystem getDefaultFileSystem() {
        return DEFAULT;
    }
}
