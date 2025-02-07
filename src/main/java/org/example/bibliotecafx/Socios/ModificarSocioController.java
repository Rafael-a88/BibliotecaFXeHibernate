package org.example.bibliotecafx.Socios;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bibliotecafx.HelloController;
import org.example.bibliotecafx.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ModificarSocioController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField direccionField;

    @FXML
    private TextField telefonoField;

    private Socio selectedSocio;
    private HelloController mainController;

    // Método para recibir los datos del socio seleccionado
    public void setSocioData(Socio socio) {
        this.selectedSocio = socio;
        nombreField.setText(socio.getNombre());
        direccionField.setText(socio.getDireccion());
        telefonoField.setText(socio.getTelefono());
    }

    // Método para manejar la acción de actualizar el socio
    @FXML
    private void actualizarSocio() {
        if (selectedSocio != null) {
            String updatedNombre = nombreField.getText();
            String updatedDireccion = direccionField.getText();
            String updatedTelefono = telefonoField.getText();

            // Validación básica de campos
            if (updatedNombre.isEmpty() || updatedDireccion.isEmpty() || updatedTelefono.isEmpty()) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            // Lógica para actualizar el socio en la base de datos
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                // Actualiza los atributos del socio seleccionado
                selectedSocio.setNombre(updatedNombre);
                selectedSocio.setDireccion(updatedDireccion);
                selectedSocio.setTelefono(updatedTelefono);

                session.update(selectedSocio); // Actualiza el socio en la base de datos
                transaction.commit();
                System.out.println("Socio actualizado en la base de datos: " + selectedSocio);

                // Actualizar la lista en el controlador principal
                if (mainController != null) {
                    mainController.cargarDatosSocio(); // Llama al método para recargar los datos
                }

                // Cerrar la ventana después de actualizar
                ((Stage) nombreField.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al actualizar el socio en la base de datos.");
            }
        }
    }

    // Método para mostrar un Alert
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
}
