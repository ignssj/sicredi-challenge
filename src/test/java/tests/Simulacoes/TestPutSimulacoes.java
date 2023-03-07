package tests.Simulacoes;

import com.github.javafaker.Faker;
import datafactory.DynamicFactory;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.Simulacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SimulacoesService;
import template.TemplateBase;

import java.util.Locale;

import static constants.Endpoints.SIMULACOES_ENDPOINT;
import static helper.ServiceHelper.matcherJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Feature("Testes automatizados da rota Simulações - Verbo Put")
public class TestPutSimulacoes extends SimulacoesService {

    @Test
    public void deveAlterarSimulacao(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        delete(SIMULACOES_ENDPOINT+"/"+simulacao.getCpf());
        assertAll("simulacao",
            () -> assertThat(response.body().path("nome"), equalTo(simulacao.getNome())),
            () -> assertThat(response.body().path("cpf"), equalTo(simulacao.getCpf())),
            () -> assertThat(response.body().path("email"),equalTo(simulacao.getEmail())),
            () -> assertThat(response.body().path("valor"),equalTo(simulacao.getValor())),
            () -> assertThat(response.body().path("parcelas"),equalTo(simulacao.getParcelas())),
            () -> assertThat(response.body().path("seguro"),equalTo(simulacao.isSeguro())),
            () -> assertThat(response.statusCode(),is(200)));
    }

    @Test
    public void deveFalharPutSimulacaoInexistente(){
        String cpfInexistente = DynamicFactory.retornaCpf();
        Response responseGet = get(SIMULACOES_ENDPOINT+"/"+cpfInexistente);
        assertThat(responseGet.statusCode(),is(404));
        Simulacao simulacao = retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+cpfInexistente,simulacao);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"), equalTo("CPF não encontrado")),
            () -> assertThat(response.statusCode(), is(404)));
    }

    @Test
    public void deveFalharPutCpfJaExiste(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao novaSimulacao = retornaSimulacao();
        Response responsePost = post(SIMULACOES_ENDPOINT,novaSimulacao);
        assertThat(responsePost.statusCode(),is(201));

        Response response = put(SIMULACOES_ENDPOINT+"/"+novaSimulacao.getCpf(),simulacaoPrevia);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"), equalTo("CPF já existe")),
            () -> assertThat(response.statusCode(), is(409)));

        delete(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf());
        delete(SIMULACOES_ENDPOINT+"/"+novaSimulacao.getCpf());
    }

    @Test
    public void deveFalharPutCpfFormatoInvalido(){
        Simulacao simulacaoTemporaria = retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT,simulacaoTemporaria);
        assertThat(response.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setCpf(DynamicFactory.retornaCpfInvalido());
        response = put(SIMULACOES_ENDPOINT+"/"+simulacaoTemporaria.getCpf(),simulacao);
        assertThat(response.statusCode(),not(is(200)));
        // não adianta tentar deletar
    }

    @Test
    public void deveFalharPutValorBaixo(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(999);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        delete(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf());
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveFalharPutValorAlto(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(40001);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        delete(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf());
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveEditarValorMenorQueOMaximo(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(40000);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        delete(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf());
    }

    @Test
    public void deveEditarValorMaiorQueOMinimo(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setValor(10000);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        delete(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf());
        assertThat(response.statusCode(),is(200));
    }

    @Test
    public void deveFalharPutParcelasBaixas(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(1);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        delete(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf());
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveFalharPutParcelasAltas(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(49);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        delete(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf());
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveEditarParcelasMenorQueOMaximo(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(48);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
        assertThat(response.statusCode(),is(200));
    }

    @Test
    public void deveEditarParcelasMaiorQueOMinimo(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        simulacao.setParcelas(2);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
    }

    @Test
    public void deveValidarSchemaSimulacaoEditada(){
        Simulacao simulacaoPrevia = retornaSimulacao();
        Response responseCadastro = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(responseCadastro.statusCode(),is(201));

        Simulacao simulacao = retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        delete(SIMULACOES_ENDPOINT+"/"+simulacao.getCpf());
        assertThat(response.asString(), matcherJsonSchema("simulacoes", "put", 200));
    }

    @Test
    public void deveValidarSchemaSimulacaoInexistente(){
        Simulacao simulacao = retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+DynamicFactory.retornaCpf(),simulacao);
        assertThat(response.statusCode(),is(404));
        assertThat(response.asString(), matcherJsonSchema("simulacoes", "put", 404));
    }
}
