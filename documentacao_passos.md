# Passos de Implementação - Projeto Doença de Chagas

## Passo 1
- Criação do arquivo `informacoes_projeto.md` contendo a introdução, tecnologias e estrutura do projeto fornecidas inicialmente.
- Criação do arquivo `documentacao_passos.md` para registrar o andamento do desenvolvimento (este arquivo).
- **Observação:** As informações sobre as 3 entidades não puderam ser adicionadas pois a imagem mencionada não foi enviada. Estamos aguardando o envio desses detalhes pelo usuário.

## Passo 2
- Recebimento da imagem com a modelagem do banco de dados contendo as 3 entidades (`cidades`, `coletas`, `pacientes`).
- Atualização do arquivo `informacoes_projeto.md` com os atributos e detalhes de cada uma dessas 3 entidades, bem como correção da estrutura do projeto para incluir a entidade `Paciente`.
- Aguardando próximas instruções para iniciar a criação do projeto ou de novas classes.

## Passo 3
- Atualização do `pom.xml` para incluir a dependência do JavaFX, uma vez que as demais (Spring Data JPA, MySQL, Lombok) já constavam no projeto inicializado.
- Criação do esqueleto de pacotes (`model`, `repository`, `service`, `controller`) dentro da estrutura já existente (`com.example.DoencaChagas`).
- Criação das classes vazias (`Cidade`, `ColetaSemanal`, `Paciente`, `BancoDeDadosDAO`, `SistemaService`, `AppController`).
- *Aguardando próximas instruções para implementar os atributos e métodos em cada classe.*

## Passo 4
- Implementação completa das 3 Entidades JPA (`Cidade`, `ColetaSemanal`, `Paciente`).
- Mapeamento das tabelas (`@Table`), colunas (`@Column`), chaves primárias (`@Id`) e relacionamentos estrangeiros (`@ManyToOne` com `Cidade`).
- Inclusão das anotações do Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`) para geração automática de getters, setters e construtores, mantendo o código limpo.

## Passo 5
- Remoção das propriedades focadas em criação de banco (`length`, `nullable`) das anotações `@Column` e `@JoinColumn` nas três Entidades. Agora restam apenas as anotações estritamente necessárias para o mapeamento e leitura/escrita dos dados no banco existente.
- Adição da configuração `spring.jpa.hibernate.ddl-auto=none` no arquivo `application.properties`. Isso é o que realmente garante que o Hibernate/JPA **não tentará criar as tabelas**, usando o banco de dados apenas para buscar e adicionar informações.
