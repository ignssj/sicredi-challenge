package tests.Restricoes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import template.TemplateRestricoes;

import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
@WireMockTest(httpPort = 9999)
public class TestPutRestricoes extends TemplateRestricoes {

    @Test
    public void deveRecusarPutRaiz(){
        Response response = put(WIREMOCK+RESTRICOES_ENDPOINT,"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void deveRecusarPutCpfRestricao(){
        Response response = put(RESTRICOES_ENDPOINT+"/01317496094","");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveRecusarPutCpfSemRestricao(){
        Response response = put(RESTRICOES_ENDPOINT+"/12345678910","");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }
}
