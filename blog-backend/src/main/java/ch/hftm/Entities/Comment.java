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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;

    String text;

    @ManyToOne(cascade = CascadeType.MERGE)
    Post post;

    @ManyToOne(cascade = CascadeType.MERGE)
    BlogUser author;

    @Transient
    List<Link> links; // actions for HATEOAS not to be saved in db

    public Comment(Long id, String text, Post post, BlogUser author) {
        this.id = id;
        this.text = text;
        this.post = post;
        this.author = author;
    }

    public Comment(String text, Post post, BlogUser author) {
        this.text = text;
        this.post = post;
        this.author = author;
    }

    public Comment() {
    }

    public Comment(Long id, String text, BlogUser author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

    public Comment(String text, BlogUser author) {
        this.text = text;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BlogUser getAuthor() {
        return author;
    }

    public void setAuthor(BlogUser author) {
        this.author = author;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}