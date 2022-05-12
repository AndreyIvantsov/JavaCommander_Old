package com.ivantsov.javacommander;

import com.ivantsov.javacommander.progress.PreProgressDlg;
import com.ivantsov.javacommander.progress.Progress;
import com.ivantsov.javacommander.progress.ProgressDlg;
import com.ivantsov.javacommander.progress.ProgressFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class MainCtrl {

//    @FXML
//    public VBox leftPanel;

//    @FXML
//    public VBox rightPanel;

    /*
     * Вложенные контроллеры
     * <p>
     * Нет необходимости создавать весь пользовательский интерфейс в одном FXML, используя один контроллер.
     * Тег <fx:include> можно использовать для включения одного файла fxml в другой. Контроллер включенного
     * fxml может быть введен в контроллер включенного файла так же, как и любой другой объект, созданный
     * FXMLLoader.
     * <p>
     * Это делается добавлением атрибута fx:id в элемент <fx:include>. Таким образом, контроллер включенного
     * fxml будет введен в поле с именем <fx:id value>Controller.
     * <p>
     * https://learntutorials.net/ru/javafx/topic/1580/
     */

    @FXML
    private PanelCtrl leftPanelController;  // name of field = leftPanel + Controller

    @FXML
    private PanelCtrl rightPanelController; // name of field = rightPanel + Controller

    @FXML
    public void mainWinOnKeyPressedAction(KeyEvent keyEvent) {
        // TODO: don't work, fix it
        System.out.println(keyEvent.getCode());
        // Alt + F4
        if (keyEvent.isAltDown() && keyEvent.getCode() == KeyCode.F4) {
            exitAction();
        }
    }

    @FXML
    public void fileMenuViewAction() {
        viewAction();
    }

    @FXML
    public void fileMenuEditAction() {
        editAction();
    }

    @FXML
    public void fileMenuCopyAction() throws IOException {
        copyAction();
    }

    @FXML
    public void fileMenuMoveAction() {
        moveAction();
    }

    @FXML
    public void fileMenuNewFolderAction() {
        newFolderAction();
    }

    @FXML
    public void fileMenuDeleteAction() {
        deleteAction();
    }

    @FXML
    public void fileMenuExitAction() {
        exitAction();
    }

    @FXML
    public void btnViewAction(ActionEvent actionEvent) throws IOException {
        viewAction();
    }

    @FXML
    public void btnEditAction(ActionEvent actionEvent) throws IOException {
        editAction();
    }

    @FXML
    public void btnCopyAction() throws IOException {
        copyAction();
    }

    @FXML
    public void btnMoveAction() {
        moveAction();
    }

    @FXML
    public void btnNewFolderAction() {
        newFolderAction();
    }

    @FXML
    public void btnDeleteAction() {
        deleteAction();
    }

    @FXML
    public void btnExitAction() {
        exitAction();
    }

    private void viewAction() {
    }

    private void editAction() {
    }

    private void startProgress(ProgressFactory factory) throws IOException {
        if (noSelectedItems()) {
            return;
        }
        PanelCtrl source = getSource();
        List<FileInfo> markedFiles = source.getSelectedItems();
        Path sourcePath = source.getCurrentPath();
        Path targetPath = getTarget().getCurrentPath();
        Progress progress = factory.getInstance(sourcePath, markedFiles, targetPath);
        if (!progress.getPreDlg().show()) {
            return;
        }
        ProgressDlg progressDlg = progress.getDlg();
        progressDlg.showAndWait();


    }

    private void copyAction() throws IOException {
        final String TITLE = "Copy";
        startProgress((sourcePath, markedFiles, destinationPath) -> new Progress() {
            @Override
            public PreProgressDlg getPreDlg() throws IOException {
                return new PreProgressDlg(
                        TITLE + getPresentationOfMarkedFiles(markedFiles),
                        sourcePath.toString(),
                        destinationPath.toString()
                );
            }

            @Override
            public ProgressDlg getDlg() throws IOException {
                return new ProgressDlg(TITLE, sourcePath.toString(), destinationPath.toString());
            }

            @Override
            public void atomicAction(Path source, Path target, CopyOption copyOption) throws IOException {
                Files.copy(source, target, copyOption);
            }

            @Override
            public void start() {

            }
        });
    }

    private void moveAction() {
    }

    private void newFolderAction() {
    }

    private void deleteAction() {
    }

    private void exitAction() {
        // TODO: don't work, fix it
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    private boolean noSelectedItems() {
        if (leftPanelController.getSelectedItems().isEmpty() && rightPanelController.getSelectedItems().isEmpty()) {
            //TODO: exclude alert in this code
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Copy");
            alert.setHeaderText("Copying is not possible");
            alert.setContentText("Not selected items for copying!");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private PanelCtrl getSource() {
        return (leftPanelController.getSelectedItems() != null) ? leftPanelController : rightPanelController;
    }

    private PanelCtrl getTarget() {
        return (leftPanelController.getSelectedItems() != null) ? rightPanelController : leftPanelController;
    }

    private String getPresentationOfMarkedFiles(List<FileInfo> markedFiles) {
        final String SEPARATOR = " ";
        final String QUOTATION_MARK = "\"";
        if (markedFiles.isEmpty()) {
            throw new RuntimeException("No marked files!");
        }
        if (markedFiles.size() == 1) {
            return SEPARATOR + QUOTATION_MARK +markedFiles.get(0).getFileName() + QUOTATION_MARK;
        } else {
            return SEPARATOR + markedFiles.size() + " items";
        }
    }
}