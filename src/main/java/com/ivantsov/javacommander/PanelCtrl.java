package com.ivantsov.javacommander;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelCtrl implements Initializable {

    // TODO: exchange magic numbers to final var

    @FXML
    TableView<FileInfo> filesTable;

    @FXML
    ComboBox<String> disksBox;

    @FXML
    TextField pathField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // File Type Column
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>("T");
        fileTypeColumn.setResizable(false);
        fileTypeColumn.setSortType(TableColumn.SortType.ASCENDING);
        fileTypeColumn.setSortable(false);
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileType().getName()));
        fileTypeColumn.setStyle("-fx-alignment: CENTER;");
        fileTypeColumn.setMinWidth(24);
        fileTypeColumn.setMaxWidth(24);

        // Filename Column
        TableColumn<FileInfo, String> filenameColumn = new TableColumn<>("Name");
        filenameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRepresentationOfFileName()));
        filenameColumn.setMinWidth(120);
        filenameColumn.setMaxWidth(1000);

        // File Size Column
        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Long aLong, boolean b) {
                super.updateItem(aLong, b);
                if (aLong == null || b) {
                    setText(null);
                    setStyle("");
                } else {
                    setText((aLong == -1L) ? "<DIR>" : String.format("%,d bytes", aLong));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });
        fileSizeColumn.setMinWidth(120);
        fileSizeColumn.setMaxWidth(240);

        // File last modified Data Column
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDataColumn = new TableColumn<>("Date");
        fileDataColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDataColumn.setStyle("-fx-alignment: CENTER;");
        fileDataColumn.setMinWidth(120);
        fileDataColumn.setMaxWidth(160);

        // Files table
        filesTable.getColumns().addAll(fileTypeColumn, filenameColumn, fileSizeColumn, fileDataColumn);
        filesTable.getSortOrder().addAll(fileTypeColumn, filenameColumn);
        filesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        filesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        filesTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                openSelectedItem();
            }
        });

        filesTable.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                openSelectedItem();
            }
        });

        filesTable.sortPolicyProperty().set(t -> {
            Comparator<FileInfo> comparator = (r1, r2) -> {
                String topDirName = getTopDirectoryName();
                var r1t = r1.getFileType();
                var r2t = r2.getFileType();
                if (r1.getFileName().equals(topDirName)) {
                    return -1;
                } else if (r2.getFileName().equals(topDirName)) {
                    return  1;
                } else if (r1t == FileInfo.FileType.DIRECTORY && r2t == FileInfo.FileType.FILE) {
                    return -1;
                } else if (r1t == FileInfo.FileType.FILE && r2t == FileInfo.FileType.DIRECTORY) {
                    return  1;
                } else if (t.getComparator() == null) {
                    return  0;
                } else {
                    return t.getComparator().compare(r1, r2);
                }
            };
            FXCollections.sort(t.getItems(), comparator);
            return true;
        });

        // Disk box
        disksBox.getItems().clear();
        for (Path path : FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(path.toString());
            disksBox.getSelectionModel().select(0);
            updateFileList(Paths.get("."));
        }
    }

    @FXML
    public void panelOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            pathUpAction();
        }
    }

    @FXML
    public void btnPathUpAction() {
        pathUpAction();
    }

    @FXML
    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> comboBox = (ComboBox<String>) actionEvent.getSource();
        updateFileList(Paths.get(comboBox.getSelectionModel().getSelectedItem()));
    }

    public void updateFileList(Path path) {
        try {
            Collection<FileInfo> list = Files.list(path).map(FileInfo::new).collect(Collectors.toList());
            Path absolutePath = path.normalize().toAbsolutePath();
            if (absolutePath.getParent() != null) {
                list.add(new FileInfo(absolutePath.resolve("..")));
            }
            pathField.setText(absolutePath.toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(list);
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Update");
            alert.setHeaderText("Failed to update the file list!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public ObservableList<FileInfo> getSelectedItems() {
        return filesTable.getSelectionModel().getSelectedItems();
    }

    public String getCurrentPathName() {
        return pathField.getText();
    }

    public Path getCurrentPath() {
        return Paths.get(getCurrentPathName());
    }

    public void pathUpAction() {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null) {
            updateFileList(upperPath);
        }
    }

    private void openSelectedItem() {
        if (!filesTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Path selectedPath = filesTable.getSelectionModel().getSelectedItem().getPath();
            if (Files.isDirectory(selectedPath)) {
                openDirectory(selectedPath);
            } else {
                openFile(selectedPath);
            }
        }
    }

    private void openDirectory(Path path) {
        if (Files.isDirectory(path)) {
            updateFileList(path);
        }
    }

    private void openFile(Path path) {
        Files.isDirectory(path);
        // TODO: open selected file
    }

    private String getTopDirectoryName() {
        return "..";
    }
}
