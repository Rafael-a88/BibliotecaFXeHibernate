package org.example.bibliotecafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.bibliotecafx.Autor.Autor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.io.IOException;
import org.example.bibliotecafx.Util.HibernateUtil;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        stage.setTitle("Biblioteca San Agustín");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        // Lanzar la aplicación JavaFX
        launch(args);
    }
}
