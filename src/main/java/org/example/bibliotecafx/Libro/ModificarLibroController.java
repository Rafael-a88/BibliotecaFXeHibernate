package org.example.bibliotecafx.Libro;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ModificarLibroController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField publisherField;

    @FXML
    private TextField yearField;

    // Método para recibir los datos del libro seleccionado
    public void setBookData(String title, String isbn, String author, String publisher, String year) {
        titleField.setText(title);
        isbnField.setText(isbn);
        authorField.setText(author);
        publisherField.setText(publisher);
        yearField.setText(year);
    }

    // Método para manejar la acción de actualizar el libro
    @FXML
    private void actualizarLibro() {
        // Aquí se implementará la lógica para actualizar el libro en la base de datos usando Hibernate
        String updatedTitle = titleField.getText();
        String updatedIsbn = isbnField.getText();
        String updatedAuthor = authorField.getText();
        String updatedPublisher = publisherField.getText();
        String updatedYear = yearField.getText();

        // Lógica para actualizar el libro en la base de datos
        // ...
    }
}
