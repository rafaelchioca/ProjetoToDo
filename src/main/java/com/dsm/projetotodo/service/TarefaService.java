package com.dsm.projetotodo.service;

import com.dsm.projetotodo.model.entity.Tarefa;
import com.dsm.projetotodo.model.enums.StatusTarefa;

import java.util.List;
import java.util.Optional;

public interface TarefaService {

    List<Tarefa> listarTodas();

    Optional<Tarefa> buscarPorId(Long id);

    List<Tarefa> buscar(Tarefa tarefaFiltro);

    Tarefa salvar(Tarefa tarefa);

    Tarefa atualizar(Tarefa tarefa);

    void deletar(Long id);

    void atualizarStatus(Tarefa tarefa, StatusTarefa status);

    void validar(Tarefa tarefa);
}
