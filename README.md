# REST Spring Boot API - Gerenciamento de Pessoas

Uma API REST robusta desenvolvida com Spring Boot para gerenciamento de pessoas, incluindo funcionalidades avançadas como importação/exportação de arquivos, paginação, HATEOAS e documentação automatizada.

## 🚀 Funcionalidades

- **CRUD Completo**: Criar, ler, atualizar e deletar pessoas
- **Paginação**: Consultas paginadas para melhor performance
- **Busca por Nome**: Filtro de pessoas por nome
- **Importação em Massa**: Upload de arquivos CSV/Excel para criação de múltiplas pessoas
- **Exportação de Dados**: Exportar dados em diferentes formatos (CSV, Excel, PDF)
- **HATEOAS**: Links hipermídia para navegação entre recursos
- **Documentação API**: Swagger/OpenAPI integrado
- **Versionamento**: API versionada (v1)
- **Validação**: Validação de dados de entrada
- **Tratamento de Exceções**: Tratamento robusto de erros

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.6**
- **Spring Data JPA**
- **Spring HATEOAS**
- **MySQL 8.0**
- **Flyway** (Migrações de banco)
- **Lombok** (Redução de boilerplate)
- **Dozer Mapper** (Mapeamento de objetos)
- **SpringDoc OpenAPI** (Documentação)
- **Apache POI** (Manipulação de arquivos Excel)
- **Apache Commons CSV** (Manipulação de arquivos CSV)
- **JasperReports** (Geração de relatórios PDF)
- **TestContainers** (Testes de integração)
- **REST Assured** (Testes de API)

## 📋 Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- MySQL 8.0+
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## 📚 Documentação da API

### Swagger UI
Acesse a documentação interativa em: `http://localhost:8080/swagger-ui.html`

### OpenAPI JSON
Especificação OpenAPI disponível em: `http://localhost:8080/v3/api-docs`

## 🌐 Endpoints Principais

### Pessoas
- `GET /api/person/v1` - Listar todas as pessoas (paginado)
- `GET /api/person/v1/{id}` - Buscar pessoa por ID
- `GET /api/person/v1/findByName` - Buscar pessoas por nome
- `POST /api/person/v1` - Criar nova pessoa
- `PUT /api/person/v1` - Atualizar pessoa
- `PATCH /api/person/v1/{id}` - Desabilitar pessoa
- `DELETE /api/person/v1/{id}` - Deletar pessoa

### Importação/Exportação
- `POST /api/person/v1/massCreation` - Importar pessoas via arquivo (CSV/Excel)
- `GET /api/person/v1/exportPage` - Exportar pessoas (CSV/Excel/PDF)

### Parâmetros de Consulta
- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)
- `sort`: Ordenação (padrão: asc)
- `firstName`: Nome para filtro

## 📝 Exemplo de Uso

### Criar uma pessoa
```bash
curl -X POST http://localhost:8080/api/person/v1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João",
    "lastName": "Silva",
    "address": "Rua das Flores, 123",
    "gender": "Male",
    "enabled": true
  }'
```

### Buscar pessoas paginadas
```bash
curl "http://localhost:8080/api/person/v1?page=0&size=5&sort=firstName,asc"
```

### Importar pessoas via arquivo
```bash
curl -X POST http://localhost:8080/api/person/v1/massCreation \
  -H "Content-Type: multipart/form-data" \
  -F "file=@pessoas.csv"
```

## 📊 Modelo de Dados

### Pessoa
```json
{
  "id": 1,
  "firstName": "João",
  "lastName": "Silva",
  "address": "Rua das Flores, 123",
  "gender": "Male",
  "enabled": true
}
```

## 🧪 Testes

### Executar todos os testes
```bash
mvn test
```

### Executar testes de integração
```bash
mvn test -Dtest=**/*IT
```

### Executar testes unitários
```bash
mvn test -Dtest=**/*Test
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/github/guiziin227/restspringboot/
│   │       ├── config/          # Configurações
│   │       ├── controller/      # Controladores REST
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── exception/       # Tratamento de exceções
│   │       ├── file/            # Importação/Exportação
│   │       ├── model/           # Entidades JPA
│   │       ├── repository/      # Repositórios
│   │       ├── serializer/      # Serializadores
│   │       └── service/         # Lógica de negócio
│   └── resources/
│       ├── application.yml      # Configurações da aplicação
│       ├── db/migration/        # Scripts Flyway
│       └── templates/           # Templates JasperReports
└── test/
    └── java/                    # Testes unitários e integração
```

## 🎯 Funcionalidades Avançadas

### HATEOAS
A API implementa HATEOAS (Hypermedia as the Engine of Application State), fornecendo links de navegação em cada resposta.

### Versionamento
A API é versionada através da URL (`/v1/`), permitindo evolução sem quebrar compatibilidade.

### Múltiplos Formatos
Suporte para múltiplos formatos de dados:
- JSON
- XML
- YAML

### Paginação
Todas as consultas de listagem suportam paginação com metadados incluídos na resposta.

### Validação
Validação robusta de dados de entrada com mensagens de erro descritivas.

## 🔒 Segurança

- Validação de entrada de dados
- Tratamento de exceções personalizado
- Configuração CORS para diferentes origens
- Sanitização de uploads de arquivos

## 📈 Monitoramento

A aplicação inclui logs detalhados para monitoramento:
- Logs de operações CRUD
- Logs de importação/exportação
- Logs de erro com stack traces

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 👨‍💻 Autor

**guiziin227**
- GitHub: [@guiziin227](https://github.com/guiziin227)

## 🆘 Suporte

Se você encontrar algum problema ou tiver dúvidas:
1. Verifique os logs da aplicação
2. Consulte a documentação da API no Swagger
3. Abra uma issue no GitHub
4. Verifique se o banco de dados está configurado corretamente

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no repositório!
