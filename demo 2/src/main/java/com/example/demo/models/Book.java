package com.example.demo.models;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@Entity //аннотация. указание того, что данный класс - модель, которая будет отвечать за отпределённую таблицу БД
public class Book {

    @Id //аннотация - уникальный идентификатор
    @GeneratedValue(strategy = GenerationType.AUTO) //аннотация - при добавлении новой записи позволит генерировать новые значения внутри данного поля (автоматически)
    private Long id; //поле - уникальные идентификатор (Long - тип данных)
    private String img, title, author; // поле - название статьи, анонс
    @Column(length = 10000)
    private String fullText;
    private Date date;
    private String ISBN;

    @OneToOne
    private Genre genre;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Review> comments;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Review> getComments() {
        return comments;
    }

    public void setComments(List<Review> comments) {
        this.comments = comments;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int price;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg(){
        return img;
    }

    public  void setImg(String img){
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Book() {
    }

    public Book(String img, String title, String fullText, Date date, String author,Genre genre, int price,String ISBN) {
        this.img = img;
        this.title = title;
        this.fullText = fullText;
        this.date = date;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.ISBN = ISBN;
    }
}

