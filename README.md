# GerenciaEJ

## Configuração do ambiente

### Pré-requisitos
- Java 17
- MySQL 8
- VS Code com a extensão **Extension Pack for Java**

---

### 1. Clonar o repositório
```bash
git clone <url-do-repositorio>
```

---

### 2. Adicionar o driver do MySQL

Baixe o driver em: https://dev.mysql.com/downloads/connector/j/
- Escolha **Platform Independent** e baixe o `.zip`
- Extraia e copie o arquivo `mysql-connector-j-9.7.0.jar` para a pasta `lib/` do projeto

---

### 3. Configurar o VS Code

Crie o arquivo `.vscode/settings.json` na raiz do projeto com o conteúdo:
```json
{
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ]
}
```

---

### 4. Criar o banco de dados

Abra o terminal e entre no MySQL:
```bash
mysql -u root -p
```

Saia do MySQL:
```
\q
```

Rode o script para criar o banco:
```bash
mysql -u root -p < gerencia_ej_v2.sql
```

---

### 5. Configurar a conexão

Abra o arquivo `model/DatabaseConnection.java` e atualize com suas credenciais:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/gerencia_ej";
private static final String USER     = "root";
private static final String PASSWORD = "sua_senha";
```

---

### 6. Rodar o projeto
```bash
java -cp .:lib/mysql-connector-j-9.7.0.jar Main
```

> **Windows:** troque `:` por `;` no classpath:
> ```bash
> java -cp .;lib\mysql-connector-j-9.7.0.jar Main
> ```
