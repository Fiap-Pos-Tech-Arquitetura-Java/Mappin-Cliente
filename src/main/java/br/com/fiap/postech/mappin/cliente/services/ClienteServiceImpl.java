package br.com.fiap.postech.mappin.cliente.services;

import br.com.fiap.postech.mappin.cliente.entities.Cliente;
import br.com.fiap.postech.mappin.cliente.repository.ClienteRepository;
import br.com.fiap.postech.mappin.cliente.repository.EnderecoRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com esse cpf.");
        }
        cliente.setId(UUID.randomUUID());
        cliente.getEndereco().setId(UUID.randomUUID());
        return clienteRepository.save(cliente);
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable, Cliente cliente) {
        Example<Cliente> clienteExample = Example.of(cliente);
        return clienteRepository.findAll(clienteExample, pageable);
    }

    @Override
    public Cliente findById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + id));
    }

    @Override
    public Cliente update(UUID id, Cliente clienteParam) {
        Cliente cliente = findById(id);
        if (StringUtils.isNotEmpty(clienteParam.getNome())) {
            cliente.setNome(clienteParam.getNome());
        }
        if (clienteParam.getId() != null && !cliente.getId().equals(clienteParam.getId())) {
            throw new IllegalArgumentException("Não é possível alterar o id de um cliente.");
        }
        if (clienteParam.getCpf() != null && !cliente.getCpf().equals(clienteParam.getCpf())) {
            throw new IllegalArgumentException("Não é possível alterar o cpf de um cliente.");
        }
        if (StringUtils.isNotEmpty(clienteParam.getNome())) {
            cliente.setNome(clienteParam.getNome());
        }
        if (clienteParam.getEndereco() != null) {
            if (clienteParam.getEndereco().getId() == null) {
                clienteParam.getEndereco().setId(UUID.randomUUID());
            } else {
                if (enderecoRepository.existsById(clienteParam.getEndereco().getId())) {
                    throw new IllegalArgumentException("Endereco não encontrado com o ID: "
                            + clienteParam.getEndereco().getId() + " para o Cliente " + id);
                }
            }
        }
        cliente = clienteRepository.save(cliente);
        return cliente;
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        clienteRepository.deleteById(id);
    }
}
