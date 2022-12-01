package tests.Restricoes;

import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import template.TemplateRestricoes;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import java.util.ArrayList;
import java.util.Locale;

import static constants.Endpoints.RESTRICOES_ENDPOINT;
import static constants.Endpoints.WIREMOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@WireMockTest(httpPort = 9999)
public class TestGetRestricoes extends TemplateRestricoes {
    private static Faker faker = new Faker(new Locale("pt-BR"));

    @BeforeAll
    public static void defineAsRestricoes(){
        cpfRestricoes = new ArrayList<String>();
        cpfRestricoes.add("97093236014");
        cpfRestricoes.add("60094146012");
        cpfRestricoes.add("84809766080");
        cpfRestricoes.add("62648716050");
        cpfRestricoes.add("26276298085");
        cpfRestricoes.add("01317496094");
        cpfRestricoes.add("55856777050");
        cpfRestricoes.add("19626829001");
        cpfRestricoes.add("24094592008");
        cpfRestricoes.add("58063164083");
    }

    @Test
    public void deveFalharGetNaRaiz(){
        Response response = get(WIREMOCK+RESTRICOES_ENDPOINT);
        assertThat(response.statusCode(),is(405));
        assertThat(response.body().path("message"),equalTo("Método não é permitido"));
    }

    @Test
    public void devePegarCpfComRestricao(){
        String cpf = TemplateRestricoes.retornaCpfComRestricao();
        Response response = get(RESTRICOES_ENDPOINT+"/"+cpf);
        assertThat(response.statusCode(),is(200));
        assertThat(response.body().path("mensagem"), equalTo("O CPF "+cpf+" possui restrição"));
    }

    @Test
    public void devePegarCpfSemRestricao(){
        String cpf = TemplateRestricoes.retornaCpfSemRestricao();
        Response response = get(RESTRICOES_ENDPOINT+"/"+cpf);
        assertThat(response.statusCode(),is(204));
        assertThat(response.getBody().asString(),is(""));
    }
}
