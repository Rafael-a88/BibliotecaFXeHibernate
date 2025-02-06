package org.example.bibliotecafx.Libro;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.example.bibliotecafx.HelloController;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.bibliotecafx.Util.HibernateUtil;

public class AgregarLibroController {

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

    private HelloController mainController;

    @FXML
    private void AgregarLibro(ActionEvent event) {
        // Captura los datos de los campos de texto
        String titulo = titleField.getText();
        String isbn = isbnField.getText();
        String autor = authorField.getText();
        String editorial = publisherField.getText();
        String anio_publicacion = yearField.getText();

        // Validación básica de campos
        if (titulo.isEmpty() || isbn.isEmpty() || autor.isEmpty() || editorial.isEmpty() || anio_publicacion.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        // Validación del año de publicación
        int anio;
        try {
            anio = Integer.parseInt(anio_publicacion);
        } catch (NumberFormatException e) {
            System.out.println("El año de publicación debe ser un número válido.");
            return;
        }

        // Crea un objeto Libro
        Book libro = new Book();
        libro.setTitle(titulo);
        libro.setIsbn(isbn);
        libro.setAuthor(autor);
        libro.setPublisher(editorial);
        libro.setYear(anio);

        // Guarda el libro en la base de datos
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(libro);
            transaction.commit();
            System.out.println("Libro guardado en la base de datos: " + libro);

            // Actualiza la lista de libros en el controlador principal
            if (mainController != null) {
                mainController.cargarDatosLibro();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
}
