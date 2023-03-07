package tests.Simulacoes;

import datafactory.DynamicFactory;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.Simulacao;
import org.junit.jupiter.api.Test;
import services.SimulacoesService;
import template.TemplateBase;

import static constants.Endpoints.SIMULACOES_ENDPOINT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Feature("Testes automatizados da rota Simulações - Verbo Delete")
public class TestDeleteSimulacoes extends SimulacoesService {

    @Test
    public void deveDeletarSimulacao(){
        Simulacao simulacao = retornaSimulacao();
        Response cadastro = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(cadastro.statusCode(),is(201));
        int id = cadastro.body().path("id");
        Response response = delete(SIMULACOES_ENDPOINT+"/"+id);
        assertAll("simulacao",
            () -> assertThat(response.body().asString(), equalTo("OK")),
            () -> assertThat(response.statusCode(), is(204)));
    }

    @Test
    public void deveFalharDeleteSimulacao(){
        Simulacao simulacao = retornaSimulacao();
        Response cadastro = post(SIMULACOES_ENDPOINT,simulacao);
        assertThat(cadastro.statusCode(),is(201));
        int id = cadastro.body().path("id");
        Response response = delete(SIMULACOES_ENDPOINT+"/"+id);
        assertAll("simulacao",
            () -> assertThat(response.body().path("mensagem"), equalTo("CPF não encontrado")),
            () -> assertThat(response.statusCode(), is(404)));
    }

    @Test
    public void deveFalharDeleteRaiz(){
        Response response = delete(SIMULACOES_ENDPOINT);
        assertThat(response.statusCode(),is(405));
        assertThat(response.getBody().asString(),is(""));
    }


}
