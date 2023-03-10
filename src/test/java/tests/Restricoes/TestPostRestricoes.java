package tests.Restricoes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import services.RestricoesService;
import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@WireMockTest(httpPort = 9200)
@Feature("Testes automatizados da rota Restrições - Verbo Post")
public class TestPostRestricoes extends RestricoesService{
    private int wiremockPort=9200;

    @Test
    public void deveRecusarPostRaiz(){
        Response response = post(WIREMOCK+wiremockPort+RESTRICOES_ENDPOINT,"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void deveRecusarPostCpfRestricao(){
        Response response = post(RESTRICOES_ENDPOINT+"/"+ retornaCpfComRestricao(),"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveRecusarPostCpfSemRestricao(){
        Response response = post(RESTRICOES_ENDPOINT+"/"+ retornaCpfSemRestricao(),"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }
}
