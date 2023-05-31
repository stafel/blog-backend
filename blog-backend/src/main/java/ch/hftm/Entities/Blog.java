package ch.hftm.Entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Blog {
    @Id @GeneratedValue
    private Long id;
    String title;
    String content;

    @ManyToOne(cascade = CascadeType.MERGE)
    BlogUser author;

    public Blog() {

    }

    public Blog(String title, String content, BlogUser author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Blog(Long id, String title, String content, BlogUser author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BlogUser getAuthor() {
        return author;
    }

    public void setAuthor(BlogUser author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }
}
