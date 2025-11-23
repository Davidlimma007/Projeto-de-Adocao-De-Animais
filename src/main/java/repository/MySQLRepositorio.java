package repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.*;


public class MySQLRepositorio implements Repositorio{

    private static final String URL = "jdbc:mysql://localhost:3306/db_adocao_novo";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver MySQL não encontrado. O JAR está na pasta 'lib' e configurado?");
            e.printStackTrace();
            // Lança um erro fatal se o Java não encontrar o 'tradutor'
            throw new RuntimeException("Driver MySQL não encontrado.", e);
        }
        // Tenta estabelecer a conexão usando o URL, Usuário e Senha
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public boolean testarConexao(){
        try(Connection connection = getConnection()){
            System.out.println("Status da conexão: Conectado com sucesso!");
            return connection != null;
        }catch (SQLException e){
            System.out.println("Falha na conexão com o Docker Mysql. Detalhes do erro: " + e.getMessage());
            return false;
        }catch (RuntimeException e){
            System.out.println("Falha no carregamento do drive. Detalhes: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void salvarAdotante(Adotante adotante) throws Exception {

        // A instrução SQL que define o que será inserido
        String sql = "INSERT INTO adotantes (nome, sexo, dataNascimento) VALUES (?, ?, ?)";

        // O 'try-with-resources' garante que 'conn' e 'stmt' serão fechados
        try (Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Mapeamento dos Dados (substituindo os '?' da instrução SQL)

            // O primeiro '?' é o nome (String)
            stmt.setString(1, adotante.getNome());

            // O segundo '?' é o sexo (Char/String)
            stmt.setString(2, String.valueOf(adotante.getSexo()));

            // O terceiro '?' é a data de nascimento (Date)
            // Precisamos converter a data java.time.LocalDate para o formato SQL Date
            stmt.setDate(3, java.sql.Date.valueOf(adotante.getDataNascimento()));

            // 2. Execução da Instrução

            // Executa o comando de INSERT
            stmt.executeUpdate();

            try(java.sql.ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    int idGerado = rs.getInt(1);
                    adotante.setId(idGerado);
                    System.out.println("Adotante " + adotante.getNome() + " salvo com sucesso!");
                }
            }


        } catch (SQLException e) {
            System.err.println("Erro ao salvar adotante no banco de dados: " + e.getMessage());
            e.printStackTrace();
            // Propaga a exceção para que o Service/Main possa lidar com ela
            throw new Exception("Falha ao salvar adotante.", e);
        }
    }

    @Override
    public void atualizarAdotante(Adotante adotante) throws Exception {

        // 1. A instrução SQL que define o que será atualizado e quem
        String sql = "UPDATE adotantes SET nome = ?, sexo = ?, dataNascimento = ? WHERE adotante_id = ?";

        // O 'try-with-resources' garante que 'conn' e 'stmt' serão fechados
        try (Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 2. Mapeamento dos Dados (A ordem aqui é fundamental!)

            // Parâmetro 1: Nome (novo valor)
            stmt.setString(1, adotante.getNome());

            // Parâmetro 2: Sexo (novo valor)
            stmt.setString(2, String.valueOf(adotante.getSexo()));

            // Parâmetro 3: Data de Nascimento (novo valor)
            stmt.setDate(3, java.sql.Date.valueOf(adotante.getDataNascimento()));

            // Parâmetro 4: O ID (Quem será atualizado)
            stmt.setInt(4, adotante.getId());

            // 3. Execução da Instrução
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Adotante com ID " + adotante.getId() + " atualizado para "
                                    + adotante.getNome() + " com sucesso!");
            } else {
                System.out.println("Alerta: Nenhum adotante encontrado com ID " + adotante.getId() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar adotante no banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao atualizar adotante.", e);
        }
    }

    @Override
    public Adotante buscarAdotantePorId(int id) throws Exception {

        // A instrução SQL que busca pelo ID.
        String sql = "SELECT adotante_id, nome, sexo, dataNascimento FROM adotantes WHERE adotante_id = ?";

        // O resultado da consulta será armazenado aqui
        Adotante adotante = null;

        // O 'try-with-resources' garante que a Connection e o PreparedStatement sejam fechados
        try (Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
        ) {

            // 1. Mapeamento do parâmetro (substituindo o '?' pelo ID)
            stmt.setInt(1, id);

            // 2. Execução da Consulta (Query)
            // executeQuery() retorna um objeto ResultSet, que contém os dados
            try (java.sql.ResultSet rs = stmt.executeQuery()) {

                // 3. Processamento do Resultado
                // O método next() move o cursor para a primeira linha de dados.
                if (rs.next()) {

                    // Se encontrou, extraímos cada coluna e montamos o objeto Adotante
                    int adotanteId = rs.getInt("adotante_id");
                    String nome = rs.getString("nome");
                    // O sexo é CHAR(1) no banco, pegamos a String e o primeiro caractere
                    char sexo = rs.getString("sexo").charAt(0);

                    // Convertemos a data SQL (Date) de volta para a data Java (LocalDate)
                    java.time.LocalDate dataNascimento = rs.getDate("dataNascimento").toLocalDate();

                    // Cria o objeto final que será retornado
                    adotante = new Adotante(adotanteId, nome, sexo, dataNascimento);

                } else {
                    // Nenhuma linha retornada
                    System.out.println("Adotante com ID " + id + " não encontrado.");
                }
            } // O ResultSet fecha aqui

        } catch (SQLException e) {
            System.err.println("Erro ao buscar adotante no banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao buscar adotante.", e);
        }
        
        // Carregar animais adotados do banco para popular a lista (após fechar a conexão anterior)
        if (adotante != null) {
            try {
                carregarAnimaisAdotados(adotante);
            } catch (SQLException e) {
                System.err.println("Erro ao carregar animais adotados: " + e.getMessage());
                // Não lança exceção, apenas loga o erro
            }
        }

        return adotante;
    }
    
    // Método auxiliar para carregar animais adotados do banco
    private void carregarAnimaisAdotados(Adotante adotante) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM adocoes WHERE adotante_id = ?";
        try (Connection conn = getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adotante.getId());
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalAdocoes = rs.getInt("total");
                    // Popular a lista com objetos "dummy" apenas para contar
                    // Isso mantém a compatibilidade com o método atingiuLimite()
                    for (int i = 0; i < totalAdocoes; i++) {
                        adotante.getAnimaisAdotados().add(null); // null apenas para contar
                    }
                }
            }
        }
    }

    @Override
    public void excluirAdotante(int id) throws Exception{
        String sql = "DELETE FROM adotantes WHERE adotante_id = ?";

        try(Connection conn = getConnection(); java.sql.PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setInt(1, id);

            int linhasAfetadas = stm.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Adotante com ID " + id + " excluído com sucesso!");
            } else {
                System.out.println("Alerta: Nenhum adotante encontrado com ID " + id + " para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir adotante do banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao excluir adotante.", e);
        }

    }


    @Override
    public void salvarAnimal(Animal animal) throws Exception {

        String sql = "INSERT INTO animais (nome, peso, altura, cor, sexo, dataNascimento, adotado, especie) VALUES (?,?,?,?,?,?,?,?)";

        try(Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            //Mapeamento dos Dados (7 Parâmetros)

            // Parâmetro 1: nome (String)
            stmt.setString(1, animal.getNome());

            // Parâmetro 2: peso (BigDecimal -> DECIMAL)
            stmt.setBigDecimal(2, animal.getPeso());

            // Parâmetro 3: altura (BigDecimal -> DECIMAL)
            stmt.setBigDecimal(3, animal.getAltura());

            // Parâmetro 4: cor (String)
            stmt.setString(4, animal.getCor());

            // Parâmetro 5: sexo (char -> CHAR)
            stmt.setString(5, String.valueOf(animal.getSexo()));

            // Parâmetro 6: dataNascimento (LocalDate -> DATE)
            stmt.setDate(6, java.sql.Date.valueOf(animal.getDataNascimento()));

            // Parâmetro 7: adotado (boolean -> TINYINT/BOOLEAN)
            stmt.setBoolean(7, animal.isAdotado()); // O isAdotado() retorna o boolean

            // Parâmetro 8: especie
            stmt.setString(8, animal.getEspecie());

            //2. Execução
            stmt.executeUpdate();

            try (java.sql.ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    int idGerado = rs.getInt(1);
                    animal.setId(idGerado);
                    System.out.println("Animal " + animal.getNome() + " Salvo com sucesso!");
                }
            }

        }catch (SQLException e){
            System.err.println("Erro ao salvar animal no banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao salvar animal.", e);
        }
    }

    @Override
    public void atualizarAnimal(Animal animal) throws Exception {

        String sql = "UPDATE animais SET nome = ?, peso = ?, altura = ?, cor = ?, sexo = ?, " +
                        "dataNascimento = ?, adotado = ? WHERE animal_id = ?";

        try (Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, animal.getNome());

            stmt.setBigDecimal(2, animal.getPeso());

            stmt.setBigDecimal(3, animal.getAltura());

            stmt.setString(4, animal.getCor());

            stmt.setString(5, String.valueOf(animal.getSexo()));

            stmt.setDate(6, java.sql.Date.valueOf(animal.getDataNascimento()));

            stmt.setBoolean(7, animal.isAdotado());

            stmt.setInt(8, animal.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Animal com ID " + animal.getId() + " atualizado para "
                        + animal.getNome() + " com sucesso!");
            } else {
                System.out.println("Alerta: Nenhum animal encontrado com ID " + animal.getId() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar animal no banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao atualizar animal.", e);
        }
    }


    @Override
    public Animal buscarAnimalPorId(int id) throws Exception {
        String sql = "SELECT animal_id, nome, peso, altura, cor, sexo, dataNascimento, adotado, " +
                        "especie FROM animais WHERE animal_id = ?";

        try (Connection conn = getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Mapeia o ID de busca

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Coleta e Mapeamento dos dados do ResultSet
                    int animalId = rs.getInt("animal_id");
                    String nome = rs.getString("nome");
                    BigDecimal peso = rs.getBigDecimal("peso");
                    BigDecimal altura = rs.getBigDecimal("altura");
                    String cor = rs.getString("cor");
                    char sexo = rs.getString("sexo").charAt(0); // Pega o CHAR(1) e converte para char
                    LocalDate dataNascimento = rs.getDate("dataNascimento").toLocalDate();
                    boolean adotado = rs.getBoolean("adotado");
                    String especie = rs.getString("especie"); // Usado para POO!

                    // 2. POO: Decisão de Instanciação com base na 'especie'
                    if (especie.equalsIgnoreCase("Cachorro")) {
                        // Construtor de Busca (com 9 parâmetros, incluindo ID)
                        return new Cachorro(animalId, nome, peso, altura, cor, sexo, dataNascimento, adotado, especie);
                    } else if (especie.equalsIgnoreCase("Gato")) {
                        // Implementação do Gato (assumindo que você criou a classe e o construtor de 9 argumentos)
                        return new Gato(animalId, nome, peso, altura, cor, sexo, dataNascimento, adotado, especie);
                    } else {
                        // Se a espécie não for reconhecida (o erro de abstração é resolvido aqui)
                        throw new Exception("Espécie de animal desconhecida no banco de dados: " + especie);
                    }
                }

                System.out.println("Animal com ID " + id + " não encontrado.");
                return null; // Retorna null se não houver resultado
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar animal no banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao buscar animal.", e);
        }
    }

    @Override
    public void excluirAnimal(int id) throws Exception{
        String sql = "DELETE FROM animais WHERE animal_id = ?";

        try(Connection conn = getConnection(); java.sql.PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setInt(1, id);

            int linhasAfetadas = stm.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Animal com ID " + id + " excluído com sucesso!");
            } else {
                System.out.println("Alerta: Nenhum animal encontrado com ID " + id + " para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir animal do banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao excluir animal.", e);
        }

    }

    @Override
    public void salvarAdocao(Adocao adocao) throws Exception {

        String sqlAdocao = "INSERT INTO adocoes (adotante_id, animal_id, dataAdocao) VALUES (?, ?, ?)";
        String sqlAnimalUpdate = "UPDATE animais SET adotado = true WHERE animal_id = ?";

        // 1. A conexão deve ser declarada fora do bloco try
        Connection conn = null;

        try{
            conn = getConnection();
            conn.setAutoCommit(false);

            // --- PASSO 1: INSERIR A ADOÇÃO (tabela adocoes) ---
            try(java.sql.PreparedStatement stmtAdocao = conn.prepareStatement(sqlAdocao)){

                stmtAdocao.setInt(1, adocao.getAdotante().getId());
                stmtAdocao.setInt(2, adocao.getAnimal().getId());
                stmtAdocao.setDate(3, java.sql.Date.valueOf(adocao.getDataAdocao()));

                stmtAdocao.executeUpdate();
            }

            // --- PASSO 2: ATUALIZAR O STATUS DO ANIMAL (tabela animais) ---
            try(java.sql.PreparedStatement stmtAnimal = conn.prepareStatement(sqlAnimalUpdate)){

                stmtAnimal.setInt(1, adocao.getAnimal().getId());

                stmtAnimal.executeUpdate();
            }

            // 2. Se as duas operações tiveram sucesso, confirma no banco de dados
            conn.commit();

            System.out.println("Transação de Adoção concluída com sucesso! Animal "
                    + adocao.getAnimal().getId() + " adotado por Adotante "
                    + adocao.getAdotante().getId());

        } catch (SQLException e){
            // 3. Se algo deu errado, desfaz ambas as operações (rollback)
            if (conn != null) {
                conn.rollback();
            }
            System.err.println("Erro fatal na transação de adoção: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha na transação de adoção.", e);

        } finally {
            // 4. Garante que a conexão é fechada, mesmo que ocorra um erro
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public Adocao buscarAdocaoPorId(int id) throws Exception {

        // SQL com JOINs e Aliases (apelidos) para evitar conflito de nomes
        String sql = "SELECT " +
                "    A.adocao_id, A.dataAdocao, " +
                "    D.adotante_id, D.nome AS adotante_nome, D.sexo AS adotante_sexo, D.dataNascimento AS adotante_dataNascimento, " +
                "    L.animal_id, L.nome AS animal_nome, L.peso, L.altura, L.cor, L.sexo AS animal_sexo, L.dataNascimento AS animal_dataNascimento, L.adotado, L.especie " +
                "FROM adocoes A " +
                "INNER JOIN adotantes D ON A.adotante_id = D.adotante_id " +
                "INNER JOIN animais L ON A.animal_id = L.animal_id " +
                "WHERE A.adocao_id = ?";

        Adocao adocao = null;

        try (Connection conn = getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Mapeia o ID de busca

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    // 1. EXTRAIR DADOS DA ADOÇÃO (A)
                    int adocaoId = rs.getInt("adocao_id");
                    LocalDate dataAdocao = rs.getDate("dataAdocao").toLocalDate();

                    // 2. CONSTRUIR O OBJETO ADOTANTE (D)
                    int adotanteId = rs.getInt("adotante_id");
                    String adotanteNome = rs.getString("adotante_nome");
                    char adotanteSexo = rs.getString("adotante_sexo").charAt(0);
                    LocalDate adotanteDataNascimento = rs.getDate("adotante_dataNascimento").toLocalDate();

                    Adotante adotante = new Adotante(adotanteId, adotanteNome, adotanteSexo, adotanteDataNascimento);

                    // 3. CONSTRUIR O OBJETO ANIMAL (L)
                    int animalId = rs.getInt("animal_id");
                    String animalNome = rs.getString("animal_nome");
                    BigDecimal peso = rs.getBigDecimal("peso");
                    BigDecimal altura = rs.getBigDecimal("altura");
                    String cor = rs.getString("cor");
                    char animalSexo = rs.getString("animal_sexo").charAt(0);
                    LocalDate animalDataNascimento = rs.getDate("animal_dataNascimento").toLocalDate();
                    boolean adotado = rs.getBoolean("adotado");
                    String especie = rs.getString("especie");

                    Animal animal = null;

                    if (especie.equalsIgnoreCase("Cachorro")) {
                        animal = new Cachorro(animalId, animalNome, peso, altura, cor, animalSexo, animalDataNascimento, adotado, especie);
                    } else if (especie.equalsIgnoreCase("Gato")) {
                        animal = new Gato(animalId, animalNome, peso, altura, cor, animalSexo, animalDataNascimento, adotado, especie);
                    } else {
                        // Fallback (lança exceção se for uma espécie desconhecida)
                        throw new Exception("Espécie de animal desconhecida no banco de dados para a adoção: " + especie);
                    }

                    // 4. CONSTRUIR O OBJETO ADOCAO FINAL
                    adocao = new Adocao(adocaoId, adotante, animal, dataAdocao);

                } else {
                    System.out.println("Adoção com ID " + id + " não encontrada.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar adoção no banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao buscar adoção.", e);
        }

        return adocao;
    }

    @Override
    public List<Adotante> listaTodosAdotantes() throws Exception {
        List<Adotante> adotantes = new ArrayList<>();
        String sql = "SELECT * FROM adotantes";

        try (Connection conn = getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Mapeamento idêntico ao que você faz em buscarAdotantePorId
                Adotante adotante = new Adotante(
                        rs.getInt("adotante_id"),
                        rs.getString("nome"),
                        rs.getString("sexo").charAt(0),
                        rs.getDate("dataNascimento").toLocalDate()
                );
                adotantes.add(adotante);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os adotantes: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao listar adotantes.", e);
        }
        
        // Carregar animais adotados para cada adotante (após fechar a conexão)
        for (Adotante adotante : adotantes) {
            try {
                carregarAnimaisAdotados(adotante);
            } catch (SQLException e) {
                System.err.println("Erro ao carregar animais adotados para " + adotante.getNome() + ": " + e.getMessage());
                // Não lança exceção, apenas loga o erro
            }
        }

        return adotantes;
    }

    @Override
    public List<Animal> listaTodosAnimais() throws Exception {
        List<Animal> animais = new ArrayList<>();
        String sql = "SELECT * FROM animais";

        try (Connection conn = getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int animalId = rs.getInt("animal_id");
                String nome = rs.getString("nome");
                BigDecimal peso = rs.getBigDecimal("peso");
                BigDecimal altura = rs.getBigDecimal("altura");
                String cor = rs.getString("cor");
                char sexo = rs.getString("sexo").charAt(0);
                LocalDate dataNascimento = rs.getDate("dataNascimento").toLocalDate();
                boolean adotado = rs.getBoolean("adotado");
                String especie = rs.getString("especie"); // Campo crucial para a POO

                Animal animal = null;

                // Instancia a subclasse correta (Pattern Factory)
                if (especie.equalsIgnoreCase("Cachorro")) {
                    animal = new Cachorro(animalId, nome, peso, altura, cor, sexo, dataNascimento, adotado, especie);
                } else if (especie.equalsIgnoreCase("Gato")) {
                    animal = new Gato(animalId, nome, peso, altura, cor, sexo, dataNascimento, adotado, especie);
                }

                // VERIFICAÇÃO DE CONSISTÊNCIA: Ignora registros com espécie desconhecida
                if (animal != null) {
                    animais.add(animal);
                } else {
                    System.err.println("Alerta de inconsistência: Animal ID " + animalId + " ignorado. Espécie '" + especie + "' não reconhecida/mapeada.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os animais: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao listar animais.", e);
        }

        return animais;
    }

    @Override
    public List<Adocao> listaTodasAdocoes() throws Exception {
        List<Adocao> adocoes = new ArrayList<>();

        // Usamos JOIN para obter os detalhes do Adotante e do Animal na mesma consulta
        String sql = "SELECT " +
                "a.adocao_id, a.dataAdocao, " +
                "ad.adotante_id, ad.nome AS adotante_nome, ad.sexo AS adotante_sexo, ad.dataNascimento AS adotante_dataNascimento, " +
                "an.animal_id, an.nome AS animal_nome, an.peso, an.altura, an.cor, an.sexo AS animal_sexo, an.dataNascimento AS animal_dataNascimento, an.adotado, an.especie " +
                "FROM adocoes a " +
                "JOIN adotantes ad ON a.adotante_id = ad.adotante_id " +
                "JOIN animais an ON a.animal_id = an.animal_id";

        try (Connection conn = getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // 1. RECONSTRUIR ADOTANTE
                Adotante adotante = new Adotante(
                        rs.getInt("adotante_id"),
                        rs.getString("adotante_nome"),
                        rs.getString("adotante_sexo").charAt(0),
                        rs.getDate("adotante_dataNascimento").toLocalDate()
                );

                // 2. RECONSTRUIR ANIMAL (usando a lógica Factory para Cachorro/Gato)
                int animalId = rs.getInt("animal_id");
                String especie = rs.getString("especie");
                Animal animal;


                // Mapeamento dos 9 parâmetros do Animal/Cachorro/Gato
                String animalNome = rs.getString("animal_nome");
                BigDecimal peso = rs.getBigDecimal("peso");
                BigDecimal altura = rs.getBigDecimal("altura");
                String cor = rs.getString("cor");
                char sexo = rs.getString("animal_sexo").charAt(0);
                LocalDate dataNascimento = rs.getDate("animal_dataNascimento").toLocalDate();
                boolean adotado = rs.getBoolean("adotado");

                if (especie.equalsIgnoreCase("Cachorro")) {
                    animal = new Cachorro(animalId, animalNome, peso, altura, cor, sexo, dataNascimento, adotado, especie);
                } else if (especie.equalsIgnoreCase("Gato")) {
                    animal = new Gato(animalId, animalNome, peso, altura, cor, sexo, dataNascimento, adotado, especie);
                } else {
                    // Se a espécie for desconhecida, pulamos esta adoção para evitar instanciar Animal abstrato
                    System.err.println("Alerta: Adoção ignorada devido à espécie desconhecida do animal ID " + animalId);
                    continue;
                }

                // 3. RECONSTRUIR ADOÇÃO
                Adocao adocao = new Adocao(
                        rs.getInt("adocao_id"),
                        adotante,
                        animal,
                        rs.getDate("dataAdocao").toLocalDate()
                );

                adocoes.add(adocao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as adoções: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao listar adoções.", e);
        }

        return adocoes;
    }
}
