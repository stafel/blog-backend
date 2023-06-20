package ch.hftm;

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
    UserRepository userRepository;

    @Inject
    PostRepository blogRepository;

    @Transactional
    public void init(@Observes StartupEvent event) {
        // init data if none available
        if (userRepository.listAll().isEmpty()) {
            userRepository.addTestUser();
        }

        if (blogRepository.listAll().isEmpty()) {
            blogRepository.addTestPosts();
        }
    }
}
