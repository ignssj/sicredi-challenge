import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import template.TemplateBase;


import java.util.Locale;

import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestRestricoes extends TemplateBase {
    private static Faker faker = new Faker(Locale.ENGLISH);

    @Test
    public void deveFalharGetNaRaiz(){
        Response response = get(RESTRICOES_ENDPOINT);
        assertThat(response.statusCode(),is(404));
    }
}
