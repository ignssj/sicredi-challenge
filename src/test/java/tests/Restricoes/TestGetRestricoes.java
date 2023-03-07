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
import static helper.ServiceHelper.matcherJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@WireMockTest(httpPort = 9400)
@Feature("Testes automatizados da rota Restrições - Verbo Get")
public class TestGetRestricoes extends RestricoesService {
    private int wiremockPort=9400;
    @Test
    public void deveFalharGetNaRaiz(){
        Response response = get(WIREMOCK+wiremockPort+RESTRICOES_ENDPOINT);
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void devePegarCpfComRestricao(){
        String cpf = retornaCpfComRestricao();
        Response response = get(RESTRICOES_ENDPOINT+"/"+cpf);
        assertThat(response.statusCode(),is(200));
        assertThat(response.body().path("mensagem"), equalTo("O CPF "+cpf+" possui restrição"));
    }

    @Test
    public void devePegarCpfSemRestricao(){
        String cpf = retornaCpfSemRestricao();
        Response response = get(RESTRICOES_ENDPOINT+"/"+cpf);
        assertThat(response.statusCode(),is(204));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveValidarSchemaGet200(){
        Response response = get(RESTRICOES_ENDPOINT+"/"+retornaCpfComRestricao());
        assertThat(response.statusCode(),is(200));
        assertThat(response.asString(), matcherJsonSchema("restricoes", "get", 200));
    }

}
