package tests.Restricoes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import services.RestricoesService;

import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
@WireMockTest(httpPort = 9000)
@Feature("Testes automatizados da rota Restrições - Verbo Put")
public class TestPutRestricoes extends RestricoesService {
    private int wiremockPort=9000;

    @Test
    public void deveRecusarPutRaiz(){
        Response response = put(WIREMOCK+wiremockPort+RESTRICOES_ENDPOINT,"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void deveRecusarPutCpfRestricao(){
        Response response = put(RESTRICOES_ENDPOINT+"/"+ retornaCpfComRestricao(),"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveRecusarPutCpfSemRestricao(){
        Response response = put(RESTRICOES_ENDPOINT+"/"+ retornaCpfSemRestricao(),"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }
}
