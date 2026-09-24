package br.com.glaucotodesco.jparelacionamentos.dto;

import br.com.glaucotodesco.jparelacionamentos.model.Aluno;

public record AlunoResumoResponse(
        Long id,
        String nome,
        String email
) {
    public static AlunoResumoResponse from(Aluno aluno) {
        return new AlunoResumoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail()
        );
    }
}
