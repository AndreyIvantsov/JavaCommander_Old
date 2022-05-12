package com.ivantsov.javacommander.progress;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PreProgressDlgCtrl {

    @FXML
    private Label txtTitleAction;

    @FXML
    private Label txtFrom;

    @FXML
    private Label txtTo;

    @FXML
    private Label txtPathFrom;

    @FXML
    private Label txtPathTo;

    private boolean keyOkPressed = false;

    public boolean getDialogResult(){
        return keyOkPressed;
    }

    public void setTxtTitle(String titleAction) {
        txtTitleAction.setText(titleAction);
    }

    public void setTxtPathFrom(String pathFrom) {
        txtPathFrom.setText(pathFrom);
    }

    public void setTxtPathTo(String pathTo) {
        txtPathTo.setText(pathTo);
    }

    public void actionCancel(ActionEvent actionEvent) {
        keyOkPressed = false;
        close(actionEvent);
    }

    public void actionOk(ActionEvent actionEvent) {
        keyOkPressed = true;
        close(actionEvent);
    }

    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
