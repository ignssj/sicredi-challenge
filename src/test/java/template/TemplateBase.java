package template;

import helper.EnvironmentConfig;
import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import services.RestricoesService;


import java.util.ArrayList;

import static io.restassured.RestAssured.*;
@Link(name = "Local",type = "http://localhost:8080")
@Epic("Análise e testes - API Sicredi")
public class TemplateBase {

    @BeforeAll
    public static void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(EnvironmentConfig.getProperty("url",""))
                .setContentType(ContentType.JSON)
                .build();
            ArrayList<String> cpfRestricoes = new ArrayList<String>();
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
            RestricoesService.setCpfRestricoes(cpfRestricoes);
        }

    public static Response get(String endpoint){
        return
                given()
                        .when()
                        .get(endpoint)
                        .then()
                        .extract()
                        .response();
    }

    public static Response delete(String endpoint){
        return
                given()
                        .when()
                        .delete(endpoint)
                        .then()
                        .extract()
                        .response();
    }

    public static Response post(String endpoint, Object obj){
        return
                given()
                        .body(obj)
                        .when()
                        .post(endpoint)
                        .then()
                        .extract()
                        .response();
    }

    public static Response put(String endpoint,Object obj){
        return
                given()
                        .body(obj)
                        .when()
                        .put(endpoint)
                        .then()
                        .extract()
                        .response();
    }

}
