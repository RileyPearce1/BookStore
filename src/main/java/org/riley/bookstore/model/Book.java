package org.riley.bookstore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity //аннотация. указание того, что данный класс - модель, которая будет отвечать за отпределённую таблицу БД
@NoArgsConstructor
@Accessors(chain = true)
public class Book {

    @Id //аннотация - уникальный идентификатор
    @GeneratedValue(strategy = GenerationType.AUTO)
    //аннотация - при добавлении новой записи позволит генерировать новые значения внутри данного поля (автоматически)
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

    private int price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return id != null && Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

