package br.com.glaucotodesco.jparelacionamentos.dto;

import br.com.glaucotodesco.jparelacionamentos.model.Aluno;
import br.com.glaucotodesco.jparelacionamentos.model.Curso;

import java.util.Comparator;
import java.util.List;

public record AlunoResponse(
        Long id,
        String nome,
        String email,
        PerfilResponse perfil,
        List<CursoResumoResponse> cursos
) {
    public static AlunoResponse from(Aluno aluno) {
        var perfil = aluno.getPerfil() == null
                ? null
                : PerfilResponse.from(aluno.getPerfil());

        var cursos = aluno.getCursos().stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResumoResponse::from)
                .toList();

        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                perfil,
                cursos
        );
    }
}
