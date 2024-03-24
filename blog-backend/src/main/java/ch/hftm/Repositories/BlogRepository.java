package ch.hftm.Repositories;

import java.util.ArrayList;
import java.util.List;

import ch.hftm.Entities.Post;
import ch.hftm.Entities.Blog;
import ch.hftm.Entities.BlogUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {

    MeterRegistry registry = Metrics.globalRegistry;

    Counter blogRequests;

    public BlogRepository() {
        // a counter for the number of returns of blog https://quarkus.io/guides/telemetry-micrometer
        blogRequests = Counter.builder("blog.requests")
            .baseUnit("connections") // see https://github.com/micrometer-metrics/micrometer/blob/main/micrometer-core/src/main/java/io/micrometer/core/instrument/binder/BaseUnits.java
            .description("number of requests to the blog endpoint since startup")
            .tags("blog", "test")
            .register(registry);
    }

    @Transactional
    public void addTestBlog() {
        this.persist(new Blog("Alpacalypse", "Something about AI Now", ""));
    }

    public Blog getBlog() {
        blogRequests.increment(); // here we increment our custom counter
        return this.findById(1L); // lazy method just return the first ever created blog
    }

    @Transactional
    public void addBlog(Blog blog) {
        this.persist(blog);
    }

    @Transactional
    public void updateBlog(Blog blog) {
        // lazy way just load the original blog and modify its values
        Blog originalBlog = this.getBlog();
        originalBlog.setName(blog.getName());
        originalBlog.setDescription(blog.getDescription());
        originalBlog.setLogoUrl(blog.getLogoUrl());
        blog = originalBlog;
        this.persist(blog);
    }
}
