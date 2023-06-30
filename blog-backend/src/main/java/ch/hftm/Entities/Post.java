package ch.hftm.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;

    @NotBlank(message="Title may not be blank")
    String title;
    String content;
    Date creationDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    BlogUser author;

    @Transient
    List<Link> links; // actions for HATEOAS not to be saved in db

    public Post() {
        this.links = new ArrayList<>();
    }

    public Post(String title, String content, Date creationDate, BlogUser author) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.author = author;
        this.links = new ArrayList<>();
    }

    public Post(Long id, String title, String content, Date creationDate, BlogUser author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.author = author;
        this.links = new ArrayList<>();
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

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }
}
