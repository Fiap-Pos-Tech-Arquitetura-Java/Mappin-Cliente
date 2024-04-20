package br.com.fiap.postech.mappin.cliente.helper;

import br.com.fiap.postech.mappin.cliente.entities.Cliente;
import br.com.fiap.postech.mappin.cliente.entities.Endereco;

import java.util.UUID;

public class ClienteHelper {
    public static Cliente getCliente(boolean geraId) {
        return getCliente(geraId, null);
    }
    public static Cliente getCliente(boolean geraId, String idEndereco) {
        var endereco = new Endereco("Rua do oriente", "283", "12345678", "São Paulo/SP/Brasil");
        if (idEndereco != null) {
            endereco.setId(UUID.fromString(idEndereco));
        } else {
            endereco.setId(null);
        }
        var cliente = new Cliente(
                "Anderson Wagner",
                "25310413030",
                endereco
        );
        if (geraId) {
            cliente.setId(UUID.randomUUID());
        }
        return cliente;
    }
}
