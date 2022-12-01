package tests.Simulacoes;

import com.github.javafaker.Faker;
import datafactory.DynamicFactory;
import io.restassured.response.Response;
import models.Simulacao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import template.TemplateBase;


import java.util.Locale;

import static constants.Endpoints.SIMULACOES_ENDPOINT;
import static helper.ServiceHelper.matcherJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGetSimulacoes extends TemplateBase {
    private static Simulacao simulacao;
    private static Faker faker = new Faker(new Locale("pt-BR"));

    @BeforeAll
    public static void cadastroDeSimulacoes() {
        simulacao = DynamicFactory.retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT, simulacao);
        assertThat(response.statusCode(), is(201));
    }

    @AfterAll
    public static void removeSimulacoesInseridas() {
        Response response = delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
        assertThat(response.statusCode(), is(200));
    }

    @Test
    public void deveValidarSchema() {
        Response responseSchema = get(SIMULACOES_ENDPOINT);
        assertThat(responseSchema.asString(), matcherJsonSchema("simulacoes", "get", 200));
    }

    @Test
    public void deveRetornarSimulacoes() {
        Response response = get(SIMULACOES_ENDPOINT);
        response.then().body("$", hasSize(greaterThanOrEqualTo(1)));
        response.then().body("cpf", hasItem(simulacao.getCpf()));
    }

    @Test
    public void deveRetornarSimulacaoExistente() {
        Response response = get(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
        assertAll("simulacao",
            () -> assertThat(response.body().path("nome"), equalTo(simulacao.getNome())),
            () -> assertThat(response.body().path("cpf"), equalTo(simulacao.getCpf())),
            () -> assertThat(response.body().path("email"),equalTo(simulacao.getEmail())),
            () -> assertThat(response.body().path("valor"),equalTo(simulacao.getValor())),
            () -> assertThat(response.body().path("parcelas"),equalTo(simulacao.getParcelas())),
            () -> assertThat(response.body().path("seguro"),equalTo(simulacao.isSeguro())),
            () -> assertThat(response.statusCode(), is(200)));
    }

    @Test
    public void deveFalharSimulacaoInexistente() {
        String cpfAleatorio = faker.numerify("###########");
        Response response = get(SIMULACOES_ENDPOINT + "/"+cpfAleatorio);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"), equalTo("O CPF "+cpfAleatorio+" possui restrição")),
            () -> assertThat(response.statusCode(), is(404)));
    }

}
