# Java Spring JPA - Relacionamentos

Projeto didático pronto para a aula de relacionamentos com Spring Data JPA.

O objetivo não é construir um CRUD do zero. O aluno clona o repositório, executa a aplicação e investiga como os relacionamentos aparecem em quatro níveis:

~~~text
Regra de negócio
      ↓
Entidades Java / JPA
      ↓
Banco de dados / chaves estrangeiras
      ↓
API REST / JSON
~~~

## Relacionamentos estudados

~~~text
Aluno 1 ───── 1 Perfil
Instrutor 1 ───── N Curso
Aluno N ───── N Curso
~~~

No código aparecem:

- @OneToOne
- @OneToMany
- @ManyToOne
- @ManyToMany
- @JoinColumn
- mappedBy
- @JoinTable

## Pré-requisitos

- Java 21
- Maven 3.9+

## Como executar

~~~bash
git clone https://github.com/glaucotodesco/Java-Spring-JPA-Relacionamentos.git
cd Java-Spring-JPA-Relacionamentos
mvn spring-boot:run
~~~

A API inicia em:

~~~text
http://localhost:8080
~~~

## H2 Console

Abra:

~~~text
http://localhost:8080/h2-console
~~~

Use:

~~~text
JDBC URL: jdbc:h2:mem:relacionamentos
User Name: sa
Password: deixe em branco
~~~

Consultas úteis:

~~~sql
SELECT * FROM PERFIS;
SELECT * FROM ALUNOS;
SELECT * FROM INSTRUTORES;
SELECT * FROM CURSOS;
SELECT * FROM ALUNO_CURSO;
~~~

## Dados iniciais

A aplicação cria automaticamente dados para que os relacionamentos possam ser consultados assim que o projeto iniciar.

- Alunos: Ana e Pedro
- Perfis: um para Ana e outro para Pedro
- Instrutores: Carlos e Mariana
- Cursos: Java e Spring, APIs REST com Spring e JPA e Hibernate
- Matrículas entre alunos e cursos

## Material da aula

A explicação completa, com código, banco, endpoint, JSON enviado e JSON devolvido, está em [AULA.md](AULA.md).

Para testar os endpoints rapidamente no IntelliJ IDEA ou em extensões compatíveis com arquivos HTTP, use [requests.http](requests.http).
