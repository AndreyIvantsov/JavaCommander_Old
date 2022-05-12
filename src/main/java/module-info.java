module com.ivantsov.javacommander {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ivantsov.javacommander to javafx.fxml;
    exports com.ivantsov.javacommander;
    exports com.ivantsov.javacommander.progress;
    opens com.ivantsov.javacommander.progress to javafx.fxml;
}