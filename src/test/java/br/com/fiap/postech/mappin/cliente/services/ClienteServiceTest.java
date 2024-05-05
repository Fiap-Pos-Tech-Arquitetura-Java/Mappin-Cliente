package br.com.fiap.postech.mappin.cliente.services;

import br.com.fiap.postech.mappin.cliente.entities.Cliente;
import br.com.fiap.postech.mappin.cliente.entities.Endereco;
import br.com.fiap.postech.mappin.cliente.helper.ClienteHelper;
import br.com.fiap.postech.mappin.cliente.repository.ClienteRepository;
import br.com.fiap.postech.mappin.cliente.repository.EnderecoRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class ClienteServiceTest {
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    private AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        clienteService = new ClienteServiceImpl(clienteRepository, enderecoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class CadastrarCliente {
        @Test
        void devePermitirCadastrarCliente() {
            // Arrange
            var cliente = ClienteHelper.getCliente(false);
            when(clienteRepository.save(any(Cliente.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            var clienteSalvo = clienteService.save(cliente);
            // Assert
            assertThat(clienteSalvo)
                    .isInstanceOf(Cliente.class)
                    .isNotNull();
            assertThat(clienteSalvo.getNome()).isEqualTo(cliente.getNome());
            assertThat(clienteSalvo.getId()).isNotNull();
            verify(clienteRepository, times(1)).save(any(Cliente.class));
        }
    }

    @Nested
    class BuscarCliente {
        @Test
        void devePermitirBuscarClientePorId() {
            // Arrange
            var cliente = ClienteHelper.getCliente(true);
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
            // Act
            var clienteObtido = clienteService.findById(cliente.getId());
            // Assert
            assertThat(clienteObtido).isEqualTo(cliente);
            verify(clienteRepository, times(1)).findById(any(UUID.class));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarClientePorId_idNaoExiste() {
            // Arrange
            var cliente = ClienteHelper.getCliente(true);
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());
            UUID uuid = cliente.getId();
            // Act
            assertThatThrownBy(() -> clienteService.findById(uuid))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cliente não encontrado com o ID: " + cliente.getId());
            // Assert
            verify(clienteRepository, times(1)).findById(any(UUID.class));
        }

        @Test
        void devePermitirBuscarTodosCliente() {
            // Arrange
            Cliente criteriosDeBusca = ClienteHelper.getCliente(false);
            Page<Cliente> clientes = new PageImpl<>(Arrays.asList(
                    ClienteHelper.getCliente(true),
                    ClienteHelper.getCliente(true),
                    ClienteHelper.getCliente(true)
            ));
            when(clienteRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(clientes);
            // Act
            var clientesObtidos = clienteService.findAll(Pageable.unpaged(), criteriosDeBusca);
            // Assert
            assertThat(clientesObtidos).hasSize(3);
            assertThat(clientesObtidos.getContent()).asList().allSatisfy(
                    cliente -> {
                        assertThat(cliente)
                                .isNotNull()
                                .isInstanceOf(Cliente.class);
                    }
            );
            verify(clienteRepository, times(1)).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Nested
    class AlterarCliente {
        @Test
        void devePermitirAlterarCliente() {
            // Arrange
            var cliente = ClienteHelper.getCliente(true);
            var clienteReferencia = new Cliente(cliente.getNome(), cliente.getCpf(), cliente.getEndereco());
            var novoCliente = new Cliente(
                    RandomStringUtils.random(20, true, true),
                    cliente.getCpf(),
                    new Endereco("do centro","41B","54521542","São Paulo/SP/Brasil")
            );
            novoCliente.setId(cliente.getId());
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
            when(clienteRepository.save(any(Cliente.class))).thenAnswer(r -> r.getArgument(0));
            // Act
            var clienteSalvo = clienteService.update(cliente.getId(), novoCliente);
            // Assert
            assertThat(clienteSalvo)
                    .isInstanceOf(Cliente.class)
                    .isNotNull();
            assertThat(clienteSalvo.getNome()).isEqualTo(novoCliente.getNome());
            assertThat(clienteSalvo.getNome()).isNotEqualTo(clienteReferencia.getNome());

            verify(clienteRepository, times(1)).findById(any(UUID.class));
            verify(clienteRepository, times(1)).save(any(Cliente.class));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarClientePorId_idNaoExiste() {
            // Arrange
            var cliente = ClienteHelper.getCliente(true);
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());
            UUID uuid = cliente.getId();
            // Act && Assert
            assertThatThrownBy(() -> clienteService.update(uuid, cliente))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cliente não encontrado com o ID: " + cliente.getId());
            verify(clienteRepository, times(1)).findById(any(UUID.class));
            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Nested
    class RemoverCliente {
        @Test
        void devePermitirRemoverCliente() {
            // Arrange
            var cliente = ClienteHelper.getCliente(true);
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
            doNothing().when(clienteRepository).deleteById(cliente.getId());
            // Act
            clienteService.delete(cliente.getId());
            // Assert
            verify(clienteRepository, times(1)).findById(any(UUID.class));
            verify(clienteRepository, times(1)).deleteById(any(UUID.class));
        }

        @Test
        void deveGerarExcecao_QuandRemoverClientePorId_idNaoExiste() {
            // Arrange
            var cliente = ClienteHelper.getCliente(true);
            doNothing().when(clienteRepository).deleteById(cliente.getId());
            UUID uuid = cliente.getId();
            // Act && Assert
            assertThatThrownBy(() -> clienteService.delete(uuid))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cliente não encontrado com o ID: " + cliente.getId());
            verify(clienteRepository, times(1)).findById(any(UUID.class));
            verify(clienteRepository, never()).deleteById(any(UUID.class));
        }
    }
}