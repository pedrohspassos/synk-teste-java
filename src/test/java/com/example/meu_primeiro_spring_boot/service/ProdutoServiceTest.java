package com.example.meu_primeiro_spring_boot.service;

import com.example.meu_primeiro_spring_boot.exceptions.RecursoNaoEncontradoException;
import com.example.meu_primeiro_spring_boot.model.Produto;
import com.example.meu_primeiro_spring_boot.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarProdutos() {
        Produto produto1 = new Produto();
        Produto produto2 = new Produto();
        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto1, produto2));

        List<Produto> produtos = produtoService.listarProdutos();
        assertEquals(2, produtos.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarProdutoPorId_Encontrado() {
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscarProdutoPorId(1L);
        assertEquals(1L, resultado.getId());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarProdutoPorId_NaoEncontrado() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.buscarProdutoPorId(1L));
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testSalvarProduto() {
        Produto produto = new Produto();
        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto resultado = produtoService.salvarProduto(produto);
        assertEquals(produto, resultado);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void testExcluirProduto_Existente() {
        when(produtoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(1L);

        produtoService.excluirProduto(1L);
        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExcluirProduto_NaoExistente() {
        when(produtoRepository.existsById(1L)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.excluirProduto(1L));
        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, never()).deleteById(anyLong());
    }
}
