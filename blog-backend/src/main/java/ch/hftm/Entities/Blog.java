package ch.hftm.Entities;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

/**
 * Top level of the full blog
 * Contains the name, short description (mission statement) and the logo used for the blog
 * Links will point to all available subresources
 */
@Entity
public class Blog {
    @Id @GeneratedValue
    private Long id;

    @NotBlank(message="Name may not be blank")
    String name;
    String description;
    String logoUrl;

    @Transient
    List<Link> links; // actions for HATEOAS not to be saved in db

    public Blog(String name, String description, String logoUrl) {
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
        this.links = new ArrayList<>();
    }

    public Blog() {
        this.links = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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
