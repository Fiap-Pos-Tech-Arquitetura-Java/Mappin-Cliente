package br.com.fiap.postech.mappin.cliente.repository;

import br.com.fiap.postech.mappin.cliente.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

}
