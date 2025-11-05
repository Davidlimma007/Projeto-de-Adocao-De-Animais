package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.Adocao;
import model.Adotante;
import model.Animal;


public class MySQLRepositorio implements Repositorio{

    private static final String URL = "jdbc:mysql://localhost:3306/db_adocao_novo";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public Connection getConnection() throws SQLException {

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
        try (Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

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

            System.out.println("Adotante " + adotante.getNome() + " salvo com sucesso!");

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
        try (Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        return adotante;
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

        try(Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)){
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

            System.out.println("Animal " + animal.getNome() + " Salvo com sucesso!");

        }catch (SQLException e){
            System.err.println("Erro ao salvar animal no banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falha ao salvar animal.", e);
        }
    }

    @Override
    public void atualizarAnimal(Animal animal) throws Exception {

    }

    @Override
    public Animal buscarAnimalPorId(int id) throws Exception {
        return null;
    }

    @Override
    public void excluirAnimal(int id) throws Exception{

    }

    @Override
    public void salvarAdocao(Adocao adocao) throws Exception {

    }
}
