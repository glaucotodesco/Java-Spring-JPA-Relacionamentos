package br.com.glaucotodesco.jparelacionamentos.controller;

import br.com.glaucotodesco.jparelacionamentos.dto.AlunoRequest;
import br.com.glaucotodesco.jparelacionamentos.dto.AlunoResponse;
import br.com.glaucotodesco.jparelacionamentos.dto.CursoResumoResponse;
import br.com.glaucotodesco.jparelacionamentos.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@Valid @RequestBody AlunoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.criar(request));
    }

    @GetMapping
    public List<AlunoResponse> listar() {
        return alunoService.listar();
    }

    @GetMapping("/{id}")
    public AlunoResponse buscar(@PathVariable Long id) {
        return alunoService.buscar(id);
    }

    @PutMapping("/{id}")
    public AlunoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoRequest request
    ) {
        return alunoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        alunoService.excluir(id);
    }

    @PutMapping("/{alunoId}/perfil/{perfilId}")
    public AlunoResponse associarPerfil(
            @PathVariable Long alunoId,
            @PathVariable Long perfilId
    ) {
        return alunoService.associarPerfil(alunoId, perfilId);
    }

    @DeleteMapping("/{alunoId}/perfil")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerPerfil(@PathVariable Long alunoId) {
        alunoService.removerPerfil(alunoId);
    }

    @PostMapping("/{alunoId}/cursos/{cursoId}")
    public AlunoResponse matricular(
            @PathVariable Long alunoId,
            @PathVariable Long cursoId
    ) {
        return alunoService.matricular(alunoId, cursoId);
    }

    @DeleteMapping("/{alunoId}/cursos/{cursoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerMatricula(
            @PathVariable Long alunoId,
            @PathVariable Long cursoId
    ) {
        alunoService.removerMatricula(alunoId, cursoId);
    }

    @GetMapping("/{alunoId}/cursos")
    public List<CursoResumoResponse> listarCursos(@PathVariable Long alunoId) {
        return alunoService.listarCursos(alunoId);
    }
}
