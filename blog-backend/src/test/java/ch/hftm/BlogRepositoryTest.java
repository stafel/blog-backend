package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;

import ch.hftm.Entities.Blog;
import ch.hftm.Entities.BlogUser;
import ch.hftm.Repositories.BlogRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BlogRepositoryTest {
    @Test
    public void testDataInit() {
        BlogRepository br = new BlogRepository();
        is(br.count() >= 2);
    }

    @Test
    @Transactional
    public void testBlogCreation() {
        BlogRepository br = new BlogRepository();
        br.addBlog(new Blog(987654321L, "Testblog", "Testblog", new BlogUser("Toastuser321")));

        Blog checkBlog = br.findById(987654321L);

        is(checkBlog.getAuthor().getNickname().equals("Toastuser321"));

        br.delete(checkBlog);
    }
}
