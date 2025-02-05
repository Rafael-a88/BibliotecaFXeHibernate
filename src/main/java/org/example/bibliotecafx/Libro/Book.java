package org.example.bibliotecafx.Libro;

public class Book {
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private int year;

    // Constructor
    public Book(String title, String isbn, String author, String publisher, int year) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }

    // Getters y Setters
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

    // Método toString (opcional, para facilitar la visualización en la tabla)
    @Override
    public String toString() {
        return title + " (" + isbn + ")";
    }
}
