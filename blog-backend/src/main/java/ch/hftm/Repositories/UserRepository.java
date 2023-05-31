package ch.hftm.Repositories;

import java.util.ArrayList;
import java.util.List;

import ch.hftm.Entities.Blog;
import ch.hftm.Entities.BlogUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<BlogUser> {
    public UserRepository() {
        
    }

    @Transactional
    public void addTestUser() {
        this.persist(new BlogUser("Rafael"));
    }

    public BlogUser getTestUser() {
        return this.findById(1L); // lazy method just return the first ever created user
    }

    @Transactional
    public void addUser(BlogUser user) {
        this.persist(user);
    }
}
