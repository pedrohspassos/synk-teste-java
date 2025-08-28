package com.example.meu_primeiro_spring_boot.repository;

  import com.example.meu_primeiro_spring_boot.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
