package com.dsm.projetotodo.model.repository;

import com.dsm.projetotodo.model.entity.Tarefa;
import com.dsm.projetotodo.model.enums.StatusTarefa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TarefaRepositoryTest {

    @Autowired
    TarefaRepository repository;

    @Test
    public void devePersistirUmaTarefa() {
        // cenário
        Tarefa tarefa = criarTarefa();

        // ação
        Tarefa tarefaSalva = repository.save(tarefa);

        // verificação
        Assertions.assertNotNull(tarefaSalva.getId());
        Assertions.assertEquals(tarefaSalva.getNome(), "Tarefa Exemplo");
    }

    @Test
    public void deveExcluirUmaTarefa() {
        // cenário
        Tarefa tarefa = criarTarefa();
        Tarefa tarefaSalva = repository.save(tarefa);

        // ação
        repository.delete(tarefaSalva);

        // verificação
        Optional<Tarefa> result = repository.findById(tarefaSalva.getId());
        Assertions.assertFalse(result.isPresent());
    }

    public static Tarefa criarTarefa() {
        return Tarefa.builder()
                .nome("Tarefa Exemplo")
                .descricao("Descrição da tarefa")
                .observacoes("Observações sobre a tarefa")
                .status(StatusTarefa.PENDENTE)
                .data_criacao(LocalDate.now())
                .data_atualizacao(LocalDate.now())
                .build();
    }
}
