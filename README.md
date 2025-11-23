# 🐾 Sistema de Adoção de Animais

Sistema completo de gerenciamento de adoção de animais desenvolvido em Java, com suporte a persistência em banco de dados MySQL e arquivos de texto. O sistema permite gerenciar animais, adotantes e realizar adoções seguindo regras de negócio específicas.

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Como Executar](#-como-executar)
- [Documentação das Classes](#-documentação-das-classes)
- [Regras de Negócio](#-regras-de-negócio)
- [Estrutura do Banco de Dados](#-estrutura-do-banco-de-dados)
- [Scripts Disponíveis](#-scripts-disponíveis)
- [Exemplos de Uso](#-exemplos-de-uso)

---

## 🎯 Sobre o Projeto

Este projeto é um sistema completo de gerenciamento de adoção de animais que permite:

- Cadastro e gerenciamento de animais (cachorros e gatos)
- Cadastro e gerenciamento de adotantes
- Realização de adoções com validações de regras de negócio
- Consulta e relatórios de adoções
- Persistência de dados em MySQL ou arquivos de texto

O sistema foi desenvolvido seguindo princípios de orientação a objetos, com separação de responsabilidades entre camadas de modelo, repositório e serviço.

---

## ✨ Funcionalidades

### Gerenciamento de Animais
- ✅ Cadastrar novos animais (Cachorro ou Gato)
- ✅ Listar todos os animais cadastrados
- ✅ Atualizar informações de animais
- ✅ Remover animais do sistema
- ✅ Visualizar status de disponibilidade

### Gerenciamento de Adotantes
- ✅ Cadastrar novos adotantes
- ✅ Listar todos os adotantes
- ✅ Atualizar informações de adotantes
- ✅ Remover adotantes do sistema
- ✅ Visualizar histórico de adoções por adotante

### Gerenciamento de Adoções
- ✅ Realizar novas adoções com validações automáticas
- ✅ Listar todas as adoções
- ✅ Filtrar adoções por adotante
- ✅ Filtrar adoções por período
- ✅ Validação automática de limites e disponibilidade

---

## 🛠 Tecnologias Utilizadas

- **Java** - Linguagem de programação principal
- **MySQL 8.0** - Banco de dados relacional
- **MySQL Connector/J 9.5.0** - Driver JDBC para conexão com MySQL
- **Docker** - Containerização do banco de dados
- **Docker Compose** - Orquestração de containers
- **PowerShell** - Scripts de automação (Windows)

---

## 🏗 Arquitetura do Projeto

O projeto segue uma arquitetura em camadas:

```
┌─────────────────────────────────┐
│         Camada de Apresentação   │
│           (Main.java)            │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│      Camada de Serviço          │
│    (ServicoAdocao.java)         │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│     Camada de Repositório       │
│  (Repositorio Interface)        │
│  ├── MySQLRepositorio           │
│  └── TxtRepositorio             │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│      Camada de Modelo           │
│  ├── Animal (abstrata)          │
│  │   ├── Cachorro               │
│  │   └── Gato                   │
│  ├── Adotante                   │
│  ├── Adocao                     │
│  └── CuidadosEspeciais          │
└─────────────────────────────────┘
```

### Padrões de Projeto Utilizados

- **Repository Pattern**: Abstração da camada de persistência
- **Strategy Pattern**: Diferentes implementações de repositório (MySQL e TXT)
- **Template Method**: Classe abstrata Animal com métodos concretos e abstratos
- **Interface Segregation**: Interface CuidadosEspeciais para funcionalidades específicas

---

## 📁 Estrutura do Projeto

```
Projeto-de-Adocao-De-Animais/
│
├── src/
│   └── main/
│       └── java/
│           ├── Main.java                    # Classe principal com interface de menu
│           ├── exceptions/                  # Exceções customizadas
│           │   ├── AnimalIndisponivelException.java
│           │   └── LimiteAdocoesException.java
│           ├── model/                       # Modelos de domínio
│           │   ├── Animal.java              # Classe abstrata base
│           │   ├── Cachorro.java            # Implementação para cachorros
│           │   ├── Gato.java                # Implementação para gatos
│           │   ├── Adotante.java            # Modelo de adotante
│           │   ├── Adocao.java              # Modelo de adoção
│           │   └── CuidadosEspeciais.java   # Interface para cuidados
│           ├── repository/                  # Camada de persistência
│           │   ├── Repositorio.java         # Interface do repositório
│           │   ├── MySQLRepositorio.java    # Implementação MySQL
│           │   └── TxtRepositorio.java      # Implementação arquivo texto
│           └── service/                     # Camada de serviços
│               └── ServicoAdocao.java       # Serviço de adoção
│
├── lib/                                     # Bibliotecas externas
│   └── mysql-connector-j-9.5.0/            # Driver MySQL
│
├── data/                                    # Arquivos de dados (TXT)
│   ├── adotantes.txt
│   ├── animais.txt
│   └── adocoes.txt
│
├── mysql_data/                              # Dados do MySQL (Docker)
│
├── out/                                     # Arquivos compilados
│
├── docker-compose.yml                       # Configuração Docker
├── backup.sql                              # Backup do banco de dados
│
├── setup-docker.ps1                        # Script de configuração Docker
├── restore-backup.ps1                      # Script de restauração
├── compilar-e-executar.ps1                 # Script de compilação e execução
├── verificar-docker.ps1                    # Script de verificação
├── baixar-driver-mysql.ps1                 # Script de download do driver
│
├── GUIA_DOCKER.md                          # Guia de uso do Docker
├── GUIA_DE_TESTES.md                       # Guia de testes
├── INSTALAR_DRIVER_MYSQL.md                # Guia de instalação do driver
├── DOWNLOAD_MANUAL_DRIVER.md               # Guia de download manual
│
└── README.md                               # Este arquivo
```

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java JDK 8 ou superior** - [Download Oracle JDK](https://www.oracle.com/java/technologies/downloads/) ou [OpenJDK](https://openjdk.org/)
- **Docker Desktop** - [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
- **PowerShell** (já incluído no Windows 10/11)
- **Git** (opcional, para clonar o repositório)

---

## 🚀 Instalação e Configuração

### 1. Configuração Rápida do Banco de Dados

#### Opção A: Configuração Automática (Recomendado)

1. **Certifique-se de que o arquivo `backup.sql` está na raiz do projeto**

2. **Execute o script de configuração:**
   ```powershell
   .\setup-docker.ps1
   ```

3. **Se você tem um backup, restaure-o:**
   ```powershell
   .\restore-backup.ps1
   ```

4. **Pronto! O banco de dados está configurado e pronto para uso.**

#### Opção B: Configuração Manual

Se preferir fazer manualmente, consulte o arquivo `GUIA_DOCKER.md` para instruções detalhadas.

### 2. Configuração do Driver MySQL

O driver MySQL já está incluído na pasta `lib/`. Se necessário, consulte:
- `INSTALAR_DRIVER_MYSQL.md` - Instruções de instalação
- `DOWNLOAD_MANUAL_DRIVER.md` - Download manual do driver

### 3. Verificação da Configuração

Execute o script de verificação:
```powershell
.\verificar-docker.ps1
```

---

## ▶️ Como Executar

### Compilação e Execução Automática

Execute o script que compila e executa o projeto:
```powershell
.\compilar-e-executar.ps1
```

### Compilação Manual

1. **Compilar o projeto:**
   ```powershell
   javac -cp "lib/mysql-connector-j-9.5.0/mysql-connector-j-9.5.0.jar" -d out src/main/java/**/*.java
   ```

2. **Executar o projeto:**
   ```powershell
   java -cp "out;lib/mysql-connector-j-9.5.0/mysql-connector-j-9.5.0.jar" Main
   ```

### Comandos Úteis do Docker

```powershell
# Iniciar banco de dados
docker-compose up -d

# Parar banco de dados
docker-compose down

# Ver logs do MySQL
docker logs -f adocao_mysql

# Acessar MySQL via linha de comando
docker exec -it adocao_mysql mysql -uroot -p1234 db_adocao_novo

# Verificar status dos containers
docker ps
```

---

## 📚 Documentação das Classes

### 🎯 Camada de Apresentação

#### `Main.java`
Classe principal que contém a interface de menu interativa do sistema.

**Responsabilidades:**
- Exibir menus interativos
- Capturar entrada do usuário
- Coordenar operações entre serviços e repositórios
- Validação de entrada de dados

**Métodos Principais:**
- `main(String[] args)` - Ponto de entrada da aplicação
- `exibirMenuPrincipal()` - Menu principal do sistema
- `gerenciarAnimais()` - Submenu de gerenciamento de animais
- `gerenciarAdotantes()` - Submenu de gerenciamento de adotantes
- `realizarAdocao()` - Interface para realizar adoções
- `listarAdocoes()` - Interface para listar e filtrar adoções

---

### 🏛 Camada de Modelo

#### `Animal.java` (Classe Abstrata)
Classe base abstrata que representa um animal no sistema.

**Atributos:**
- `id` (int) - Identificador único
- `nome` (String) - Nome do animal
- `peso` (BigDecimal) - Peso em quilogramas
- `altura` (BigDecimal) - Altura em metros
- `cor` (String) - Cor do animal
- `sexo` (char) - Sexo (M/F)
- `dataNascimento` (LocalDate) - Data de nascimento
- `adotado` (boolean) - Status de adoção
- `especie` (String) - Espécie do animal

**Métodos Principais:**
- `emitirSom()` - Método abstrato para emitir som (implementado nas subclasses)
- `serAdotado()` - Marca o animal como adotado (lança exceção se já estiver adotado)
- `getId()`, `getNome()`, `getPeso()`, etc. - Getters padrão
- `setId()`, `setNome()`, `setPeso()`, etc. - Setters padrão

**Exceções:**
- `AnimalIndisponivelException` - Lançada quando tenta adotar um animal já adotado

---

#### `Cachorro.java`
Classe que representa um cachorro, estendendo `Animal` e implementando `CuidadosEspeciais`.

**Características:**
- Espécie: "Cachorro"
- Som emitido: "Au Au!"

**Métodos Implementados:**
- `emitirSom()` - Retorna "Au Au!"
- `vacinar()` - Implementação da interface CuidadosEspeciais
- `vermifugar()` - Implementação da interface CuidadosEspeciais

**Construtores:**
- `Cachorro(String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento, boolean adotado)`
- `Cachorro(int id, String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento, boolean adotado, String especie)`

---

#### `Gato.java`
Classe que representa um gato, estendendo `Animal` e implementando `CuidadosEspeciais`.

**Características:**
- Espécie: "Gato"
- Som emitido: "Miau Miau!"

**Métodos Implementados:**
- `emitirSom()` - Retorna "Miau Miau!"
- `vacinar()` - Implementação da interface CuidadosEspeciais
- `vermifugar()` - Implementação da interface CuidadosEspeciais

**Construtores:**
- `Gato(String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento, boolean adotado)`
- `Gato(int id, String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento, boolean adotado, String especie)`

---

#### `Adotante.java`
Classe que representa um adotante no sistema.

**Atributos:**
- `id` (int) - Identificador único
- `nome` (String) - Nome do adotante
- `sexo` (char) - Sexo (M/F)
- `dataNascimento` (LocalDate) - Data de nascimento
- `animaisAdotados` (List<Animal>) - Lista de animais adotados
- `LIMITE_ADOCOES` (static final int) - Limite de 3 adoções por adotante

**Métodos Principais:**
- `atingiuLimite()` - Verifica se o adotante atingiu o limite de adoções
- `adcionarAnimal(Animal animal)` - Adiciona um animal à lista (lança exceção se exceder limite)
- `getLimiteAdocoes()` - Retorna o limite de adoções (3)
- Getters e setters padrão

**Exceções:**
- `LimiteAdocoesException` - Lançada quando tenta adicionar animal além do limite

---

#### `Adocao.java`
Classe que representa uma adoção no sistema.

**Atributos:**
- `id` (int) - Identificador único
- `adotante` (Adotante) - Adotante responsável
- `animal` (Animal) - Animal adotado
- `dataAdocao` (LocalDate) - Data em que a adoção foi realizada

**Métodos Principais:**
- Getters e setters padrão
- `toString()` - Formatação para exibição

---

#### `CuidadosEspeciais.java` (Interface)
Interface que define métodos para cuidados especiais com animais.

**Métodos:**
- `vacinar()` - Realiza vacinação do animal
- `vermifugar()` - Realiza vermifugação do animal

**Implementada por:**
- `Cachorro`
- `Gato`

---

### 🔧 Camada de Serviço

#### `ServicoAdocao.java`
Classe de serviço que contém a lógica de negócio para realização de adoções.

**Responsabilidades:**
- Validar disponibilidade do animal
- Validar limite de adoções do adotante
- Coordenar a persistência da adoção
- Atualizar status do animal e adotante

**Métodos Principais:**
- `realizarAdoção(Adotante adotante, Animal animal)` - Realiza uma adoção com todas as validações

**Validações Realizadas:**
1. Verifica se o animal está disponível (não adotado)
2. Recarrega o adotante do banco para ter dados atualizados
3. Verifica se o adotante atingiu o limite de 3 adoções
4. Marca o animal como adotado
5. Persiste a adoção no repositório
6. Atualiza o status do animal no repositório

**Exceções Tratadas:**
- `AnimalIndisponivelException` - Animal já foi adotado
- `LimiteAdocoesException` - Adotante atingiu limite de adoções
- `Exception` - Erros gerais de persistência

---

### 💾 Camada de Repositório

#### `Repositorio.java` (Interface)
Interface que define o contrato para persistência de dados.

**Métodos para Adotantes:**
- `salvarAdotante(Adotante adotante)`
- `atualizarAdotante(Adotante adotante)`
- `buscarAdotantePorId(int id)`
- `excluirAdotante(int id)`
- `listaTodosAdotantes()`

**Métodos para Animais:**
- `salvarAnimal(Animal animal)`
- `atualizarAnimal(Animal animal)`
- `buscarAnimalPorId(int id)`
- `excluirAnimal(int id)`
- `listaTodosAnimais()`

**Métodos para Adoções:**
- `salvarAdocao(Adocao adocao)`
- `buscarAdocaoPorId(int id)`
- `listaTodasAdocoes()`

---

#### `MySQLRepositorio.java`
Implementação do repositório usando MySQL como banco de dados.

**Configuração:**
- URL: `jdbc:mysql://localhost:3306/db_adocao_novo`
- Usuário: `root`
- Senha: `1234`
- Driver: `com.mysql.cj.jdbc.Driver`

**Características:**
- Usa `PreparedStatement` para prevenir SQL Injection
- Gerencia conexões com try-with-resources
- Retorna IDs gerados automaticamente
- Usa JOINs para carregar relacionamentos

**Métodos Especiais:**
- `getConnection()` - Estabelece conexão com o banco
- `testarConexao()` - Testa a conexão com o banco

---

#### `TxtRepositorio.java`
Implementação do repositório usando arquivos de texto.

**Características:**
- Persistência em arquivos `.txt` na pasta `data/`
- Formato: valores separados por ponto e vírgula (`;`)
- Cria automaticamente arquivos e diretórios se não existirem
- Gerencia IDs sequenciais automaticamente

**Arquivos Utilizados:**
- `data/adotantes.txt` - Formato: `ID;NOME;SEXO;DATA_NASCIMENTO`
- `data/animais.txt` - Formato: `ID;ESPECIE;NOME;PESO;ALTURA;SEXO;DATA_NASCIMENTO;ADOTADO`
- `data/adocoes.txt` - Formato: `ID;ID_ADOTANTE;ID_ANIMAL;DATA_ADOCAO`

**Métodos Auxiliares:**
- `toLine()` - Converte objeto para linha de texto
- `fromLine()` - Converte linha de texto para objeto
- `getNextId()` - Gera próximo ID sequencial

---

### ⚠️ Camada de Exceções

#### `AnimalIndisponivelException.java`
Exceção lançada quando se tenta adotar um animal que já foi adotado.

**Herança:** `RuntimeException`

**Uso:**
- Lançada em `Animal.serAdotado()` quando o animal já está adotado
- Tratada em `ServicoAdocao.realizarAdoção()`

---

#### `LimiteAdocoesException.java`
Exceção lançada quando um adotante tenta adotar mais animais do que o permitido.

**Herança:** `RuntimeException`

**Uso:**
- Lançada em `Adotante.adcionarAnimal()` quando o limite é excedido
- Tratada em `ServicoAdocao.realizarAdoção()`

---

## 📜 Regras de Negócio

### Regra 1: Limite de Adoções
- **Cada adotante pode adotar no máximo 3 animais**
- A validação é feita automaticamente antes de cada adoção
- O sistema verifica o histórico de adoções do adotante no banco de dados

### Regra 2: Disponibilidade de Animais
- **Um animal só pode ser adotado se estiver disponível**
- Animais já adotados não podem ser adotados novamente
- O status é atualizado automaticamente após a adoção

### Regra 3: Integridade Referencial
- Ao excluir um adotante, todas as suas adoções são removidas
- Ao excluir um animal, todas as adoções relacionadas são removidas
- O sistema mantém a consistência dos dados

### Regra 4: Validação de Dados
- Datas devem estar no formato `dd/MM/yyyy`
- Campos obrigatórios são validados antes da persistência
- IDs são gerados automaticamente pelo sistema

---

## 🗄 Estrutura do Banco de Dados

### Tabela: `adotantes`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `adotante_id` | INT | Chave primária, auto-incremento |
| `nome` | VARCHAR(100) | Nome do adotante |
| `sexo` | CHAR(1) | Sexo (M/F) |
| `dataNascimento` | DATE | Data de nascimento |

### Tabela: `animais`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `animal_id` | INT | Chave primária, auto-incremento |
| `nome` | VARCHAR(100) | Nome do animal |
| `peso` | DECIMAL(5,2) | Peso em quilogramas |
| `altura` | DECIMAL(5,2) | Altura em metros |
| `cor` | VARCHAR(50) | Cor do animal |
| `sexo` | CHAR(1) | Sexo (M/F) |
| `dataNascimento` | DATE | Data de nascimento |
| `adotado` | TINYINT(1) | Status de adoção (0 = disponível, 1 = adotado) |
| `especie` | VARCHAR(50) | Espécie (Cachorro/Gato) |

### Tabela: `adocoes`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `adocao_id` | INT | Chave primária, auto-incremento |
| `dataAdocao` | DATE | Data da adoção |
| `adotante_id` | INT | Chave estrangeira para `adotantes` |
| `animal_id` | INT | Chave estrangeira para `animais` |

**Relacionamentos:**
- `adocoes.adotante_id` → `adotantes.adotante_id` (Foreign Key)
- `adocoes.animal_id` → `animais.animal_id` (Foreign Key)

---

## 📜 Scripts Disponíveis

### `setup-docker.ps1`
Script que configura automaticamente o ambiente Docker:
- Verifica se o Docker está rodando
- Inicia o container MySQL
- Aguarda o banco estar pronto
- Cria o banco de dados se necessário

### `restore-backup.ps1`
Script que restaura um backup do banco de dados:
- Verifica se o arquivo `backup.sql` existe
- Restaura o backup no banco de dados

### `compilar-e-executar.ps1`
Script que compila e executa o projeto:
- Compila todos os arquivos Java
- Executa a aplicação com as dependências corretas

### `verificar-docker.ps1`
Script que verifica o status do Docker:
- Verifica se o Docker está rodando
- Verifica se o container MySQL está ativo
- Testa a conexão com o banco

### `baixar-driver-mysql.ps1`
Script que baixa o driver MySQL automaticamente.

---

## 💡 Exemplos de Uso

### Exemplo 1: Cadastrar um Animal

```
--- MENU PRINCIPAL ---
1. Gerenciar Animais
2. Gerenciar Adotantes
3. Realizar Adoção
4. Listar Adoções
0. Sair
Escolha uma opção: 1

--- Gerenciar Animais ---
1. Cadastrar Animal
2. Listar Todos os Animais
3. Atualizar Animal
4. Remover Animal
0. Voltar
Escolha uma opção: 1

-- Cadastrar Animal --
Tipo (1-Cachorro, 2-Gato): 1
Nome: Max
Peso: 15.50
Altura: 0.50
Cor: Caramelo
Sexo: M
Data de Nascimento: 15/01/2023

Animal cadastrado com sucesso!
```

### Exemplo 2: Realizar uma Adoção

```
--- MENU PRINCIPAL ---
Escolha uma opção: 3

--- Realizar Adoção ---
Digite o ID do Adotante: 1
Digite o ID do Animal: 5

Adoção realizada com sucesso.
```

### Exemplo 3: Listar Adoções por Período

```
--- MENU PRINCIPAL ---
Escolha uma opção: 4

--- Listar Adoções ---
1. Listar todas
2. Filtrar por Adotante
3. Filtrar por Período
Escolha uma opção: 3

Digite a data inicial (dd/MM/yyyy): 01/01/2024
Digite a data final (dd/MM/yyyy): 31/12/2024

-- Registros de Adoção --
ID: 1 | Adotante: João Silva | Animal: Max | Data: 15/03/2024
ID: 2 | Adotante: Maria Santos | Animal: Bella | Data: 20/03/2024
```

---

## 🔧 Configuração do Banco de Dados

### Credenciais Padrão

- **Host:** localhost
- **Porta:** 3306
- **Banco:** db_adocao_novo
- **Usuário:** root
- **Senha:** 1234

### Alterando as Credenciais

Para alterar as credenciais, edite o arquivo `src/main/java/repository/MySQLRepositorio.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/db_adocao_novo";
private static final String USER = "root";
private static final String PASSWORD = "1234";
```

E também atualize o arquivo `docker-compose.yml` se necessário.

---

## 🐛 Solução de Problemas

### Erro: "Driver MySQL não encontrado"
**Solução:** Verifique se o arquivo `mysql-connector-j-9.5.0.jar` está na pasta `lib/mysql-connector-j-9.5.0/`. Execute `.\baixar-driver-mysql.ps1` se necessário.

### Erro: "Falha na conexão com o Docker MySQL"
**Solução:** 
1. Verifique se o Docker está rodando: `docker ps`
2. Inicie o container: `docker-compose up -d`
3. Aguarde alguns segundos para o MySQL inicializar

### Erro: "Animal não encontrado"
**Solução:** Verifique se o ID do animal existe. Use a opção "Listar Todos os Animais" para ver os IDs disponíveis.

### Erro: "Adotante atingiu o limite de adoções"
**Solução:** Este é um comportamento esperado. Cada adotante pode adotar no máximo 3 animais. Verifique o histórico de adoções do adotante.

---

## 📝 Notas de Desenvolvimento

- O projeto utiliza `BigDecimal` para valores monetários e medidas para garantir precisão
- As datas são tratadas com `LocalDate` do pacote `java.time`
- O sistema suporta dois tipos de persistência: MySQL e arquivos de texto
- A validação de regras de negócio é feita na camada de serviço
- O sistema mantém integridade referencial entre as entidades

---

## 📄 Licença

Este projeto está sob a licença especificada no arquivo `LICENSE`.

---

## 👥 Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📞 Suporte

Para dúvidas ou problemas:
- Consulte os guias na pasta raiz do projeto
- Verifique os logs do Docker: `docker logs adocao_mysql`
- Verifique os logs da aplicação no console

---

**Desenvolvido com ❤️ para facilitar a adoção responsável de animais**
