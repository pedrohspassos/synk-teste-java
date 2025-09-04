package com.example.meu_primeiro_spring_boot.controller;

import com.example.meu_primeiro_spring_boot.service.ProdutoService;
import com.example.meu_primeiro_spring_boot.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private Produto produto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto(1L, "Produto Teste", 100.0);
    }

    @Test
    public void testBuscarProdutoPorId_Sucesso() {
        when(produtoService.buscarProdutoPorId(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoController.buscarProdutoPorId(1L);
        
        assertTrue(resultado.isPresent());
        assertEquals(produto.getNome(), resultado.get().getNome());
        verify(produtoService, times(1)).buscarProdutoPorId(1L);
    }

    @Test
    public void testBuscarProdutoPorId_ProdutoNaoEncontrado() {
        when(produtoService.buscarProdutoPorId(2L)).thenReturn(Optional.empty());

        Optional<Produto> resultado = produtoController.buscarProdutoPorId(2L);
        
        assertFalse(resultado.isPresent());
        verify(produtoService, times(1)).buscarProdutoPorId(2L);
    }

    @Test
    public void testSalvarProduto_Sucesso() {
        when(produtoService.salvarProduto(produto)).thenReturn(produto);

        Produto resultado = produtoController.salvarProduto(produto);
        
        assertNotNull(resultado);
        assertEquals(produto.getNome(), resultado.getNome());
        verify(produtoService, times(1)).salvarProduto(produto);
    }

    @Test
    public void testSalvarProduto_Error() {
        when(produtoService.salvarProduto(produto)).thenThrow(new RuntimeException("Erro ao salvar"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            produtoController.salvarProduto(produto);
        });
        
        assertEquals("Erro ao salvar", exception.getMessage());
        verify(produtoService, times(1)).salvarProduto(produto);
    }

    @Test
    public void testExcluirProduto_Sucesso() {
        doNothing().when(produtoService).excluirProduto(1L);

        assertDoesNotThrow(() -> produtoController.excluirProduto(1L));

        verify(produtoService, times(1)).excluirProduto(1L);
    }

    @Test
    public void testExcluirProduto_Error() {
        doThrow(new RuntimeException("Erro ao excluir")).when(produtoService).excluirProduto(1L);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            produtoController.excluirProduto(1L);
        });
        
        assertEquals("Erro ao excluir", exception.getMessage());
        verify(produtoService, times(1)).excluirProduto(1L);
    }
}