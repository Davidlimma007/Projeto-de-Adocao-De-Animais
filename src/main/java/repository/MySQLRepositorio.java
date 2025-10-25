package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.Adocao;
import model.Adotante;
import model.Animal;


public class MySQLRepositorio implements Repositorio{

    private static final String URL = "jdbc:mysql://localhost:3306/db_adocao";
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

    }

    @Override
    public void salvarAnimal(Animal animal) throws Exception {

    }

    @Override
    public void atualizarStatusAnimal(Animal animal) throws Exception {

    }

    @Override
    public void salvarAdocao(Adocao adocao) throws Exception {

    }

    @Override
    public Adotante buscarAdotantePorId(int id) throws Exception {
        return null;
    }

    @Override
    public Animal buscarAnimalPorId(int id) throws Exception {
        return null;
    }
}
