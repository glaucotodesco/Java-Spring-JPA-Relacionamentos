package br.com.glaucotodesco.jparelacionamentos.dto;

import br.com.glaucotodesco.jparelacionamentos.model.Curso;

public record CursoResumoResponse(
        Long id,
        String nome,
        Integer cargaHoraria
) {
    public static CursoResumoResponse from(Curso curso) {
        return new CursoResumoResponse(
                curso.getId(),
                curso.getNome(),
                curso.getCargaHoraria()
        );
    }
}
