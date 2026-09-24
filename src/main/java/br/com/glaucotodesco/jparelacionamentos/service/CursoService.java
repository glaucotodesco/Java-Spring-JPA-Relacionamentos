package br.com.glaucotodesco.jparelacionamentos.service;

import br.com.glaucotodesco.jparelacionamentos.dto.AlunoResumoResponse;
import br.com.glaucotodesco.jparelacionamentos.dto.CursoRequest;
import br.com.glaucotodesco.jparelacionamentos.dto.CursoResponse;
import br.com.glaucotodesco.jparelacionamentos.model.Aluno;
import br.com.glaucotodesco.jparelacionamentos.model.Curso;
import br.com.glaucotodesco.jparelacionamentos.repository.AlunoRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.CursoRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.InstrutorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstrutorRepository instrutorRepository;
    private final AlunoRepository alunoRepository;

    public CursoService(
            CursoRepository cursoRepository,
            InstrutorRepository instrutorRepository,
            AlunoRepository alunoRepository
    ) {
        this.cursoRepository = cursoRepository;
        this.instrutorRepository = instrutorRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public CursoResponse criar(CursoRequest request) {
        var instrutor = buscarInstrutor(request.instrutorId());

        var curso = new Curso();
        curso.setNome(request.nome());
        curso.setCargaHoraria(request.cargaHoraria());
        curso.setInstrutor(instrutor);

        return CursoResponse.from(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {
        return cursoRepository.findAll().stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CursoResponse buscar(Long id) {
        return CursoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public CursoResponse atualizar(Long id, CursoRequest request) {
        var curso = buscarEntidade(id);
        var instrutor = buscarInstrutor(request.instrutorId());

        curso.setNome(request.nome());
        curso.setCargaHoraria(request.cargaHoraria());
        curso.setInstrutor(instrutor);

        return CursoResponse.from(cursoRepository.save(curso));
    }

    @Transactional
    public void excluir(Long id) {
        var curso = buscarEntidade(id);

        var alunos = alunoRepository.findAllByCursos_Id(id);
        for (var aluno : alunos) {
            aluno.getCursos().removeIf(c -> Objects.equals(c.getId(), id));
        }
        alunoRepository.saveAll(alunos);

        cursoRepository.delete(curso);
    }

    @Transactional
    public CursoResponse trocarInstrutor(Long cursoId, Long instrutorId) {
        var curso = buscarEntidade(cursoId);
        var instrutor = buscarInstrutor(instrutorId);

        curso.setInstrutor(instrutor);
        return CursoResponse.from(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    public List<AlunoResumoResponse> listarAlunos(Long cursoId) {
        var curso = buscarEntidade(cursoId);

        return curso.getAlunos().stream()
                .sorted(Comparator.comparing(Aluno::getId))
                .map(AlunoResumoResponse::from)
                .toList();
    }

    private Curso buscarEntidade(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não encontrado: " + id
                ));
    }

    private br.com.glaucotodesco.jparelacionamentos.model.Instrutor buscarInstrutor(Long id) {
        return instrutorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Instrutor não encontrado: " + id
                ));
    }
}
