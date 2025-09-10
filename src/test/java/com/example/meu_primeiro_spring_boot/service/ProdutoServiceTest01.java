package com.example.meu_primeiro_spring_boot.service;

import com.example.meu_primeiro_spring_boot.exceptions.RecursoNaoEncontradoException;
import com.example.meu_primeiro_spring_boot.model.Produto;
import com.example.meu_primeiro_spring_boot.repository.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
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

public class ProdutoService01 {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) closeable.close();
    }

    @Test
    void listarProdutos_deveRetornarListaDoRepositorio() {
        Produto p1 = new Produto();
        Produto p2 = new Produto();
        when(produtoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Produto> resultado = produtoService.listarProdutos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(produtoRepository, times(1)).findAll();
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void buscarProdutoPorId_quandoExiste_deveRetornarProduto() {
        Produto produto = new Produto();
        produto.setId(10L);
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscarProdutoPorId(10L);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(produtoRepository, times(1)).findById(10L);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void buscarProdutoPorId_quandoNaoExiste_deveLancarRecursoNaoEncontradoException() {
        long idInexistente = 99L;
        when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.buscarProdutoPorId(idInexistente)
        );

        assertTrue(ex.getMessage().contains("Produto com ID " + idInexistente + " não encontrado"));
        verify(produtoRepository, times(1)).findById(idInexistente);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void salvarProduto_deveDelegarParaRepositorioERetornarSalvo() {
        Produto novo = new Produto();
        novo.setId(5L);
        when(produtoRepository.save(novo)).thenReturn(novo);

        Produto salvo = produtoService.salvarProduto(novo);

        assertNotNull(salvo);
        assertEquals(5L, salvo.getId());
        verify(produtoRepository, times(1)).save(novo);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void excluirProduto_quandoExiste_deveChamarDeleteById() {
        long id = 7L;
        when(produtoRepository.existsById(id)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(id);

        produtoService.excluirProduto(id);

        verify(produtoRepository, times(1)).existsById(id);
        verify(produtoRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void excluirProduto_quandoNaoExiste_deveLancarRecursoNaoEncontradoException() {
        long id = 8L;
        when(produtoRepository.existsById(id)).thenReturn(false);

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.excluirProduto(id)
        );

        assertTrue(ex.getMessage().contains("Produto com ID " + id + " não encontrado"));
        verify(produtoRepository, times(1)).existsById(id);
        verify(produtoRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(produtoRepository);
    }
}
