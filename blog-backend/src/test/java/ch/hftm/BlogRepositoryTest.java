package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BlogRepositoryTest {
    @Test
    public void testDataInit() {
        BlogRepository bp = new BlogRepository();
        is(bp.count() >= 2);
    }
}
