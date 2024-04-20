package br.com.fiap.postech.mappin.cliente.helper;

import br.com.fiap.postech.mappin.cliente.entities.Cliente;
import br.com.fiap.postech.mappin.cliente.entities.Endereco;

import java.util.UUID;

public class EnderecoHelper {
    public static Endereco getEndereco(boolean geraId) {
        var endereco = new Endereco("Rua do oriente", "283", "12345678", "São Paulo/SP/Brasil");
        if (geraId) {
            endereco.setId(UUID.randomUUID());
        }
        return endereco;
    }
}
