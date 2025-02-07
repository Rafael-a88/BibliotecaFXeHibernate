package org.example.bibliotecafx.Libro;

import jakarta.persistence.*;
import org.example.bibliotecafx.Prestamos.Prestamos;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String title;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "autor", nullable = false)
    private String author;

    @Column(name = "editorial", nullable = false)
    private String publisher;

    @Column(name = "anio_publicacion", nullable = false)
    private int year;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prestamos> prestamos = new ArrayList<>();

    // Constructor vacío (obligatorio para Hibernate)
    public Book() {
    }

    // Constructor personalizado
    public Book(String title, String isbn, String author, String publisher, int year) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Prestamos> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamos> prestamos) {
        this.prestamos = prestamos;
    }

    @Override
    public String toString() {
        return "Título: " + title + ", ISBN: " + isbn + ", Autor: " + author +
                ", Editorial: " + publisher + ", Año de Publicación: " + year;
    }
}
