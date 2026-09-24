package br.com.glaucotodesco.jparelacionamentos.dto;

import br.com.glaucotodesco.jparelacionamentos.model.Curso;
import br.com.glaucotodesco.jparelacionamentos.model.Instrutor;

import java.util.Comparator;
import java.util.List;

public record InstrutorResponse(
        Long id,
        String nome,
        String email,
        List<CursoResumoResponse> cursos
) {
    public static InstrutorResponse from(Instrutor instrutor) {
        var cursos = instrutor.getCursos().stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResumoResponse::from)
                .toList();

        return new InstrutorResponse(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                cursos
        );
    }
}
