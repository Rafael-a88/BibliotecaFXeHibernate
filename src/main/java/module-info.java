module org.example.bibliotecafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens org.example.bibliotecafx to javafx.fxml; // Permitir acceso a HelloController
    opens org.example.bibliotecafx.Autor to javafx.fxml; // Permitir acceso a AgregarAutorController
    opens org.example.bibliotecafx.Libro to javafx.fxml; // Permitir acceso a ModificarLibroController

    exports org.example.bibliotecafx;
    exports org.example.bibliotecafx.Libro;
    exports org.example.bibliotecafx.Autor; // Asegúrate de exportar el paquete Autor
}
