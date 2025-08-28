package com.example.meu_primeiro_spring_boot.repository;

import com.example.meu_primeiro_spring_boot.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
}
