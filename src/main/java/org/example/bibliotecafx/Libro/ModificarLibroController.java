package org.example.bibliotecafx.Libro;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bibliotecafx.HelloController;
import org.example.bibliotecafx.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    private Book selectedBook; // Almacenar el libro seleccionado
    private HelloController mainController;

    // Método para recibir los datos del libro seleccionado
    public void setBookData(Book book) {
        this.selectedBook = book; // Guardar el libro seleccionado
        titleField.setText(book.getTitle());
        isbnField.setText(book.getIsbn());
        authorField.setText(book.getAuthor());
        publisherField.setText(book.getPublisher());
        yearField.setText(String.valueOf(book.getYear()));
    }

    // Método para manejar la acción de actualizar el libro
    @FXML
    private void actualizarLibro() {
        if (selectedBook != null) {
            String updatedTitle = titleField.getText();
            String updatedIsbn = isbnField.getText();
            String updatedAuthor = authorField.getText();
            String updatedPublisher = publisherField.getText();
            String updatedYear = yearField.getText();

            // Validación básica de campos
            if (updatedTitle.isEmpty() || updatedIsbn.isEmpty() || updatedAuthor.isEmpty() || updatedPublisher.isEmpty() || updatedYear.isEmpty()) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            // Lógica para actualizar el libro en la base de datos
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                // Actualiza los atributos del libro seleccionado
                selectedBook.setTitle(updatedTitle);
                selectedBook.setIsbn(updatedIsbn);
                selectedBook.setAuthor(updatedAuthor);
                selectedBook.setPublisher(updatedPublisher);
                selectedBook.setYear(Integer.parseInt(updatedYear));

                session.update(selectedBook); // Actualiza el libro en la base de datos
                transaction.commit();
                System.out.println("Libro actualizado en la base de datos: " + selectedBook);

                // Actualizar la lista en el controlador principal
                if (mainController != null) {
                    mainController.cargarDatosLibro(); // Llama al método para recargar los datos
                }

                // Cerrar la ventana después de actualizar
                ((Stage) titleField.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al actualizar el libro en la base de datos.");
            }
        }
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    // Método para mostrar un Alert
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
