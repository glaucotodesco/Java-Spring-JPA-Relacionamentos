package br.com.glaucotodesco.jparelacionamentos.controller;

import br.com.glaucotodesco.jparelacionamentos.dto.CursoResumoResponse;
import br.com.glaucotodesco.jparelacionamentos.dto.InstrutorRequest;
import br.com.glaucotodesco.jparelacionamentos.dto.InstrutorResponse;
import br.com.glaucotodesco.jparelacionamentos.service.InstrutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {

    private final InstrutorService instrutorService;

    public InstrutorController(InstrutorService instrutorService) {
        this.instrutorService = instrutorService;
    }

    @PostMapping
    public ResponseEntity<InstrutorResponse> criar(@Valid @RequestBody InstrutorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instrutorService.criar(request));
    }

    @GetMapping
    public List<InstrutorResponse> listar() {
        return instrutorService.listar();
    }

    @GetMapping("/{id}")
    public InstrutorResponse buscar(@PathVariable Long id) {
        return instrutorService.buscar(id);
    }

    @PutMapping("/{id}")
    public InstrutorResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InstrutorRequest request
    ) {
        return instrutorService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        instrutorService.excluir(id);
    }

    @GetMapping("/{id}/cursos")
    public List<CursoResumoResponse> listarCursos(@PathVariable Long id) {
        return instrutorService.listarCursos(id);
    }
}
