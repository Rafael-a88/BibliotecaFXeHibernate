package org.example.bibliotecafx.Autor;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ModificarAutorController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField nacionalidadField;

    // Método para recibir los datos del autor seleccionado
    public void setAutorData(String nombre, String nacionalidad) {
        nombreField.setText(nombre);
        nacionalidadField.setText(nacionalidad);
    }



}
