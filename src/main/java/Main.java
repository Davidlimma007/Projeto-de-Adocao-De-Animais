
public class Main {
    public static void main(String[] args) {
        repository.MySQLRepositorio repositorio = new repository.MySQLRepositorio();
        System.out.println("Iniciando conexão com o banco de dados.....");

        if(repositorio.testarConexao()){
            System.out.println("Sucesso na conexão");
        }else{
            System.out.println("Falha na conexão");
        }

    }

}
