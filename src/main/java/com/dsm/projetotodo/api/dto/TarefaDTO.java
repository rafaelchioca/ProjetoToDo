package com.dsm.projetotodo.api.dto;

import com.dsm.projetotodo.model.enums.StatusTarefa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TarefaDTO {

    private Long id;

    @NotNull(message = "O nome da tarefa é obrigatório.")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres.")
    private String nome;

    @Size(max = 1000, message = "A descrição pode ter no máximo 1000 caracteres.")
    private String descricao;

    @NotNull(message = "O status da tarefa é obrigatório.")
    private StatusTarefa status;

    @Size(max = 500, message = "As observações podem ter no máximo 500 caracteres.")
    private String observacoes;
}
