package tests.Restricoes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import datafactory.DynamicFactory;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import template.TemplateBase;

import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@WireMockTest(httpPort = 9999)
@Feature("Testes automatizados da rota Restrições - Verbo Delete")
public class TestDeleteRestricoes extends TemplateBase {

    @Test
    public void deveRecusarDeleteRaiz(){
        Response response = delete(WIREMOCK+RESTRICOES_ENDPOINT);
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void deveRecusarDeleteCpfRestricao(){
        Response response = delete(RESTRICOES_ENDPOINT+"/"+ DynamicFactory.retornaCpfComRestricao());
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveRecusarDeleteCpfSemRestricao(){
        Response response = delete(RESTRICOES_ENDPOINT+"/"+DynamicFactory.retornaCpfSemRestricao());
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }
}
