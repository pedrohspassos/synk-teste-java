package com.example.meu_primeiro_spring_boot.service;

import com.example.meu_primeiro_spring_boot.exceptions.RecursoNaoEncontradoException;
import com.example.meu_primeiro_spring_boot.model.Produto;
import com.example.meu_primeiro_spring_boot.repository.ProdutoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

