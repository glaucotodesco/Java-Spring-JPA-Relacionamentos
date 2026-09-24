package br.com.glaucotodesco.jparelacionamentos.dto;

import br.com.glaucotodesco.jparelacionamentos.model.Aluno;
import br.com.glaucotodesco.jparelacionamentos.model.Curso;

import java.util.Comparator;
import java.util.List;

public record CursoResponse(
        Long id,
        String nome,
        Integer cargaHoraria,
        InstrutorResumoResponse instrutor,
        List<AlunoResumoResponse> alunos
) {
    public static CursoResponse from(Curso curso) {
        var alunos = curso.getAlunos().stream()
                .sorted(Comparator.comparing(Aluno::getId))
                .map(AlunoResumoResponse::from)
                .toList();

        return new CursoResponse(
                curso.getId(),
                curso.getNome(),
                curso.getCargaHoraria(),
                InstrutorResumoResponse.from(curso.getInstrutor()),
                alunos
        );
    }
}
