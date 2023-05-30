package ch.hftm;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Blog {
    @Id @GeneratedValue
    private Long id;
    String title;
    String content;

    public Blog() {

    }

    public Blog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Blog(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
