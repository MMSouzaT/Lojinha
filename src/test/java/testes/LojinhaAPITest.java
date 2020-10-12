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

    @Test
    public void testBuscarTodosProdutos(){
        RestAssured
            .given()
                .header("token", token)
            .when()
                .get("produto")
            .then()
                .assertThat()
                    .statusCode(200)
                    .body("data.produtoid[0]", Matchers.equalTo(8397))
                    .body("message", Matchers.equalTo("Listagem de produtos realizada com sucesso"));
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
    public void testBuscarComponenteDoProdutoComFiltro(){

        RestAssured
                .given()
                .header("token", token)
                .when()
                .get("produto/8397?=TV")
                .then()
                .assertThat()
                .statusCode(200)
                .body("data.produtoid", Matchers.equalTo(8397))
                .body("message", Matchers.equalTo("Detalhando dados do produto"));
    }

    @Test
    public void testAlterarProduto(){
        RestAssured
                .given()
                    .header("token", token)
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                            " \"produtonome\": \"Massarico\",\n" +
                            " \"produtovalor\": 500.00,\n" +
                            " \"produtocores\": [\n" +
                            " \"Dourado\"\n" +
                            " ],\n" +
                            " \"componentes\": [\n" +
                            " {\n" +
                            " \"componentenome\": \"Tomada\",\n" +
                            " \"componentequantidade\": 1\n" +
                            " }\n" +
                            " ]\n" +
                            "}")
                .when()
                    .put("produto/8398")
                .then()
                .assertThat()
                .statusCode(200)
                .body("data.produtonome", Matchers.equalTo("Massarico"))
                .body("data.componentes[0].componentenome", Matchers.equalTo("Tomada"))
                .body("message", Matchers.equalTo("Produto alterado com sucesso"));
    }

    @Test
    public void testAlterarProdutoDeVolta(){
        RestAssured
                .given()
                .header("token", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"produtonome\": \"Elevador\",\n" +
                        " \"produtovalor\": 1500.00,\n" +
                        " \"produtocores\": [\n" +
                        " \"Cinza\"\n" +
                        " ],\n" +
                        " \"componentes\": [\n" +
                        " {\n" +
                        " \"componentenome\": \"Botões\",\n" +
                        " \"componentequantidade\": 12\n" +
                        " }\n" +
                        " ]\n" +
                        "}")
                .when()
                .put("produto/8398")
                .then()
                .assertThat()
                .statusCode(200)
                .body("data.produtonome", Matchers.equalTo("Elevador"))
                .body("data.componentes[0].componentenome", Matchers.equalTo("Botões"))
                .body("message", Matchers.equalTo("Produto alterado com sucesso"));
    }


    @Test
    public void testBuscarDadosDosProdutos(){

        RestAssured
                .given()
                    .header("token", token)
                .when()
                    .get("produto")
                .then()
                    .assertThat()
                        .statusCode(200)
                        .body("message", Matchers.equalTo("Listagem de produtos realizada com sucesso"));
    }

}
