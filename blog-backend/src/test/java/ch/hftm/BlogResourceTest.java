package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import ch.hftm.Repositories.BlogRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BlogResourceTest {

    @Inject
    BlogRepository blogRepository;

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/blog")
          .then()
             .statusCode(200);
             //.body(is(blogRepository.getBlog()));
    }

}