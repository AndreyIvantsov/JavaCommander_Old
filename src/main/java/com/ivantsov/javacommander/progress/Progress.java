package com.ivantsov.javacommander.progress;

import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;

public interface Progress {
   PreProgressDlg getPreDlg() throws IOException;
    ProgressDlg getDlg() throws IOException;
    void atomicAction(Path source, Path target, CopyOption copyOption) throws IOException;
    void start();
}
