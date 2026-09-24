package br.com.glaucotodesco.jparelacionamentos.dto;

import br.com.glaucotodesco.jparelacionamentos.model.Instrutor;

public record InstrutorResumoResponse(
        Long id,
        String nome,
        String email
) {
    public static InstrutorResumoResponse from(Instrutor instrutor) {
        return new InstrutorResumoResponse(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail()
        );
    }
}
