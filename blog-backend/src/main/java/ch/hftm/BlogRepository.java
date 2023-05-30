package ch.hftm;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {
    public BlogRepository() {
        
    }

    public void addTestBlogs() {
        this.persist(new Blog("First", "Hello World"));
        this.persist(new Blog("Second", "First!!1!"));
    }

    public List<Blog> getBlogs() {
        return this.listAll();
    }

    @Transactional
    public void addBlog(Blog blog) {
        this.persist(blog);
    }
}
