package com.ivantsov.javacommander.progress;

import com.ivantsov.javacommander.FileInfo;
import com.ivantsov.javacommander.PanelCtrl;

import java.nio.file.Path;
import java.util.List;

public interface ProgressFactory {
    Progress getInstance(Path sourcePath, List<FileInfo> markedFiles, Path targetPath);
}
