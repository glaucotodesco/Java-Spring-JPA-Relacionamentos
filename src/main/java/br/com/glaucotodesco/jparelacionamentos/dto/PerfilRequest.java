package br.com.glaucotodesco.jparelacionamentos.dto;

import jakarta.validation.constraints.NotBlank;

public record PerfilRequest(
        @NotBlank String telefone,
        @NotBlank String cidade
) {
}
