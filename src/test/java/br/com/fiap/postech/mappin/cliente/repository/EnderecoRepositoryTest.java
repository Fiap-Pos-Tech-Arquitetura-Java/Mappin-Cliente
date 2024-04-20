package br.com.fiap.postech.mappin.cliente.repository;

import br.com.fiap.postech.mappin.cliente.entities.Endereco;
import br.com.fiap.postech.mappin.cliente.helper.EnderecoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class EnderecoRepositoryTest {
    @Mock
    private EnderecoRepository enderecoRepository;

    AutoCloseable openMocks;
    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCadastrarEndereco() {
        // Arrange
        var endereco = EnderecoHelper.getEndereco(false);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        // Act
        var savedEndereco = enderecoRepository.save(endereco);
        // Assert
        assertThat(savedEndereco).isNotNull().isEqualTo(endereco);
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    void devePermitirBuscarEndereco() {
        // Arrange
        var endereco = EnderecoHelper.getEndereco(true);
        when(enderecoRepository.findById(endereco.getId())).thenReturn(Optional.of(endereco));
        // Act
        var enderecoOpcional = enderecoRepository.findById(endereco.getId());
        // Assert
        assertThat(enderecoOpcional).isNotNull().containsSame(endereco);
        enderecoOpcional.ifPresent(
                enderecoRecebido -> {
                    assertThat(enderecoRecebido).isInstanceOf(Endereco.class).isNotNull();
                    assertThat(enderecoRecebido.getId()).isEqualTo(endereco.getId());
                    assertThat(enderecoRecebido.getRua()).isEqualTo(endereco.getRua());
                    assertThat(enderecoRecebido.getNumero()).isEqualTo(endereco.getNumero());
                    assertThat(enderecoRecebido.getCep()).isEqualTo(endereco.getCep());
                    assertThat(enderecoRecebido.getCidade()).isEqualTo(endereco.getCidade());
                }
        );
        verify(enderecoRepository, times(1)).findById(endereco.getId());
    }
    @Test
    void devePermitirRemoverEndereco() {
        //Arrange
        var id = UUID.randomUUID();
        doNothing().when(enderecoRepository).deleteById(id);
        //Act
        enderecoRepository.deleteById(id);
        //Assert
        verify(enderecoRepository, times(1)).deleteById(id);
    }
    @Test
    void devePermitirListarEnderecos() {
        // Arrange
        var endereco1 = EnderecoHelper.getEndereco(true);
        var endereco2 = EnderecoHelper.getEndereco(true);
        var listaEnderecos = Arrays.asList(
                endereco1,
                endereco2
        );
        when(enderecoRepository.findAll()).thenReturn(listaEnderecos);
        // Act
        var enderecosListados = enderecoRepository.findAll();
        assertThat(enderecosListados)
                .hasSize(2)
                .containsExactlyInAnyOrder(endereco1, endereco2);
        verify(enderecoRepository, times(1)).findAll();
    }
}