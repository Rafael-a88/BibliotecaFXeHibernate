module org.example.bibliotecafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.desktop;
    requires java.persistence; // Necesario para JPA
    requires java.naming;
    requires org.hibernate.orm.core;
    requires jakarta.persistence; // Necesario para MySQL

    // Hibernate no tiene un módulo explícito, pero puedes abrirlo al módulo no nombrado
    opens org.example.bibliotecafx to javafx.fxml;
    opens org.example.bibliotecafx.Autor to org.hibernate.orm.core, javafx.fxml;
    opens org.example.bibliotecafx.Libro to org.hibernate.orm.core, javafx.fxml;
    opens org.example.bibliotecafx.Socios to org.hibernate.orm.core, javafx.fxml;
    opens org.example.bibliotecafx.Prestamos to org.hibernate.orm.core, javafx.fxml;

    exports org.example.bibliotecafx;
    exports org.example.bibliotecafx.Libro;
    exports org.example.bibliotecafx.Autor;
    exports org.example.bibliotecafx.Socios;
    exports org.example.bibliotecafx.Prestamos;
}
