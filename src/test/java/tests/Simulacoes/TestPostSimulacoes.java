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

@Feature("Testes automatizados da rota Simulações - Verbo Post")
public class TestPostSimulacoes extends SimulacoesService {
    private static Simulacao simulacaoPrevia;

    @BeforeAll
    public static void deveCadastrarUmaSimulacao(){
        simulacaoPrevia = retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT, simulacaoPrevia);
        assertThat(response.statusCode(), is(201));
    }

    @AfterAll
    public static void deveDeletarSimulacao(){
        Response response = delete(SIMULACOES_ENDPOINT + "/" + simulacaoPrevia.getCpf());
        assertThat(response.statusCode(), is(200));
    }

    @Test
    public void deveCadastrarSimulacaoRetornandoDados(){
        Simulacao simulacao = retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        int id = response.then().extract().path("id");
        assertAll("simulacao",
            () -> assertThat(response.body().path("nome"), equalTo(simulacao.getNome())),
            () -> assertThat(response.body().path("cpf"), equalTo(simulacao.getCpf())),
            () -> assertThat(response.body().path("email"),equalTo(simulacao.getEmail())),
            () -> assertThat(response.body().path("valor"),equalTo(simulacao.getValor())),
            () -> assertThat(response.body().path("parcelas"),equalTo(simulacao.getParcelas())),
            () -> assertThat(response.body().path("seguro"),equalTo(simulacao.isSeguro())),
            () -> assertThat(response.statusCode(), is(201)));

        Response responseDelete = delete(SIMULACOES_ENDPOINT+"/"+id);
        assertThat(responseDelete.statusCode(),is(200));
    }

    @Test
    public void deveFalharCadastroSimulacaoCpfExistente(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setCpf(simulacaoPrevia.getCpf());
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"),equalTo("CPF já existe")),
            () -> assertThat(response.statusCode(), is(409)));
    }

    @Test
    public void deveFalharCpfFormatoInvalido(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setCpf(DynamicFactory.retornaCpf());
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveFalharValorBaixo(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(999);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveFalharValorAlto(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(40001);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveCadastrarValorMenorQueOMaximo(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(40000);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        int id = response.then().extract().path("id");
        assertThat(response.statusCode(),is(201));
        Response responseDelete = delete(SIMULACOES_ENDPOINT+"/"+id);
        assertThat(responseDelete.statusCode(),is(200));
    }

    @Test
    public void deveCadastrarValorMaiorQueOMinimo(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(10000);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        int id = response.then().extract().path("id");
        assertThat(response.statusCode(),is(201));
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
        Response responseDelete = delete(SIMULACOES_ENDPOINT+"/"+id);
        assertThat(responseDelete.statusCode(),is(200));
    }

    @Test
    public void deveFalharParcelasBaixas(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(1);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveFalharParcelasAltas(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(49);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveCadastrarParcelasMenorQueOMaximo(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(48);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        int id = response.then().extract().path("id");
        assertThat(response.statusCode(),is(201));
        Response responseDelete = delete(SIMULACOES_ENDPOINT+"/"+id);
        assertThat(responseDelete.statusCode(),is(200));
    }

    @Test
    public void deveCadastrarParcelasMaiorQueOMinimo(){
        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(2);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        int id = response.then().extract().path("id");
        assertThat(response.statusCode(),is(201));
        Response responseDelete = delete(SIMULACOES_ENDPOINT+"/"+id);
        assertThat(responseDelete.statusCode(),is(200));
    }

    @Test
    public void deveValidarSchemaCadastroIncompleto(){
        Simulacao simulacao = retornaSimulacaoVazia();
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),is(400));
        assertThat(response.asString(), matcherJsonSchema("simulacoes", "post", 400));
    }

    @Test
    public void deveValidarSchema201(){
        Simulacao simulacao = retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),is(201));
        assertThat(response.asString(), matcherJsonSchema("simulacoes", "post", 201));
    }

    }

