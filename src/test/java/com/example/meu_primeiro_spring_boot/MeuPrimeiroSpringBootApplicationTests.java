package com.example.meu_primeiro_spring_boot;

import com.example.meu_primeiro_spring_boot.exceptions.RecursoNaoEncontradoException;
import com.example.meu_primeiro_spring_boot.model.Produto;
import com.example.meu_primeiro_spring_boot.repository.ProdutoRepository;
import com.example.meu_primeiro_spring_boot.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeuPrimeiroSpringBootApplicationTests {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void contextLoads() {
    }

    // 1) listarProdutos deve retornar lista vazia quando repositório estiver vazio
    @Test
    void shouldListarProdutosRetornarListaVaziaQuandoRepositorioVazio() {
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Produto> result = produtoService.listarProdutos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(produtoRepository, times(1)).findAll();
        verifyNoMoreInteractions(produtoRepository);
    }

    // 2) buscarProdutoPorId deve retornar o produto quando existir
    @Test
    void shouldBuscarProdutoPorIdRetornarProdutoQuandoExistir() {
        Produto produto = new Produto(1L, "Produto A", "10.00");
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto result = produtoService.buscarProdutoPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Produto A", result.getNome());
        assertEquals("10.00", result.getPreco());
        verify(produtoRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(produtoRepository);
    }

    // 3) buscarProdutoPorId deve lançar RecursoNaoEncontradoException quando não existir
    @Test
    void shouldBuscarProdutoPorIdLancarExcecaoQuandoNaoExistir() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.buscarProdutoPorId(99L)
        );

        assertTrue(ex.getMessage().contains("Produto com ID 99 não encontrado"));
        verify(produtoRepository, times(1)).findById(99L);
        verifyNoMoreInteractions(produtoRepository);
    }

    // 4) salvarProduto deve retornar a entidade salva do repositório
    @Test
    void shouldSalvarProdutoRetornarEntidadeSalva() {
        Produto entrada = new Produto(null, "Novo Produto", "15.50");
        Produto salvo = new Produto(5L, "Novo Produto", "15.50");
        when(produtoRepository.save(entrada)).thenReturn(salvo);

        Produto result = produtoService.salvarProduto(entrada);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("Novo Produto", result.getNome());
        assertEquals("15.50", result.getPreco());
        verify(produtoRepository, times(1)).save(entrada);
        verifyNoMoreInteractions(produtoRepository);
    }

    // 5) excluirProduto deve lançar RecursoNaoEncontradoException quando ID não existir
    @Test
    void shouldExcluirProdutoLancarExcecaoQuandoNaoExistir() {
        when(produtoRepository.existsById(99L)).thenReturn(false);

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.excluirProduto(99L)
        );
        assertTrue(ex.getMessage().contains("Produto com ID 99 não encontrado"));
        verify(produtoRepository, times(1)).existsById(99L);
        verify(produtoRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(produtoRepository);
    }

    // 6) excluirProduto deve excluir quando ID existir
    @Test
    void shouldExcluirProdutoExcluirQuandoExistir() {
        when(produtoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(1L);

        assertDoesNotThrow(() -> produtoService.excluirProduto(1L));

        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(produtoRepository);
    }
}
