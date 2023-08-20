package com.maira.livrosapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maira.livrosapi.domain.model.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

}
