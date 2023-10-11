package com.maira.livrosapi.domain.repository;

import com.maira.livrosapi.domain.model.Grupo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    Page<Grupo> findByNomeContaining(String nome, Pageable pageable);

}
