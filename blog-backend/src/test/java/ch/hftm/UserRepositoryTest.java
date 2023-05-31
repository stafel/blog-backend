package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;

import ch.hftm.Entities.Blog;
import ch.hftm.Entities.BlogUser;
import ch.hftm.Repositories.BlogRepository;
import ch.hftm.Repositories.UserRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import jakarta.inject.Inject;

@QuarkusTest
public class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    @Test
    public void testDataInit() {
        is(userRepository.count() >= 1);
    }

    @Test
    @Transactional
    public void testBlogCreation() {
        BlogUser refUser = new BlogUser("Gigacado33");
        userRepository.addUser(refUser);

        BlogUser checkUser = userRepository.findById(refUser.getId());

        is(checkUser.getNickname().equals("Gigacado33"));

        userRepository.delete(checkUser);
    }
}
