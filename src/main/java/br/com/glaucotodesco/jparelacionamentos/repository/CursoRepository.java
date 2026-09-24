package br.com.glaucotodesco.jparelacionamentos.repository;

import br.com.glaucotodesco.jparelacionamentos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByInstrutor_Id(Long instrutorId);

    List<Curso> findAllByInstrutor_Id(Long instrutorId);
}
