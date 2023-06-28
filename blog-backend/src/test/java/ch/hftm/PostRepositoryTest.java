package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;

import ch.hftm.Entities.Post;
import ch.hftm.Entities.BlogUser;
import ch.hftm.Repositories.PostRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import jakarta.inject.Inject;

@QuarkusTest
public class PostRepositoryTest {

    @Inject
    PostRepository postRepository;

    @Test
    public void testDataInit() {
        is(postRepository.count() >= 2);
    }

    @Test
    @Transactional
    public void testBlogCreation() {
        Post refBlog = new Post("Testblog", "Testblog", new Date(System.currentTimeMillis()), new BlogUser("FireFox123"));
        postRepository.addPost(refBlog);

        Post checkBlog = postRepository.findById(refBlog.getId());

        is(checkBlog.getTitle().equals("Testblog"));
        is(checkBlog.getAuthor().getNickname().equals("FireFox123"));

        postRepository.delete(checkBlog);
    }
}
