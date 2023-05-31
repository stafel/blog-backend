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

@QuarkusTest
public class UserRepositoryTest {
    @Test
    public void testDataInit() {
        UserRepository ur = new UserRepository();
        is(ur.count() >= 1);
    }

    @Test
    @Transactional
    public void testBlogCreation() {
        UserRepository ur = new UserRepository();
        ur.addUser(new BlogUser(67676767L, "Gigacado33"));

        BlogUser checkUser = ur.findById(67676767L);

        is(checkUser.getNickname().equals("Gigacado33"));

        ur.delete(checkUser);
    }
}
