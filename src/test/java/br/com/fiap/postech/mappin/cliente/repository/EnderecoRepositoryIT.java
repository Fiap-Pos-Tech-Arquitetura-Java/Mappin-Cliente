package br.com.fiap.postech.mappin.cliente.repository;

import br.com.fiap.postech.mappin.cliente.entities.Endereco;
import br.com.fiap.postech.mappin.cliente.helper.EnderecoHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class EnderecoRepositoryIT {
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Test
    void devePermitirCriarEstrutura() {
        var totalRegistros = enderecoRepository.count();
        assertThat(totalRegistros).isEqualTo(3);
    }

    @Test
    void devePermitirCadastrarEndereco() {
        // Arrange
        var endereco = EnderecoHelper.getEndereco(true);
        // Act
        var enderecoCadastrado = enderecoRepository.save(endereco);
        // Assert
        assertThat(enderecoCadastrado).isInstanceOf(Endereco.class).isNotNull();
        assertThat(enderecoCadastrado.getId()).isEqualTo(endereco.getId());
        assertThat(enderecoCadastrado.getRua()).isEqualTo(endereco.getRua());
        assertThat(enderecoCadastrado.getNumero()).isEqualTo(endereco.getNumero());
        assertThat(enderecoCadastrado.getCep()).isEqualTo(endereco.getCep());
        assertThat(enderecoCadastrado.getCidade()).isEqualTo(endereco.getCidade());
    }
    @Test
    void devePermitirBuscarEndereco() {
        // Arrange
        var id = UUID.fromString("f0ef7e69-207b-4456-a130-a130d0f329d5");
        var rua = "rua a";
        // Act
        var enderecoOpcional = enderecoRepository.findById(id);
        // Assert
        assertThat(enderecoOpcional).isPresent();
        enderecoOpcional.ifPresent(
                enderecoRecebido -> {
                    assertThat(enderecoRecebido).isInstanceOf(Endereco.class).isNotNull();
                    assertThat(enderecoRecebido.getId()).isEqualTo(id);
                    assertThat(enderecoRecebido.getRua()).isEqualTo(rua);
                }
        );
        //verify(enderecoRepository, times(1)).findById(endereco.getId());
    }
    @Test
    void devePermitirRemoverEndereco() {
        // Arrange
        var id = UUID.fromString("ffd28058-4c16-41ce-9f03-80dfbc177aaf");
        // Act
        enderecoRepository.deleteById(id);
        // Assert
        var enderecoOpcional = enderecoRepository.findById(id);
        assertThat(enderecoOpcional).isEmpty();
    }
    @Test
    void devePermitirListarEnderecos() {
        // Arrange
        // Act
        var enderecosListados = enderecoRepository.findAll();
        // Assert
        assertThat(enderecosListados).hasSize(3);
    }
}
