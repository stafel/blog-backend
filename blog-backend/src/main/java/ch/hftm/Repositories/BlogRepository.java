package ch.hftm.Repositories;

import java.util.ArrayList;
import java.util.List;

import ch.hftm.Entities.Post;
import ch.hftm.Entities.Blog;
import ch.hftm.Entities.BlogUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {
    public BlogRepository() {
        
    }

    @Transactional
    public void addTestBlog() {
        this.persist(new Blog("Alpacalypse", "Something about AI Now", ""));
    }

    public Blog getBlog() {
        return this.findById(1L); // lazy method just return the first ever created blog
    }
}
