package org.example.bibliotecafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.bibliotecafx.Autor.AgregarAutorController;
import org.example.bibliotecafx.Autor.Autor;
import org.example.bibliotecafx.Autor.ModificarAutorController;
import org.example.bibliotecafx.Libro.AgregarLibroController;
import org.example.bibliotecafx.Libro.Book;
import org.example.bibliotecafx.Libro.ModificarLibroController;
import org.example.bibliotecafx.Prestamos.RegistrarPrestamoController;
import org.example.bibliotecafx.Socios.AgregarSocioController;
import org.example.bibliotecafx.Socios.ModificarSocioController;
import org.example.bibliotecafx.Socios.Socio;
import org.example.bibliotecafx.Prestamos.Prestamos;
import org.example.bibliotecafx.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelloController {



    // Variables para libros
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TextField searchField;
    @FXML
    private TextField searchField2;
    @FXML
    private TextField searchField3;
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

    // Variables para Socios

    @FXML
    private TableView<Socio> sociosTable; // Tabla para socios
    @FXML
    private TableColumn<Socio, String> nombreColumn2; // Columna para nombre
    @FXML
    private TableColumn<Socio, String> direccionColumn; // Columna para dirección
    @FXML
    private TableColumn<Socio, String> telefonoColumn; // Columna para teléfono

    private final ObservableList<Socio> socioList = FXCollections.observableArrayList(); // Lista de socios


    // Variables para Prestamos
    @FXML
    private TableView<Prestamos> loansTable;
    @FXML
    private TableColumn<Prestamos, String> libroColumn;
    @FXML
    private TableColumn<Prestamos, String> socioColumn;
    @FXML
    private TableColumn<Prestamos, LocalDate> fechaPrestamColumn;
    @FXML
    private TableColumn<Prestamos, LocalDate> fechaDevolucionColumn;
    @FXML
    private TableColumn<Prestamos, String> estadoColumn;
    @FXML
    private TextField buscarTextField;


    private final ObservableList<Prestamos> loansList = FXCollections.observableArrayList();

    public HelloController(){

    }

    @FXML
    public void initialize() {
        cargarDatosLibro();
        cargarDatosAutor();
        cargarDatosSocio();
        cargarDatosPrestamos();


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

            // Obtener los libros que no tienen préstamos activos
            libros = session.createQuery(
                            "SELECT b " +
                                    "FROM Book b " +
                                    "WHERE b.id NOT IN (" +
                                    "  SELECT p.libro.id " +
                                    "  FROM Prestamos p " +
                                    "  WHERE p.estado = 'Activo'" +
                                    ")", Book.class)
                    .list();

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

    // Parte del controlador para controlar la pestaña Autor
    public void cargarDatosAutor() {
        // Vincular las columnas del TableView con los atributos de la clase Autor
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));

        // Cargar los autores desde la base de datos
        authorList.clear();
        List<Autor> autores = obtenerAutoresDesdeBD(); // Método para obtener autores desde la base de datos
        authorList.addAll(autores); // Agregar los autores a la lista observable
        authorsTable.setItems(authorList); // Vincular la lista al TableView
    }

    private List<Autor> obtenerAutoresDesdeBD() {
        List<Autor> autores = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            autores = session.createQuery("FROM Autor", Autor.class).list();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return autores;
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

            // Pasar el controlador principal al controlador de agregar autor
            AgregarAutorController controller = fxmlLoader.getController();
            controller.setMainController(this);

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
                controller.setAutorData(selectedAutor); // Pasar el objeto Autor completo
                controller.setMainController(this); // Pasar el controlador principal

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
                // Eliminar el autor de la base de datos
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.delete(session.merge(selectedAutor));
                    transaction.commit();
                    System.out.println("Autor eliminado de la base de datos: " + selectedAutor);

                    // Actualizar la lista de autores
                    authorList.remove(selectedAutor);
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Error al eliminar el autor de la base de datos.");
                }
            }
        } else {
            mostrarAlerta("Por favor, selecciona un autor para eliminar.");
        }
    }


    @FXML
    public void buscarAutor() {
        String query = searchField2.getText().toLowerCase(); // Obtener el texto de búsqueda
        ObservableList<Autor> filteredList = FXCollections.observableArrayList();

        // Filtrar los autores
        for (Autor autor : authorList) {
            if (autor.getNombre().toLowerCase().contains(query)) {
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

    // Fin de la parte del controlador para controlar la pestaña Autor

    // Parte del controlador para controlar la pestaña Socios

    // Método para cargar los datos de los socios
    public void cargarDatosSocio() {
        // Vincular las columnas del TableView con los atributos de la clase Socio
        nombreColumn2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Cargar los socios desde la base de datos
        socioList.clear();
        List<Socio> socios = obtenerSociosDesdeBD(); // Método para obtener socios desde la base de datos
        socioList.addAll(socios); // Agregar los socios a la lista observable
        sociosTable.setItems(socioList); // Vincular la lista al TableView
    }

    private List<Socio> obtenerSociosDesdeBD() {
        List<Socio> socios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            socios = session.createQuery("FROM Socio", Socio.class).list();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socios;
    }

    @FXML
    public void abrirVentanaAgregarSocio() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bibliotecafx/AgregarSocio.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);

            Stage stage = new Stage();
            stage.setTitle("Agregar Socio");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pasar el controlador principal al controlador de agregar socio
            AgregarSocioController controller = fxmlLoader.getController();
            controller.setMainController(this);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirVentanaModificarSocio() {
        Socio selectedSocio = sociosTable.getSelectionModel().getSelectedItem();

        if (selectedSocio != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bibliotecafx/ModificarSocio.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 400);

                Stage stage = new Stage();
                stage.setTitle("Modificar Socio");
                stage.setScene(scene);

                ModificarSocioController controller = fxmlLoader.getController();
                controller.setSocioData(selectedSocio); // Pasar el objeto Socio completo
                controller.setMainController(this); // Pasar el controlador principal

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Por favor, selecciona un socio para modificar.");
        }
    }

    @FXML
    public void eliminarSocio() {
        Socio selectedSocio = sociosTable.getSelectionModel().getSelectedItem();

        if (selectedSocio != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Está seguro de que desea eliminar este socio?");
            alert.setContentText("Nombre: " + selectedSocio.getNombre());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar el socio de la base de datos
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.delete(session.merge(selectedSocio)); // Asegúrate de que el socio esté gestionado
                    transaction.commit();
                    System.out.println("Socio eliminado de la base de datos: " + selectedSocio);

                    // Actualizar la lista de socios
                    socioList.remove(selectedSocio);
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Error al eliminar el socio de la base de datos.");
                }
            }
        } else {
            mostrarAlerta("Por favor, selecciona un socio para eliminar.");
        }
    }

    @FXML
    public void buscarSocios() {
        String query = searchField3.getText().toLowerCase(); // Obtener el texto de búsqueda
        ObservableList<Socio> filteredList = FXCollections.observableArrayList();

        // Filtrar los socios
        for (Socio socio : socioList) {
            if (socio.getNombre().toLowerCase().contains(query) ||
                    socio.getTelefono().contains(query)) {
                filteredList.add(socio);
            }
        }

        // Actualizar la tabla
        sociosTable.setItems(filteredList);
    }

    @FXML
    public void mostrarTodoSocio() {
        sociosTable.setItems(socioList);
    }

    // Parte del controlador para los Prestamos

    @FXML
    public void abrirVentanaRegistrarPrestamo() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/bibliotecafx/RegistrarPrestamo.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);

            Stage stage = new Stage();
            stage.setTitle("Registrar Préstamo");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            RegistrarPrestamoController controller = fxmlLoader.getController();
            controller.setMainController(this);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registrarDevolucion() {
        // Obtener el préstamo seleccionado
        Prestamos selectedPrestamo = loansTable.getSelectionModel().getSelectedItem();

        if (selectedPrestamo != null) {
            // Cambiar el estado a "Entregado"
            selectedPrestamo.setEstado("Entregado");

            // Actualizar el préstamo en la base de datos
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.update(selectedPrestamo); // Actualiza el objeto en la base de datos
                transaction.commit();
                System.out.println("Préstamo actualizado a 'Entregado': " + selectedPrestamo);
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al actualizar el estado del préstamo.");
            }

            // Actualizar la tabla para reflejar los cambios
            cargarDatosPrestamos(); // Recargar los datos de la tabla
            cargarDatosLibro();
        } else {
            mostrarAlerta("Por favor, selecciona un préstamo para registrar la devolución.");
        }
    }


    public void cargarDatosPrestamos() {
        // Vincular las columnas del TableView con los atributos de la clase Prestamos
        libroColumn.setCellValueFactory(new PropertyValueFactory<>("libro"));
        socioColumn.setCellValueFactory(new PropertyValueFactory<>("socio"));
        fechaPrestamColumn.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        fechaDevolucionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Cargar los préstamos desde la base de datos
        loansList.clear();
        List<Prestamos> prestamos = obtenerPrestamosDesdeBD(); // Método para obtener préstamos desde la base de datos
        loansList.addAll(prestamos); // Agregar los préstamos a la lista observable
        loansTable.setItems(loansList); // Vincular la lista al TableView
        cargarDatosLibro();
    }

    private List<Prestamos> obtenerPrestamosDesdeBD() {
        List<Prestamos> prestamos = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            prestamos = session.createQuery("FROM Prestamos", Prestamos.class).list();
            transaction.commit();
            cargarDatosLibro();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    @FXML
    public void buscarPrestamos() {
        String busqueda = buscarTextField.getText().toLowerCase();

        List<Prestamos> prestamosFiltered = loansList.stream()
                .filter(p -> p.getSocio().toLowerCase().contains(busqueda))
                .collect(Collectors.toList());

        loansTable.getItems().setAll(prestamosFiltered);
    }





}
