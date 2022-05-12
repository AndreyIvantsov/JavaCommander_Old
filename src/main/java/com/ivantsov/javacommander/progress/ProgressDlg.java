package com.ivantsov.javacommander.progress;

import com.ivantsov.javacommander.MainCtrl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ProgressDlg {

    private Stage stage;
    private ProgressDlgCtrl controller;

    public ProgressDlg(String title, String pathFrom, String pathTo) throws IOException {
        final String FXML_FILE = "ProgressDlg.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(MainCtrl.class.getResource(FXML_FILE));
        Parent parent = fxmlLoader.load();

        controller = fxmlLoader.getController();
        controller.setTxtTitle(title);
        controller.setTxtPathFrom(pathFrom);
        controller.setTxtPathTo(pathTo);

        stage = new Stage();
        Scene scene = new Scene(parent);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent.getScene().getWindow());
        stage.setTitle(title);
        // TODO: make a setting for the width of the dialog box
        stage.setWidth(500);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
