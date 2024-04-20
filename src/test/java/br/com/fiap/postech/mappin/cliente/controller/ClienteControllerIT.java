package br.com.fiap.postech.mappin.cliente.controller;


import br.com.fiap.postech.mappin.cliente.entities.Cliente;
import br.com.fiap.postech.mappin.cliente.entities.Endereco;
import br.com.fiap.postech.mappin.cliente.helper.ClienteHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ClienteControllerIT {

    public static final String CLIENTE = "/mappin/cliente";
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class CadastrarCliente {
        @Test
        void devePermitirCadastrarCliente() {
            var cliente = ClienteHelper.getCliente(false);
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(cliente)
            .when()
                .post(CLIENTE)
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("schemas/cliente.schema.json"));
            // TODO VERIFICAR A OBRIGATORIEDADE DO ID
        }

        @Test
        void deveGerarExcecao_QuandoCadastrarCliente_RequisicaoXml() {
            /*
              Na aula o professor instanciou uma string e enviou no .body()
              Mas como o teste valida o contentType o body pode ser enviado com qualquer conteudo
              ou nem mesmo ser enviado como ficou no teste abaixo.
             */
            given()
                .contentType(MediaType.APPLICATION_XML_VALUE)
            .when()
                .post(CLIENTE)
            .then()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }
    }

    @Nested
    class BuscarCliente {
        @Test
        void devePermitirBuscarClientePorId() {
            var id = "56833f9a-7fda-49d5-a760-8e1ba41f35a8";
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(CLIENTE + "/{id}", id)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/cliente.schema.json"));
            // TODO VERIFICAR A OBRIGATORIEDADE DO ID
        }
        @Test
        void deveGerarExcecao_QuandoBuscarClientePorId_idNaoExiste() {
            var id = ClienteHelper.getCliente(true).getId();
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(CLIENTE + "/{id}", id)
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void devePermitirBuscarTodosCliente() {
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(CLIENTE)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/cliente.page.schema.json"));
        }

        @Test
        void devePermitirBuscarTodosCliente_ComPaginacao() {
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
                .queryParam("page", "1")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(CLIENTE)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/cliente.page.schema.json"));
        }
    }

    @Nested
    class AlterarCliente {
        @Test
        void devePermitirAlterarCliente(){
            var cliente = new Cliente(
                    "Kaiby o mestre do miro !!!",
                    "52816804046",
                    new Endereco("do projeto","123","9876341234","São Paulo/SP/Brasil")
            );
            cliente.setId(UUID.fromString("ab8fdcd5-c9b5-471e-8ad0-380a65d6cc86"));
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
                .body(cliente).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put(CLIENTE + "/{id}", cliente.getId())
            .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body(matchesJsonSchemaInClasspath("schemas/cliente.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarCliente_RequisicaoXml() {
            var cliente = ClienteHelper.getCliente(true);
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
                .body(cliente).contentType(MediaType.APPLICATION_XML_VALUE)
            .when().log().all()
                .put(CLIENTE + "/{id}", cliente.getId())
            .then().log().all()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        }

        @Test
        void deveGerarExcecao_QuandoAlterarClientePorId_idNaoExiste() {
            var cliente = ClienteHelper.getCliente(true);
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
                .body(cliente).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put(CLIENTE + "/{id}", cliente.getId())
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("Cliente não encontrado com o ID: " + cliente.getId()));
        }
    }

    @Nested
    class RemoverCliente {
        @Test
        void devePermitirRemoverCliente() {
            var cliente = new Cliente(
                    "Janaina",
                    "ccc@ddd.com",
                    new Endereco("do projeto","123","9876341234","São Paulo/SP/Brasil")
            );
            cliente.setId(UUID.fromString("8855e7b2-77b6-448b-97f8-8a0b529f3976"));
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
            .when()
                .delete(CLIENTE + "/{id}", cliente.getId())
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void deveGerarExcecao_QuandoRemoverClientePorId_idNaoExiste() {
            var cliente = ClienteHelper.getCliente(true);
            given()
                //.header(HttpHeaders.AUTHORIZATION, ClienteHelper.getToken())
            .when()
                .delete(CLIENTE + "/{id}", cliente.getId())
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("Cliente não encontrado com o ID: " + cliente.getId()));
        }
    }
}
