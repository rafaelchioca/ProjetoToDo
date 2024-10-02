package com.dsm.projetotodo.api.resource;

import com.dsm.projetotodo.api.dto.TarefaDTO;
import com.dsm.projetotodo.exception.RegraNegocioException;
import com.dsm.projetotodo.model.entity.Tarefa;
import com.dsm.projetotodo.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaResource {

    private final TarefaService service;

    @Autowired
    public TarefaResource(TarefaService service) {
        this.service = service;
    }

    // Buscar todas as tarefas
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Tarefa>> listarTodas() {
        List<Tarefa> tarefas = service.listarTodas();
        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = service.buscarPorId(id);
        return tarefa.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar nova tarefa
    @PostMapping
    public ResponseEntity<Tarefa> salvar(@Valid @RequestBody TarefaDTO dto) {
        Tarefa tarefa = Tarefa.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .status(dto.getStatus())
                .observacoes(dto.getObservacoes())
                .build();
        try {
            Tarefa tarefaSalva = service.salvar(tarefa);
            return new ResponseEntity<>(tarefaSalva, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Atualizar tarefa existente
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaDTO dto) {
        Optional<Tarefa> tarefaExistente = service.buscarPorId(id);
        if (!tarefaExistente.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Tarefa tarefa = Tarefa.builder()
                .id(id)
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .status(dto.getStatus())
                .observacoes(dto.getObservacoes())
                .build();

        try {
            Tarefa tarefaAtualizada = service.atualizar(tarefa);
            return ResponseEntity.ok(tarefaAtualizada);
        } catch (RegraNegocioException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Deletar tarefa por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<Tarefa> tarefa = service.buscarPorId(id);
        if (!tarefa.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        service.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
