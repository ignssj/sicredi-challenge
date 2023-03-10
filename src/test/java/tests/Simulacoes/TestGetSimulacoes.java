package tests.Simulacoes;

import datafactory.DynamicFactory;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.Simulacao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.SimulacoesService;

import static constants.Endpoints.SIMULACOES_ENDPOINT;
import static helper.ServiceHelper.matcherJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Feature("Testes automatizados da rota Simulações - Verbo Get")
public class TestGetSimulacoes extends SimulacoesService {
    private static Simulacao simulacao;

    @BeforeAll
    public static void cadastroDeSimulacoes() {
        simulacao = retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT, simulacao);
        assertThat(response.statusCode(), is(201));
    }

    @AfterAll
    public static void removeSimulacoesInseridas() {
        Response response = delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
        assertThat(response.statusCode(), is(200));
    }

    @Test
    public void deveValidarSchemaRaiz200() {
        Response responseSchema = get(SIMULACOES_ENDPOINT);
        assertThat(responseSchema.asString(), matcherJsonSchema("simulacoes", "get", 200));
    }

    @Test
    public void deveValidarSchemaSimulacaoEncontrada() {
        Response responseSchema = get(SIMULACOES_ENDPOINT+"/"+simulacao.getCpf());
        assertThat(responseSchema.asString(), matcherJsonSchema("simulacoes/cpf", "get", 200));
    }

    @Test
    public void deveValidarSchemaSimulacaoNaoEncontrada() {
        Response responseSchema = get(SIMULACOES_ENDPOINT+"/"+DynamicFactory.retornaCpf());
        assertThat(responseSchema.asString(), matcherJsonSchema("simulacoes/cpf", "get", 404));
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
        String cpfAleatorio = DynamicFactory.retornaCpf();
        Response response = get(SIMULACOES_ENDPOINT + "/"+cpfAleatorio);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"), equalTo("O CPF "+cpfAleatorio+" possui restrição")),
            () -> assertThat(response.statusCode(), is(404)));
    }

}
