package tests.Simulacoes;

import datafactory.DynamicFactory;
import io.restassured.response.Response;
import models.Simulacao;
import org.junit.jupiter.api.Test;
import template.TemplateBase;

import static constants.Endpoints.SIMULACOES_ENDPOINT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestDeleteSimulacoes extends TemplateBase {
    private Simulacao simulacao;
    @Test
    public void deveDeletarSimulacao(){
        simulacao = DynamicFactory.retornaSimulacao();
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
        simulacao = DynamicFactory.retornaSimulacao();
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
