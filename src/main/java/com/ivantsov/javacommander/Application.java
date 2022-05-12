package com.ivantsov.javacommander;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {

    private static final String MAIN_WIN_TITLE = "Java Commander";
    private static final int MAIN_WIN_WIDTH = 1200;
    private static final int MAIN_WIN_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("MainWim.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MAIN_WIN_WIDTH, MAIN_WIN_HEIGHT);
        stage.setTitle(MAIN_WIN_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}