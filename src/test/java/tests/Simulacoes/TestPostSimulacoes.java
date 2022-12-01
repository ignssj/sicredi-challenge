package tests.Simulacoes;

import datafactory.DynamicFactory;
import io.restassured.response.Response;
import models.Simulacao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import template.TemplateBase;

import static constants.Endpoints.SIMULACOES_ENDPOINT;
import static helper.ServiceHelper.matcherJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestPostSimulacoes extends TemplateBase {
    private static Simulacao simulacaoPrevia;

    @BeforeAll
    public static void deveCadastrarUmaSimulacao(){
        simulacaoPrevia = DynamicFactory.retornaSimulacao();
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
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertAll("simulacao",
            () -> assertThat(response.body().path("nome"), equalTo(simulacao.getNome())),
            () -> assertThat(response.body().path("cpf"), equalTo(simulacao.getCpf())),
            () -> assertThat(response.body().path("email"),equalTo(simulacao.getEmail())),
            () -> assertThat(response.body().path("valor"),equalTo(simulacao.getValor())),
            () -> assertThat(response.body().path("parcelas"),equalTo(simulacao.getParcelas())),
            () -> assertThat(response.body().path("seguro"),equalTo(simulacao.isSeguro())),
            () -> assertThat(response.statusCode(), is(201)));
    }

    @Test
    public void deveFalharCadastroSimulacaoCpfExistente(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setCpf(simulacaoPrevia.getCpf());
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"),equalTo("CPF já existe")),
            () -> assertThat(response.statusCode(), is(409)));
    }

    @Test
    public void deveFalharCpfFormatoInvalido(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setCpf("123.456.789-10");
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveFalharValorBaixo(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setValor(999);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveFalharValorAlto(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setValor(40001);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveCadastrarValorMenorQueOMaximo(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setValor(40000);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),is(201));
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
    }

    @Test
    public void deveCadastrarValorMaiorQueOMinimo(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setValor(10000);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),is(201));
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
    }

    @Test
    public void deveFalharParcelasBaixas(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setParcelas(1);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveFalharParcelasAltas(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setParcelas(49);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),not(is(201)));
    }

    @Test
    public void deveCadastrarParcelasMenorQueOMaximo(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setParcelas(48);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),is(201));
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
    }

    @Test
    public void deveCadastrarParcelasMaiorQueOMinimo(){
        Simulacao simulacao = DynamicFactory.retornaSimulacao();
        simulacao.setParcelas(2);
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),is(201));
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
    }

    @Test
    public void deveValidarSchemaCadastroIncompleto(){
        Simulacao simulacao = DynamicFactory.retornaSimulacaoVazia();
        Response response = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(response.statusCode(),is(400));
        assertThat(response.asString(), matcherJsonSchema("simulacoes", "post", 400));
    }


    }

