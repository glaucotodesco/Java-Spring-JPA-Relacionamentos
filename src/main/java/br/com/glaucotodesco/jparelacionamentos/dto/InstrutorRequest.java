package br.com.glaucotodesco.jparelacionamentos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InstrutorRequest(
        @NotBlank String nome,
        @NotBlank @Email String email
) {
}
