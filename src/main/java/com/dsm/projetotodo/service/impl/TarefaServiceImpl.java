package com.dsm.projetotodo.service.impl;

import com.dsm.projetotodo.exception.RegraNegocioException;
import com.dsm.projetotodo.model.entity.Tarefa;
import com.dsm.projetotodo.model.enums.StatusTarefa;
import com.dsm.projetotodo.model.repository.TarefaRepository;
import com.dsm.projetotodo.service.TarefaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository repository;

    public TarefaServiceImpl(TarefaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tarefa> listarTodas() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tarefa> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tarefa> buscar(Tarefa tarefaFiltro) {
        Example<Tarefa> example = Example.of(tarefaFiltro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    @Transactional
    public Tarefa salvar(Tarefa tarefa) {
        validar(tarefa);
        tarefa.setStatus(StatusTarefa.PENDENTE);  // Set the initial status
        return repository.save(tarefa);
    }

    @Override
    @Transactional
    public Tarefa atualizar(Tarefa tarefa) {
        Objects.requireNonNull(tarefa.getId(), "O ID da tarefa não pode ser nulo.");

        Optional<Tarefa> tarefaExistenteOpt = buscarPorId(tarefa.getId());
        if (!tarefaExistenteOpt.isPresent()) {
            throw new RegraNegocioException("Tarefa não encontrada.");
        }

        Tarefa tarefaExistente = tarefaExistenteOpt.get();

        tarefa.setData_criacao(tarefaExistente.getData_criacao());

        if (tarefa.getStatus() == null) {
            tarefa.setStatus(tarefaExistente.getStatus());
        }

        validar(tarefa);

        return repository.save(tarefa);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Optional<Tarefa> tarefa = buscarPorId(id);
        if (tarefa.isPresent()) {
            repository.delete(tarefa.get());
        } else {
            throw new RegraNegocioException("Tarefa não encontrada.");
        }
    }

    @Override
    @Transactional
    public void atualizarStatus(Tarefa tarefa, StatusTarefa status) {
        tarefa.setStatus(status);
        atualizar(tarefa);
    }

    @Override
    public void validar(Tarefa tarefa) {
        if (tarefa.getNome() == null || tarefa.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um nome válido.");
        }
        if (tarefa.getDescricao() == null || tarefa.getDescricao().trim().isEmpty()) {
            throw new RegraNegocioException("Informe uma descrição válida.");
        }
    }
}
