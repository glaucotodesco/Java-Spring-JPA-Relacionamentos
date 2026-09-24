package br.com.glaucotodesco.jparelacionamentos.repository;

import br.com.glaucotodesco.jparelacionamentos.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByPerfil_Id(Long perfilId);

    List<Aluno> findAllByCursos_Id(Long cursoId);
}
