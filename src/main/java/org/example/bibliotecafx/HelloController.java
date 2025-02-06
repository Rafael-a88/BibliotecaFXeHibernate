package org.example.bibliotecafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.bibliotecafx.Autor.AgregarAutorController;
import org.example.bibliotecafx.Autor.Autor;
import org.example.bibliotecafx.Autor.ModificarAutorController;
import org.example.bibliotecafx.Libro.AgregarLibroController;
import org.example.bibliotecafx.Libro.Book;
import org.example.bibliotecafx.Libro.ModificarLibroController;
import org.example.bibliotecafx.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloController {

    // Variables para libros
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TextField searchField;
    @FXML
    private TextField searchField2;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, Integer> yearColumn;

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    // Variables para autores
    @FXML
    private TableView<Autor> authorsTable; // Tabla para autores
    @FXML
    private TableColumn<Autor, String> nombreColumn; // Columna para nombre
    @FXML
    private TableColumn<Autor, String> nacionalidadColumn; // Columna para nacionalidad

    private final ObservableList<Autor> authorList = FXCollections.observableArrayList(); // Lista de autores

    public HelloController(){

    }

    @FXML
    public void initialize() {
        cargarDatosLibro();
        cargarDatosAutor();
    }


    // Parte del controlador para controlar la pestaña LIBRO

    public void cargarDatosLibro() {
        // Vincular las columnas del TableView con los atributos de la clase Book
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Cargar los libros desde la base de datos
        bookList.clear();
        List<Book> libros = obtenerLibrosDesdeBD(); // Metodo para obtener libros desde la base de datos
        bookList.addAll(libros); // Agregar los libros a la lista observable
        booksTable.setItems(bookList); // Vincular la lista al TableView
    }

    private List<Book> obtenerLibrosDesdeBD() {
        List<Book> libros = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            libros = session.createQuery("FROM Book", Book.class).list();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return libros;
    }

    @FXML
    public void abrirVentanaAgregarLibro() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bibliotecafx/AgregarLibro.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);

            Stage stage = new Stage();
            stage.setTitle("Agregar Libro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pasar el controlador principal al controlador de agregar libro
            AgregarLibroController controller = fxmlLoader.getController();
            controller.setMainController(this);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void abrirVentanaModificarLibro() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bibliotecafx/ModificarLibro.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 400);

                Stage stage = new Stage();
                stage.setTitle("Modificar Libro");
                stage.setScene(scene);

                ModificarLibroController controller = fxmlLoader.getController();
                controller.setBookData(selectedBook); // Pasar el objeto Book completo
                controller.setMainController(this); // Pasar el controlador principal

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Por favor, selecciona un libro para modificar.");
        }
    }



    @FXML
    public void eliminarLibro() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Está seguro de que desea eliminar este libro?");
            alert.setContentText("Título: " + selectedBook.getTitle() + "\nISBN: " + selectedBook.getIsbn());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar el libro de la base de datos
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.delete(session.merge(selectedBook)); // Asegúrate de que el libro esté gestionado
                    transaction.commit();
                    System.out.println("Libro eliminado de la base de datos: " + selectedBook);

                    // Actualizar la lista de libros
                    bookList.remove(selectedBook);
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Error al eliminar el libro de la base de datos.");
                }
            }
        } else {
            mostrarAlerta("Por favor, selecciona un libro para eliminar.");
        }
    }

    @FXML
    public void buscarLibros() {
        String query = searchField.getText().toLowerCase(); // Obtener el texto de búsqueda
        ObservableList<Book> filteredList = FXCollections.observableArrayList();

        // Filtrar los libros
        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(query) ||
                    book.getAuthor().toLowerCase().contains(query) ||
                    book.getIsbn().contains(query)) {
                filteredList.add(book);
            }
        }

        // Actualizar la tabla
        booksTable.setItems(filteredList);
    }

    @FXML
    public void mostrarTodo() {
        booksTable.setItems(bookList);
    }

    // Método para mostrar un Alert
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // FIN de la parte del controlador para controlar la pestaña LIBRO

    public void cargarDatosAutor() {
        // Vincular las columnas del TableView con los atributos de la clase Autor
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));

        // Vincular authorList al TableView
        authorsTable.setItems(authorList);
    }

    @FXML
    public void abrirVentanaAgregarAutor() {
        try {
            // Cargar el archivo FXML de la ventana "Agregar Autor"
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bibliotecafx/AgregarAutor.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);

            // Crear una nueva ventana (Stage) para la vista "Agregar Libro"
            Stage stage = new Stage();
            stage.setTitle("Agregar Autor");
            stage.setScene(scene);

            // Configurar la ventana como modal (bloquea la ventana principal hasta que se cierre)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Mostrar la ventana y esperar a que se cierre
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirVentanaModificarAutor() {
        Autor selectedAutor = authorsTable.getSelectionModel().getSelectedItem();

        if (selectedAutor != null) { // Verifica que haya un autor seleccionado
            try {
                // Cargar el archivo FXML de la ventana "Modificar Autor"
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bibliotecafx/ModificarAutor.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 400);

                // Crear una nueva ventana (Stage) para la vista "Modificar Autor"
                Stage stage = new Stage();
                stage.setTitle("Modificar Autor");
                stage.setScene(scene);

                // Obtener el controlador de la ventana "Modificar Autor"
                ModificarAutorController controller = fxmlLoader.getController();
                controller.setAutorData(selectedAutor.getNombre(), selectedAutor.getNacionalidad());

                // Configurar la ventana como modal (bloquea la ventana principal hasta que se cierre)
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Mostrar un mensaje de advertencia si no hay un autor seleccionado
            mostrarAlerta("Por favor, selecciona un autor para modificar.");
        }
    }



    @FXML
    public void eliminarAutor() {
        Autor selectedAutor = authorsTable.getSelectionModel().getSelectedItem();

        if (selectedAutor != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Está seguro de que desea eliminar este autor?");
            alert.setContentText("Nombre: " + selectedAutor.getNombre());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                authorList.remove(selectedAutor);
            }
        } else {
            mostrarAlerta("Por favor, selecciona un autor para eliminar.");
        }
    }

    @FXML
    public void buscarAutor() {
        String query = searchField2.getText().toLowerCase(); // Obtener el texto de búsqueda
        ObservableList<Autor> filteredList = FXCollections.observableArrayList();

        // Filtrar los libros
        for (Autor autor : authorList) {
            if (autor.getNombre().toLowerCase().contains(query) ||
                    autor.getNacionalidad().toLowerCase().contains(query)) {
                filteredList.add(autor);
            }
        }

        // Actualizar la tabla
        authorsTable.setItems(filteredList);
    }

    @FXML
    public void mostrarTodoAutor() {
        authorsTable.setItems(authorList);
    }
    // Parte del controlador para controlar la pestaña Autor


}
