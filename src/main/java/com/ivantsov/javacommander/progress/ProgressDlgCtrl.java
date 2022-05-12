package com.ivantsov.javacommander.progress;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProgressDlgCtrl {

    @FXML
    public Label txtTitleAction;

    @FXML
    public Label txtFrom;

    @FXML
    public Label txtTo;

    @FXML
    public Label txtPathFrom;

    @FXML
    public Label txtPathTo;

    @FXML
    public Label txtItemPercent;

    @FXML
    public Label txtCommonPercent;

    @FXML
    public ProgressBar progressItemPercent;

    @FXML
    public Label txtItemsProgress;

    @FXML
    public ProgressBar progressCommonPercent;

    @FXML
    public Label txtSizeProgress;

    public void setTxtTitle(String titleAction) {
        txtTitleAction.setText(titleAction);
    }

    public void setTxtPathFrom(String pathFrom) {
        txtPathFrom.setText(pathFrom);
    }

    public void setTxtPathTo(String pathTo) {
        txtPathTo.setText(pathTo);
    }

}
