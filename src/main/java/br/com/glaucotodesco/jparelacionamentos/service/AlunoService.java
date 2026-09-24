package br.com.glaucotodesco.jparelacionamentos.service;

import br.com.glaucotodesco.jparelacionamentos.dto.AlunoRequest;
import br.com.glaucotodesco.jparelacionamentos.dto.AlunoResponse;
import br.com.glaucotodesco.jparelacionamentos.dto.CursoResumoResponse;
import br.com.glaucotodesco.jparelacionamentos.model.Aluno;
import br.com.glaucotodesco.jparelacionamentos.repository.AlunoRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.CursoRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.PerfilRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PerfilRepository perfilRepository;
    private final CursoRepository cursoRepository;

    public AlunoService(
            AlunoRepository alunoRepository,
            PerfilRepository perfilRepository,
            CursoRepository cursoRepository
    ) {
        this.alunoRepository = alunoRepository;
        this.perfilRepository = perfilRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public AlunoResponse criar(AlunoRequest request) {
        var aluno = new Aluno();
        aluno.setNome(request.nome());
        aluno.setEmail(request.email());
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listar() {
        return alunoRepository.findAll().stream()
                .sorted(Comparator.comparing(Aluno::getId))
                .map(AlunoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscar(Long id) {
        return AlunoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        var aluno = buscarEntidade(id);
        aluno.setNome(request.nome());
        aluno.setEmail(request.email());
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional
    public void excluir(Long id) {
        var aluno = buscarEntidade(id);
        aluno.getCursos().clear();
        alunoRepository.save(aluno);
        alunoRepository.delete(aluno);
    }

    @Transactional
    public AlunoResponse associarPerfil(Long alunoId, Long perfilId) {
        var aluno = buscarEntidade(alunoId);
        var perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Perfil não encontrado: " + perfilId
                ));

        alunoRepository.findByPerfil_Id(perfilId)
                .filter(outroAluno -> !Objects.equals(outroAluno.getId(), alunoId))
                .ifPresent(outroAluno -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "O perfil já está associado ao aluno " + outroAluno.getId()
                    );
                });

        aluno.setPerfil(perfil);
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional
    public void removerPerfil(Long alunoId) {
        var aluno = buscarEntidade(alunoId);
        aluno.setPerfil(null);
        alunoRepository.save(aluno);
    }

    @Transactional
    public AlunoResponse matricular(Long alunoId, Long cursoId) {
        var aluno = buscarEntidade(alunoId);
        var curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso não encontrado: " + cursoId
                ));

        aluno.getCursos().add(curso);
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional
    public void removerMatricula(Long alunoId, Long cursoId) {
        var aluno = buscarEntidade(alunoId);

        if (!cursoRepository.existsById(cursoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Curso não encontrado: " + cursoId
            );
        }

        aluno.getCursos().removeIf(curso -> Objects.equals(curso.getId(), cursoId));
        alunoRepository.save(aluno);
    }

    @Transactional(readOnly = true)
    public List<CursoResumoResponse> listarCursos(Long alunoId) {
        var aluno = buscarEntidade(alunoId);

        return aluno.getCursos().stream()
                .sorted(Comparator.comparing(curso -> curso.getId()))
                .map(CursoResumoResponse::from)
                .toList();
    }

    private Aluno buscarEntidade(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado: " + id
                ));
    }
}
