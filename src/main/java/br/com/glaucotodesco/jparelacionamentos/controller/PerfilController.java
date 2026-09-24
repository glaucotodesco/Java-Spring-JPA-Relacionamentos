package br.com.glaucotodesco.jparelacionamentos.controller;

import br.com.glaucotodesco.jparelacionamentos.dto.PerfilRequest;
import br.com.glaucotodesco.jparelacionamentos.dto.PerfilResponse;
import br.com.glaucotodesco.jparelacionamentos.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> criar(@Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilService.criar(request));
    }

    @GetMapping
    public List<PerfilResponse> listar() {
        return perfilService.listar();
    }

    @GetMapping("/{id}")
    public PerfilResponse buscar(@PathVariable Long id) {
        return perfilService.buscar(id);
    }

    @PutMapping("/{id}")
    public PerfilResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PerfilRequest request
    ) {
        return perfilService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        perfilService.excluir(id);
    }
}
