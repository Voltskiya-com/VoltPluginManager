package com.voltskiya.lib.configs.data.config;


import apple.utilities.util.FileFormatting;
import java.io.File;

public class AppleConfigPath {

    private final String name;
    private String[] path;
    private String extension;

    public AppleConfigPath(String name) {
        this.name = name;
        this.path = null;
        this.extension = null;
    }

    public AppleConfigPath(String name, String[] path) {
        this.name = name;
        this.path = path;
        this.extension = null;
    }

    @Override
    public int hashCode() {
        int hashCode = this.name.hashCode();
        for (String p : this.path)
            hashCode += p.hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AppleConfigPath other)) return false;

        if (!this.name.equals(other.name))
            return false;

        if (this.path.length != other.path.length)
            return false;
        for (int i = 0; i < path.length; i++) {
            if (!this.path[i].equals(other.path[i]))
                return false;
        }
        return this.extension == null ||
            other.extension == null ||
            this.extension.equals(other.extension);
    }

    public String name() {
        return name;
    }

    public String[] path() {
        return path;
    }

    public String extension() {
        return this.extension;
    }

    public void setPath(String[] path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    public File getFile(File rootFolder) {
        File folder = FileFormatting.folderWithChildren(rootFolder, this.path);
        return new File(folder, this.name + this.extension);
    }
}
