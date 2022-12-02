package tests.Restricoes;

import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import datafactory.DynamicFactory;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import template.TemplateBase;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import java.util.ArrayList;
import java.util.Locale;

import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static helper.ServiceHelper.matcherJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@WireMockTest(httpPort = 9999)
public class TestGetRestricoes extends TemplateBase {

    @Test
    public void deveFalharGetNaRaiz(){
        Response response = get(WIREMOCK+RESTRICOES_ENDPOINT);
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void devePegarCpfComRestricao(){
        String cpf = DynamicFactory.retornaCpfComRestricao();
        Response response = get(RESTRICOES_ENDPOINT+"/"+cpf);
        assertThat(response.statusCode(),is(200));
        assertThat(response.body().path("mensagem"), equalTo("O CPF "+cpf+" possui restrição"));
    }

    @Test
    public void devePegarCpfSemRestricao(){
        String cpf = DynamicFactory.retornaCpfSemRestricao();
        Response response = get(RESTRICOES_ENDPOINT+"/"+cpf);
        assertThat(response.statusCode(),is(204));
        assertThat(response.getBody().asString(),is(""));
    }

    @Test
    public void deveValidarSchemaGet200(){
        Response response = get(RESTRICOES_ENDPOINT+"/"+DynamicFactory.retornaCpfComRestricao());
        assertThat(response.statusCode(),is(200));
        assertThat(response.asString(), matcherJsonSchema("restricoes", "get", 200));
    }

}
