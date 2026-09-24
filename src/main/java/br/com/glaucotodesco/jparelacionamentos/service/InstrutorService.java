package br.com.glaucotodesco.jparelacionamentos.service;

import br.com.glaucotodesco.jparelacionamentos.dto.CursoResumoResponse;
import br.com.glaucotodesco.jparelacionamentos.dto.InstrutorRequest;
import br.com.glaucotodesco.jparelacionamentos.dto.InstrutorResponse;
import br.com.glaucotodesco.jparelacionamentos.model.Curso;
import br.com.glaucotodesco.jparelacionamentos.model.Instrutor;
import br.com.glaucotodesco.jparelacionamentos.repository.CursoRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.InstrutorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class InstrutorService {

    private final InstrutorRepository instrutorRepository;
    private final CursoRepository cursoRepository;

    public InstrutorService(
            InstrutorRepository instrutorRepository,
            CursoRepository cursoRepository
    ) {
        this.instrutorRepository = instrutorRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public InstrutorResponse criar(InstrutorRequest request) {
        var instrutor = new Instrutor();
        instrutor.setNome(request.nome());
        instrutor.setEmail(request.email());
        return InstrutorResponse.from(instrutorRepository.save(instrutor));
    }

    @Transactional(readOnly = true)
    public List<InstrutorResponse> listar() {
        return instrutorRepository.findAll().stream()
                .sorted(Comparator.comparing(Instrutor::getId))
                .map(InstrutorResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public InstrutorResponse buscar(Long id) {
        return InstrutorResponse.from(buscarEntidade(id));
    }

    @Transactional
    public InstrutorResponse atualizar(Long id, InstrutorRequest request) {
        var instrutor = buscarEntidade(id);
        instrutor.setNome(request.nome());
        instrutor.setEmail(request.email());
        return InstrutorResponse.from(instrutorRepository.save(instrutor));
    }

    @Transactional
    public void excluir(Long id) {
        var instrutor = buscarEntidade(id);

        if (cursoRepository.existsByInstrutor_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O instrutor possui cursos. Troque ou exclua os cursos antes."
            );
        }

        instrutorRepository.delete(instrutor);
    }

    @Transactional(readOnly = true)
    public List<CursoResumoResponse> listarCursos(Long instrutorId) {
        buscarEntidade(instrutorId);

        return cursoRepository.findAllByInstrutor_Id(instrutorId).stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResumoResponse::from)
                .toList();
    }

    private Instrutor buscarEntidade(Long id) {
        return instrutorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Instrutor não encontrado: " + id
                ));
    }
}
