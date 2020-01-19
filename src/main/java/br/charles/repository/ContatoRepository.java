package br.charles.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.charles.model.Contato;

@Repository
public interface ContatoRepository extends PagingAndSortingRepository<Contato, String> {

    public Page<Contato> findAll(Pageable pageable);

    @Query("SELECT p FROM Contato p "
            + "WHERE lower(nome) like %:busca% ")
    public Page<Contato> busca(@Param("busca") String busca, Pageable pageable);

}
