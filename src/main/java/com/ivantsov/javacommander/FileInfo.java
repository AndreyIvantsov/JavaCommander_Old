package com.ivantsov.javacommander;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {

    public enum FileType {
        FILE("F"), DIRECTORY("D");

        private final String name;

        public String getName() {
            return name;
        }

        FileType(String name) {
            this.name = name;
        }

    }
    // TODO: make a setting
    private static final boolean SHOW_SQUARE_BRACKETS_AROUND_DIRECTORIES = true;

    private final Path path;
    private final String fileName;
    private final FileType fileType;
    private final Long size;
    private final LocalDateTime lastModified;

    public FileInfo(Path path) {
        try {
            this.path = path;
            fileName = path.getFileName().toString();
            fileType = (Files.isDirectory(path)) ? FileType.DIRECTORY : FileType.FILE;
            if (fileType == FileType.DIRECTORY) {
                size = -1L;
            } else {
                size = Files.size(path);
            }
            lastModified = LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(path).toInstant(),
                    ZoneOffset.systemDefault());
        } catch (IOException e) {
            // TODO: Figure out how to handle this exception
            throw new RuntimeException("Unable to create file info from path!");
        }
    }

    public Path getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRepresentationOfFileName() {
        if (SHOW_SQUARE_BRACKETS_AROUND_DIRECTORIES && this.getFileType() == FileType.DIRECTORY) {
            return "[" + fileName + "]";
        }
        return fileName;
    }

    public FileType getFileType() {
        return fileType;
    }

    public Long getSize() {
        return size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }
}
