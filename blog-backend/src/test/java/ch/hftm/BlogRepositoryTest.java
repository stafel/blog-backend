package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;

import ch.hftm.Entities.Blog;
import ch.hftm.Entities.BlogUser;
import ch.hftm.Repositories.BlogRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import jakarta.inject.Inject;

@QuarkusTest
public class BlogRepositoryTest {

    @Inject
    BlogRepository blogRepository;

    @Test
    public void testDataInit() {
        is(blogRepository.count() >= 2);
    }

    @Test
    @Transactional
    public void testBlogCreation() {
        Blog refBlog = new Blog("Testblog", "Testblog", new BlogUser("FireFox123"));
        blogRepository.addBlog(refBlog);

        Blog checkBlog = blogRepository.findById(refBlog.getId());

        is(checkBlog.getTitle().equals("Testblog"));
        is(checkBlog.getAuthor().getNickname().equals("FireFox123"));

        blogRepository.delete(checkBlog);
    }
}
