package ch.hftm.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

@Entity
public class BlogUser {
    @Id @GeneratedValue
    private Long id;

    @NotBlank(message="Nickname may not be blank")
    String nickname;
    String description;
    Date signupDate;

    @Transient
    List<Link> links; // actions for HATEOAS not to be saved in db

    public BlogUser() {
        this.links = new ArrayList<>();
    }

    public BlogUser(String nickname) {
        this.nickname = nickname;
        this.signupDate = new Date(System.currentTimeMillis());
        this.links = new ArrayList<>();
    }

    public BlogUser(Long id, String nickname, String description, Date signupDate) {
        this.id = id;
        this.nickname = nickname;
        this.description = description;
        this.signupDate = signupDate;
        this.links = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
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
