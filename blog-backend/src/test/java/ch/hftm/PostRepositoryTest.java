package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;

import ch.hftm.Entities.Post;
import ch.hftm.Entities.BlogUser;
import ch.hftm.Repositories.PostRepository;
import ch.hftm.Repositories.UserRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import jakarta.inject.Inject;

@QuarkusTest
public class PostRepositoryTest {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Test
    public void testDataInit() {
        assertThat(2L, is(postRepository.count()));
    }

    @Test
    @Transactional
    public void testBlogCreation() {
        Post refBlog = new Post("Testblog", "Testblog", new Date(System.currentTimeMillis()), userRepository.getTestUser());
        postRepository.addPost(refBlog);

        Post checkBlog = postRepository.findById(refBlog.getId());

        assertThat("Testblog", is(checkBlog.getTitle()));
        assertThat("Rafael", is(checkBlog.getAuthor().getNickname()));

        postRepository.delete(checkBlog);
    }
}
