package org.example.bibliotecafx.Socios;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.bibliotecafx.HelloController;
import org.example.bibliotecafx.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AgregarSocioController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField direccionField;

    @FXML
    private TextField telefonoField;

    private HelloController mainController;

    @FXML
    private void agregarSocio(ActionEvent event) {
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();

        if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        Socio socio = new Socio(nombre, direccion, telefono);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(socio);
            transaction.commit();
            System.out.println("Socio guardado en la base de datos: " + socio);

            if (mainController != null) {
                mainController.cargarDatosSocio();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
}
