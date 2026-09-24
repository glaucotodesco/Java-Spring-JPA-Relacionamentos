# Aula — Relacionamentos JPA com Spring Boot

## Objetivo

Ao final da aula, o aluno deve conseguir observar uma regra de negócio e identificar qual relacionamento JPA representa aquela situação.

O projeto já está pronto. A atividade da turma é investigar, testar e alterar pequenos pontos.

O fio condutor será sempre:

~~~text
Domínio → Java/JPA → Banco → Endpoint → JSON
~~~

---

# 1. @OneToOne — Aluno e Perfil

## Regra de negócio

Um aluno possui um perfil.

~~~text
ALUNO 1 ───── 1 PERFIL
~~~

## Java/JPA

Em Aluno.java:

~~~java
@OneToOne
@JoinColumn(name = "perfil_id", unique = true)
private Perfil perfil;
~~~

A coluna perfil_id fica na tabela ALUNOS porque Aluno é o lado que possui a associação.

## Banco

~~~text
PERFIS
----------------
id
telefone
cidade

ALUNOS
----------------
id
nome
email
perfil_id
~~~

Confira:

~~~sql
SELECT * FROM ALUNOS;
SELECT * FROM PERFIS;
~~~

## Criando um perfil

Endpoint:

~~~http
POST /perfis
~~~

JSON enviado:

~~~json
{
  "telefone": "11999999999",
  "cidade": "São Paulo"
}
~~~

Considerando os dados iniciais, uma resposta possível é:

~~~json
{
  "id": 3,
  "telefone": "11999999999",
  "cidade": "São Paulo"
}
~~~

## Criando um aluno

Endpoint:

~~~http
POST /alunos
~~~

JSON enviado:

~~~json
{
  "nome": "Beatriz",
  "email": "beatriz@email.com"
}
~~~

Resposta:

~~~json
{
  "id": 3,
  "nome": "Beatriz",
  "email": "beatriz@email.com",
  "perfil": null,
  "cursos": []
}
~~~

## Associando o perfil ao aluno

Endpoint:

~~~http
PUT /alunos/3/perfil/3
~~~

Não existe body. Os dois IDs estão na URL.

Resposta:

~~~json
{
  "id": 3,
  "nome": "Beatriz",
  "email": "beatriz@email.com",
  "perfil": {
    "id": 3,
    "telefone": "11999999999",
    "cidade": "São Paulo"
  },
  "cursos": []
}
~~~

Depois da associação:

~~~sql
SELECT * FROM ALUNOS WHERE ID = 3;
~~~

O valor de PERFIL_ID passa a ser 3.

## Consultando

~~~http
GET /alunos/3
~~~

Resposta:

~~~json
{
  "id": 3,
  "nome": "Beatriz",
  "email": "beatriz@email.com",
  "perfil": {
    "id": 3,
    "telefone": "11999999999",
    "cidade": "São Paulo"
  },
  "cursos": []
}
~~~

## Removendo a associação

~~~http
DELETE /alunos/3/perfil
~~~

Resposta: 204 No Content.

O aluno continua existindo e o perfil também. Apenas a FK perfil_id do aluno passa a ser null.

### Perguntas para a turma

1. Em qual tabela está a chave estrangeira?
2. O que o nome perfil_id representa?
3. Se o valor de perfil_id for 2, qual Perfil está relacionado ao Aluno?
4. O que muda no banco quando removemos a associação?

---

# 2. @ManyToOne — Muitos Cursos para um Instrutor

## Regra de negócio

Vários cursos podem pertencer ao mesmo instrutor.

~~~text
INSTRUTOR 1 ───── N CURSOS
~~~

Do ponto de vista do Curso:

~~~text
Muitos Cursos → um Instrutor
~~~

## Java/JPA

Em Curso.java:

~~~java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "instrutor_id", nullable = false)
private Instrutor instrutor;
~~~

## Banco

~~~text
INSTRUTORES
----------------
id
nome
email

CURSOS
----------------
id
nome
carga_horaria
instrutor_id
~~~

Regra importante:

> Em um relacionamento 1:N, a chave estrangeira normalmente fica no lado N.

## Criando um instrutor

~~~http
POST /instrutores
~~~

JSON enviado:

~~~json
{
  "nome": "Roberta",
  "email": "roberta@email.com"
}
~~~

Resposta possível:

~~~json
{
  "id": 3,
  "nome": "Roberta",
  "email": "roberta@email.com",
  "cursos": []
}
~~~

## Criando um curso

~~~http
POST /cursos
~~~

JSON enviado:

~~~json
{
  "nome": "Spring Data JPA",
  "cargaHoraria": 24,
  "instrutorId": 3
}
~~~

Resposta possível:

~~~json
{
  "id": 4,
  "nome": "Spring Data JPA",
  "cargaHoraria": 24,
  "instrutor": {
    "id": 3,
    "nome": "Roberta",
    "email": "roberta@email.com"
  },
  "alunos": []
}
~~~

O fluxo é:

~~~text
"instrutorId": 3
        ↓
Service busca Instrutor 3
        ↓
curso.setInstrutor(instrutor)
        ↓
@ManyToOne
        ↓
CURSOS.instrutor_id = 3
~~~

Confira:

~~~sql
SELECT * FROM CURSOS;
~~~

## Trocando o instrutor de um curso

~~~http
PUT /cursos/1/instrutor/2
~~~

Resposta:

~~~json
{
  "id": 1,
  "nome": "Java e Spring",
  "cargaHoraria": 40,
  "instrutor": {
    "id": 2,
    "nome": "Mariana",
    "email": "mariana@email.com"
  },
  "alunos": [
    {
      "id": 1,
      "nome": "Ana",
      "email": "ana@email.com"
    },
    {
      "id": 2,
      "nome": "Pedro",
      "email": "pedro@email.com"
    }
  ]
}
~~~

Depois:

~~~sql
SELECT * FROM CURSOS WHERE ID = 1;
~~~

Observe a alteração de INSTRUTOR_ID.

---

# 3. @OneToMany — Um Instrutor possui vários Cursos

Agora olhamos o mesmo relacionamento pelo outro lado.

## Java/JPA

Em Instrutor.java:

~~~java
@OneToMany(mappedBy = "instrutor")
private Set<Curso> cursos = new LinkedHashSet<>();
~~~

O valor:

~~~java
mappedBy = "instrutor"
~~~

aponta para o atributo:

~~~java
private Instrutor instrutor;
~~~

existente em Curso.java.

Ou seja:

~~~text
Instrutor.java
List/Set de cursos
@OneToMany
       ↕
Curso.java
um instrutor
@ManyToOne
~~~

## Consultando um instrutor

~~~http
GET /instrutores/1
~~~

Resposta inicial:

~~~json
{
  "id": 1,
  "nome": "Carlos",
  "email": "carlos@email.com",
  "cursos": [
    {
      "id": 1,
      "nome": "Java e Spring",
      "cargaHoraria": 40
    },
    {
      "id": 2,
      "nome": "APIs REST com Spring",
      "cargaHoraria": 20
    }
  ]
}
~~~

O JSON ajuda a enxergar o @OneToMany:

~~~json
"cursos": [
  { "...": "curso 1" },
  { "...": "curso 2" }
]
~~~

## Endpoint específico

~~~http
GET /instrutores/1/cursos
~~~

Resposta:

~~~json
[
  {
    "id": 1,
    "nome": "Java e Spring",
    "cargaHoraria": 40
  },
  {
    "id": 2,
    "nome": "APIs REST com Spring",
    "cargaHoraria": 20
  }
]
~~~

### Perguntas para a turma

1. @OneToMany e @ManyToOne representam dois relacionamentos diferentes?
2. Onde está a FK?
3. O que mappedBy = "instrutor" está referenciando?
4. Por que não foi criada uma tabela intermediária?

---

# 4. @ManyToMany — Alunos e Cursos

## Regra de negócio

Um aluno pode fazer vários cursos e um curso pode possuir vários alunos.

~~~text
ALUNO N ───── N CURSO
~~~

## Java/JPA

Em Aluno.java:

~~~java
@ManyToMany
@JoinTable(
    name = "aluno_curso",
    joinColumns = @JoinColumn(name = "aluno_id"),
    inverseJoinColumns = @JoinColumn(name = "curso_id")
)
private Set<Curso> cursos = new LinkedHashSet<>();
~~~

Em Curso.java:

~~~java
@ManyToMany(mappedBy = "cursos")
private Set<Aluno> alunos = new LinkedHashSet<>();
~~~

## Banco

Uma única FK não é suficiente para representar N:N.

Por isso existe a tabela intermediária:

~~~text
ALUNO_CURSO
----------------
aluno_id
curso_id
~~~

Com os dados iniciais, consulte:

~~~sql
SELECT * FROM ALUNO_CURSO;
~~~

## Matriculando um aluno

~~~http
POST /alunos/1/cursos/3
~~~

Não existe body. Os IDs estão na URL.

Resposta:

~~~json
{
  "id": 1,
  "nome": "Ana",
  "email": "ana@email.com",
  "perfil": {
    "id": 1,
    "telefone": "11911111111",
    "cidade": "São Paulo"
  },
  "cursos": [
    {
      "id": 1,
      "nome": "Java e Spring",
      "cargaHoraria": 40
    },
    {
      "id": 2,
      "nome": "APIs REST com Spring",
      "cargaHoraria": 20
    },
    {
      "id": 3,
      "nome": "JPA e Hibernate",
      "cargaHoraria": 24
    }
  ]
}
~~~

No banco é criada uma linha equivalente a:

~~~text
ALUNO_ID | CURSO_ID
1        | 3
~~~

## Cursos de um aluno

~~~http
GET /alunos/1/cursos
~~~

Resposta:

~~~json
[
  {
    "id": 1,
    "nome": "Java e Spring",
    "cargaHoraria": 40
  },
  {
    "id": 2,
    "nome": "APIs REST com Spring",
    "cargaHoraria": 20
  }
]
~~~

## Alunos de um curso

~~~http
GET /cursos/1/alunos
~~~

Resposta:

~~~json
[
  {
    "id": 1,
    "nome": "Ana",
    "email": "ana@email.com"
  },
  {
    "id": 2,
    "nome": "Pedro",
    "email": "pedro@email.com"
  }
]
~~~

## Removendo uma matrícula

~~~http
DELETE /alunos/1/cursos/2
~~~

Resposta: 204 No Content.

A linha correspondente é removida de ALUNO_CURSO. Nem o aluno nem o curso são apagados.

### Perguntas para a turma

1. Por que precisamos da tabela ALUNO_CURSO?
2. O que joinColumns representa?
3. O que inverseJoinColumns representa?
4. O que mappedBy = "cursos" significa em Curso.java?

---

# 5. Todos os endpoints da aplicação

| Método | Endpoint | Função |
|---|---|---|
| POST | /perfis | Criar perfil |
| GET | /perfis | Listar perfis |
| GET | /perfis/{id} | Buscar perfil |
| PUT | /perfis/{id} | Alterar perfil |
| DELETE | /perfis/{id} | Excluir perfil |
| POST | /alunos | Criar aluno |
| GET | /alunos | Listar alunos |
| GET | /alunos/{id} | Buscar aluno |
| PUT | /alunos/{id} | Alterar aluno |
| DELETE | /alunos/{id} | Excluir aluno |
| PUT | /alunos/{id}/perfil/{perfilId} | Associar perfil |
| DELETE | /alunos/{id}/perfil | Remover perfil do aluno |
| POST | /alunos/{alunoId}/cursos/{cursoId} | Matricular aluno |
| DELETE | /alunos/{alunoId}/cursos/{cursoId} | Remover matrícula |
| GET | /alunos/{alunoId}/cursos | Listar cursos do aluno |
| POST | /instrutores | Criar instrutor |
| GET | /instrutores | Listar instrutores |
| GET | /instrutores/{id} | Buscar instrutor |
| PUT | /instrutores/{id} | Alterar instrutor |
| DELETE | /instrutores/{id} | Excluir instrutor |
| GET | /instrutores/{id}/cursos | Cursos do instrutor |
| POST | /cursos | Criar curso |
| GET | /cursos | Listar cursos |
| GET | /cursos/{id} | Buscar curso |
| PUT | /cursos/{id} | Alterar curso |
| DELETE | /cursos/{id} | Excluir curso |
| PUT | /cursos/{id}/instrutor/{instrutorId} | Associar ou trocar instrutor |
| GET | /cursos/{id}/alunos | Listar alunos do curso |

Os endpoints DELETE retornam 204 No Content quando concluídos.

---

# 6. Exercício de fixação sugerido

Adicionar uma nova relação:

~~~text
Categoria 1 ───── N Curso
~~~

A turma deve:

1. criar a entidade Categoria;
2. adicionar @ManyToOne em Curso;
3. adicionar @OneToMany em Categoria;
4. observar onde aparece categoria_id;
5. criar os endpoints mínimos;
6. testar o JSON;
7. conferir o H2.

A pergunta central é:

> Em qual tabela deve ficar a chave estrangeira?
