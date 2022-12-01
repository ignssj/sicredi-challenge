package tests.Restricoes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;
import template.TemplateRestricoes;
import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@WireMockTest(httpPort = 9999)
public class TestPostRestricoes extends TemplateRestricoes{

    @Test
    public void deveRecusarPostRaiz(){
        Response response = post(WIREMOCK+RESTRICOES_ENDPOINT,"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void deveRecusarPostCpfRestricao(){
        Response response = post(RESTRICOES_ENDPOINT+"/01317496094","");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveRecusarPostCpfSemRestricao(){
        Response response = post(RESTRICOES_ENDPOINT+"/12345678910","");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }
}
