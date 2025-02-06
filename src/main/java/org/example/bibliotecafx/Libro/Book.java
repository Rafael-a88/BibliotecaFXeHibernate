package org.example.bibliotecafx.Libro;

import jakarta.persistence.*;

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

    // Constructor
    public Book(String title, String isbn, String author, String publisher, int year) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }

    public Book() {

    }

    // Getters y Setters
    public Long getId() {return id; }

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


    @Override
    public String toString() {
        return "Título: " + title + ", ISBN: " + isbn + ", Autor: " + author +
                ", Editorial: " + publisher + ", Año de Publicación: " + year;
    }

}
