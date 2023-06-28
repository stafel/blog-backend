package ch.hftm;

import ch.hftm.Repositories.BlogRepository;
import ch.hftm.Repositories.PostRepository;
import ch.hftm.Repositories.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;

@ApplicationScoped
public class DataInitialization {

    @Inject
    BlogRepository blogRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Transactional
    public void init(@Observes StartupEvent event) {
        // init data if none available
        if  (blogRepository.listAll().isEmpty()) {
            blogRepository.addTestBlog();
        }

        if (userRepository.listAll().isEmpty()) {
            userRepository.addTestUser();
        }

        if (postRepository.listAll().isEmpty()) {
            postRepository.addTestPosts();
        }
    }
}
