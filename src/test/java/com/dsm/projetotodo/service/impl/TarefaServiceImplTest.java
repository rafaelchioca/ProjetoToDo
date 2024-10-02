package com.dsm.projetotodo.service.impl;

import com.dsm.projetotodo.exception.RegraNegocioException;
import com.dsm.projetotodo.model.entity.Tarefa;
import com.dsm.projetotodo.model.enums.StatusTarefa;
import com.dsm.projetotodo.model.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TarefaServiceImplTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarTarefaComSucesso() {
        Tarefa tarefa = Tarefa.builder().nome("Tarefa 1").descricao("Descrição 1").status(StatusTarefa.PENDENTE).build();

        when(repository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa tarefaSalva = service.salvar(tarefa);

        assertNotNull(tarefaSalva);
        assertEquals("Tarefa 1", tarefaSalva.getNome());
        verify(repository, times(1)).save(tarefa);
    }

    @Test
    void deveAtualizarTarefaComSucesso() {
        Tarefa tarefa = Tarefa.builder().id(1L).nome("Tarefa 1").descricao("Descrição Atualizada").status(StatusTarefa.PENDENTE).build();

        when(repository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(repository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa tarefaAtualizada = service.atualizar(tarefa);

        assertEquals("Descrição Atualizada", tarefaAtualizada.getDescricao());
        verify(repository, times(1)).save(tarefa);
    }

    @Test
    void deveDeletarTarefaComSucesso() {
        Tarefa tarefa = Tarefa.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

        service.deletar(1L);

        verify(repository, times(1)).delete(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoValidacaoFalhar() {
        Tarefa tarefaInvalida = Tarefa.builder().nome("").descricao("").build();

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> service.salvar(tarefaInvalida));
        assertEquals("Informe um nome válido.", exception.getMessage());
    }
}
