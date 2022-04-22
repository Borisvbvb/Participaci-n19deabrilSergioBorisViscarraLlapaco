
package com.emergentes.modelo;

public class Libro {
    private int id ;
    private String isbn;
    private String titulo;
    private String categoria;

    public Libro() {
        this.id = 0;
        this.isbn = "";
        this.titulo = "";
        this.categoria = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    
}
