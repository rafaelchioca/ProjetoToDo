package com.dsm.projetotodo.model.repository;

import com.dsm.projetotodo.model.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
