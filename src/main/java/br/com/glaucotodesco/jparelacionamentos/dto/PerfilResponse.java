package br.com.glaucotodesco.jparelacionamentos.dto;

import br.com.glaucotodesco.jparelacionamentos.model.Perfil;

public record PerfilResponse(
        Long id,
        String telefone,
        String cidade
) {
    public static PerfilResponse from(Perfil perfil) {
        return new PerfilResponse(
                perfil.getId(),
                perfil.getTelefone(),
                perfil.getCidade()
        );
    }
}
