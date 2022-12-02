package tests.Restricoes;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import datafactory.DynamicFactory;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;
import template.TemplateBase;
import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@WireMockTest(httpPort = 9999)
@Feature("Testes automatizados da rota Restrições - Verbo Post")
public class TestPostRestricoes extends TemplateBase{

    @Test
    public void deveRecusarPostRaiz(){
        Response response = post(WIREMOCK+RESTRICOES_ENDPOINT,"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void deveRecusarPostCpfRestricao(){
        Response response = post(RESTRICOES_ENDPOINT+"/"+DynamicFactory.retornaCpfComRestricao(),"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveRecusarPostCpfSemRestricao(){
        Response response = post(RESTRICOES_ENDPOINT+"/"+ DynamicFactory.retornaCpfSemRestricao(),"");
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }
}
