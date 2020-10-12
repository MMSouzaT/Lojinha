package testes;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class LojinhaAPITest {

    private String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://165.227.93.41/";
        RestAssured.basePath = "lojinha";

        token = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                        " \"usuariologin\": \"marciosouza\",\n" +
                        " \"usuariosenha\": \"123456\"\n" +
                        "}")
                .when()
                    .post("login")
                .then()
                    .extract()
                        .path("data.token");

    }
    @Test
    public void testBuscarDadosDeUmProdutoEspecifico(){

        RestAssured
                .given()
                    .header("token", token)
                .when()
                    .get("produto/8397")
                .then()
                    .assertThat()
                        .statusCode(200)
                        .body("data.produtonome", Matchers.equalTo("Casa"))
                        .body("data.componentes[0].componentenome", Matchers.equalTo("TV"))
                        .body("message", Matchers.equalTo("Detalhando dados do produto"));

    }

    @Test
    public void cadastrandoUmProduto(){
        RestAssured
                .given()
                    .header("token", token)
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                            "    \"produtonome\": \"Computador\",\n" +
                            "    \"produtovalor\": 1300.00,\n" +
                            "    \"produtocores\": [\n" +
                            "        \"Preto\"\n" +
                            "    ],\n" +
                            "    \"componentes\": [\n" +
                            "        {\n" +
                            "            \"componentenome\": \"Caixa de som\",\n" +
                            "            \"componentequantidade\": 2\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"componentenome\": \"Teclado\",\n" +
                            "            \"componentequantidade\": 1\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}")
                .when()
                    .post("produto")
                .then()
                    .assertThat()
                        .statusCode(201)
                        .body("data.produtonome", Matchers.equalTo("Computador"))
                        .body("data.componentes[0].componentenome", Matchers.equalTo("Caixa de som"))
                        .body("message", Matchers.equalTo("Produto adicionado com sucesso"));
        }
}
