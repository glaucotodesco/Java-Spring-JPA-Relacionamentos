package br.com.glaucotodesco.jparelacionamentos.config;

import br.com.glaucotodesco.jparelacionamentos.model.Aluno;
import br.com.glaucotodesco.jparelacionamentos.model.Curso;
import br.com.glaucotodesco.jparelacionamentos.model.Instrutor;
import br.com.glaucotodesco.jparelacionamentos.model.Perfil;
import br.com.glaucotodesco.jparelacionamentos.repository.AlunoRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.CursoRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.InstrutorRepository;
import br.com.glaucotodesco.jparelacionamentos.repository.PerfilRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner carregarDados(
            PerfilRepository perfilRepository,
            InstrutorRepository instrutorRepository,
            CursoRepository cursoRepository,
            AlunoRepository alunoRepository
    ) {
        return args -> {
            var perfilAna = new Perfil();
            perfilAna.setTelefone("11911111111");
            perfilAna.setCidade("São Paulo");

            var perfilPedro = new Perfil();
            perfilPedro.setTelefone("19922222222");
            perfilPedro.setCidade("Campinas");

            perfilRepository.save(perfilAna);
            perfilRepository.save(perfilPedro);

            var carlos = new Instrutor();
            carlos.setNome("Carlos");
            carlos.setEmail("carlos@email.com");

            var mariana = new Instrutor();
            mariana.setNome("Mariana");
            mariana.setEmail("mariana@email.com");

            instrutorRepository.save(carlos);
            instrutorRepository.save(mariana);

            var javaSpring = new Curso();
            javaSpring.setNome("Java e Spring");
            javaSpring.setCargaHoraria(40);
            javaSpring.setInstrutor(carlos);

            var apiRest = new Curso();
            apiRest.setNome("APIs REST com Spring");
            apiRest.setCargaHoraria(20);
            apiRest.setInstrutor(carlos);

            var jpaHibernate = new Curso();
            jpaHibernate.setNome("JPA e Hibernate");
            jpaHibernate.setCargaHoraria(24);
            jpaHibernate.setInstrutor(mariana);

            cursoRepository.save(javaSpring);
            cursoRepository.save(apiRest);
            cursoRepository.save(jpaHibernate);

            var ana = new Aluno();
            ana.setNome("Ana");
            ana.setEmail("ana@email.com");
            ana.setPerfil(perfilAna);
            ana.getCursos().add(javaSpring);
            ana.getCursos().add(apiRest);

            var pedro = new Aluno();
            pedro.setNome("Pedro");
            pedro.setEmail("pedro@email.com");
            pedro.setPerfil(perfilPedro);
            pedro.getCursos().add(javaSpring);
            pedro.getCursos().add(jpaHibernate);

            alunoRepository.save(ana);
            alunoRepository.save(pedro);
        };
    }
}
