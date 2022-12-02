package tests.Simulacoes;

import com.github.javafaker.Faker;
import datafactory.DynamicFactory;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.Simulacao;
import org.junit.jupiter.api.BeforeAll;
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
public class TestPutSimulacoes extends TemplateBase {

    private static Simulacao simulacaoPrevia;
    private static Faker faker = new Faker(new Locale("pt-BR"));


    @BeforeAll
    public static void deveCadastrarSimulacao(){
        simulacaoPrevia = SimulacoesService.retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT,simulacaoPrevia);
        assertThat(response.statusCode(),is(201));
    }

    @Test
    public void deveAlterarSimulacao(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        simulacaoPrevia = simulacao;
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
        String cpfInexistente = faker.numerify("###########");
        Response responseGet = get(SIMULACOES_ENDPOINT+"/"+cpfInexistente);
        assertThat(responseGet.statusCode(),is(404));
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+cpfInexistente,simulacao);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"), equalTo("CPF não encontrado")),
            () -> assertThat(response.statusCode(), is(404)));
    }

    @Test
    public void deveFalharPutCpfJaExiste(){
        Simulacao novaSimulacao = SimulacoesService.retornaSimulacao();
        Response responsePost = post(SIMULACOES_ENDPOINT,novaSimulacao);
        assertThat(responsePost.statusCode(),is(201));

        Response response = put(SIMULACOES_ENDPOINT+"/"+novaSimulacao.getCpf(),simulacaoPrevia);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"), equalTo("CPF já existe")),
            () -> assertThat(response.statusCode(), is(409)));
    }

    @Test
    public void deveFalharPutCpfFormatoInvalido(){
        Simulacao simulacaoTemporaria = SimulacoesService.retornaSimulacao();
        Response response = post(SIMULACOES_ENDPOINT,simulacaoTemporaria);
        assertThat(response.statusCode(),is(201));

        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setCpf(faker.numerify("###.###.###-##"));
        response = put(SIMULACOES_ENDPOINT+"/"+simulacaoTemporaria.getCpf(),simulacao);
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveFalharPutValorBaixo(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setValor(999);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        simulacaoPrevia = simulacao;
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveFalharPutValorAlto(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setValor(40001);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        simulacaoPrevia = simulacao;
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveEditarValorMenorQueOMaximo(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setValor(40000);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        simulacaoPrevia = simulacao;
    }

    @Test
    public void deveEditarValorMaiorQueOMinimo(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setValor(10000);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        simulacaoPrevia = simulacao;
    }

    @Test
    public void deveFalharPutParcelasBaixas(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setParcelas(1);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveFalharPutParcelasAltas(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setParcelas(49);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        simulacaoPrevia = simulacao;
        assertThat(response.statusCode(),not(is(200)));
    }

    @Test
    public void deveEditarParcelasMenorQueOMaximo(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setParcelas(48);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        simulacaoPrevia = simulacao;
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
    }

    @Test
    public void deveEditarParcelasMaiorQueOMinimo(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        simulacao.setParcelas(2);
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        simulacaoPrevia = simulacao;
        delete(SIMULACOES_ENDPOINT + "/" + simulacao.getCpf());
    }

    @Test
    public void deveValidarSchemaSimulacaoEditada(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+simulacaoPrevia.getCpf(),simulacao);
        assertThat(response.statusCode(),is(200));
        assertThat(response.asString(), matcherJsonSchema("simulacoes", "put", 200));
    }

    @Test
    public void deveValidarSchemaSimulacaoInexistente(){
        Simulacao simulacao = SimulacoesService.retornaSimulacao();
        Response response = put(SIMULACOES_ENDPOINT+"/"+faker.numerify("############"),simulacao);
        assertThat(response.statusCode(),is(404));
        assertThat(response.asString(), matcherJsonSchema("simulacoes", "put", 404));
    }
}
