package com.example.meu_primeiro_spring_boot.service;

import com.example.meu_primeiro_spring_boot.exceptions.RecursoNaoEncontradoException;
import com.example.meu_primeiro_spring_boot.model.Produto;
import com.example.meu_primeiro_spring_boot.repository.ProdutoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarProdutos(){

        return produtoRepository.findAll();
    }

    public Produto buscarProdutoPorId(Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com ID " + id + " não encontrado"));
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void excluirProduto(Long id){

        if(!produtoRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Produto com ID " + id + " não encontrado");
        }

        produtoRepository.deleteById(id);
    }

}
