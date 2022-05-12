package com.ivantsov.javacommander;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.CopyOption.*;

public class Model {

    public static void copy(Path sourcePath, List<FileInfo> selectedFiles, Path destinationPath) throws IOException {

        for (FileInfo sourceFile : selectedFiles) {
            Files.copy(sourcePath.resolve(sourceFile.getFileName()), destinationPath.resolve(sourceFile.getFileName()));
            // TODO: implement window of copy progress
            // TODO: implement a question to the user about replacing files when copying
        }
    }

    public static void move(String sourcePath, List<String> selectedFiles, String destinationPath) throws IOException {
        for (String sourceFile : selectedFiles) {
            Files.move(Paths.get(sourcePath, sourceFile), Paths.get(destinationPath, sourceFile));

            // TODO: implement window of copy progress
            // TODO: implement a question to the user about replacing files when copying
        }
    }

//    private static void doublePanelFilesAction(DoublePanelFilesAction filesAction) throws IOException {
//        for (String sourceFile : selectedFiles) {
//            Files.move(Paths.get(sourcePath, sourceFile), Paths.get(destinationPath, sourceFile));
//        }
//    }

}
