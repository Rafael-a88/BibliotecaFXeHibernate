package org.example.bibliotecafx.Prestamos;

import jakarta.persistence.*;
import org.example.bibliotecafx.Libro.Book;

import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "libro", nullable = false)
    private Book libro;

    @Column(name = "socio", nullable = false)
    private String socio;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDate fechaDevolucion;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    // Constantes para el estado
    public static final String ESTADO_ACTIVO = "Activo";
    public static final String ESTADO_FINALIZADO = "Finalizado";

    // Constructor vacío (obligatorio para Hibernate)
    public Prestamos() {
    }

    // Constructor personalizado
    public Prestamos(Book libro, String socio, LocalDate fechaPrestamo, LocalDate fechaDevolucion, String estado) {
        this.libro = libro;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getLibro() {
        return libro;
    }

    public void setLibro(Book libro) {
        this.libro = libro;
    }

    public String getSocio() {
        return socio;
    }

    public void setSocio(String socio) {
        this.socio = socio;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
