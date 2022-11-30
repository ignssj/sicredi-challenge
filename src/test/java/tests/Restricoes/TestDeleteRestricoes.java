package tests.Restricoes;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import template.TemplateRestricoes;

import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestDeleteRestricoes extends TemplateRestricoes {

    @Test
    public void deveRecusarDeleteRaiz(){
        Response response = delete(RESTRICOES_ENDPOINT);
        assertThat(response.statusCode(),is(404));
        assertThat(response.body().path("error"),is("Not Found"));
    }

    @Test
    public void deveRecusarDeleteCpfRestricao(){
        Response response = delete(RESTRICOES_ENDPOINT+"/01317496094");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveRecusarDeleteCpfSemRestricao(){
        Response response = delete(RESTRICOES_ENDPOINT+"/12345678910");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }
}
