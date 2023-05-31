package ch.hftm.Entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BlogUser {
    @Id @GeneratedValue
    private Long id;

    String nickname;

    public BlogUser() {

    }

    public BlogUser(String nickname) {
        this.nickname = nickname;
    }

    public BlogUser(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
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
}
