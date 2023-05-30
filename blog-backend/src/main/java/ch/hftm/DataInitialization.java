package ch.hftm;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataInitialization {
    @Transactional
    public void init(@Observes StartupEvent event) {
        // init data if none available
        BlogRepository bp = new BlogRepository();

        if (bp.listAll().isEmpty()) {
            bp.addTestBlogs();
        }
    }
}
