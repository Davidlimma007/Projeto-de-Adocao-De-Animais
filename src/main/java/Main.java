import model.Adotante; // Para criar o objeto Adotante
import repository.MySQLRepositorio; // Para acessar o repositório
import java.time.LocalDate; // Para a data
import java.util.Scanner; // Para a interação com o usuário

public class Main {
    public static void main(String[] args) {

        // Colocamos tudo dentro de um try-catch por causa do 'throws Exception' no salvarAdotante
        try (Scanner scanner = new Scanner(System.in)) {

            // 1. Instanciar o Repositório
            MySQLRepositorio repositorio = new MySQLRepositorio();

            // Opcional: Teste de conexão (mantido do passo anterior, mas não é obrigatório rodar sempre)
            if (!repositorio.testarConexao()) {
                System.out.println("Não é possível continuar: Falha na conexão inicial com o banco.");
                return; // Encerra o programa se a conexão falhar
            }

            // -----------------------------------------------------------------
            // 2. Coleta de Dados do Usuário
            System.out.println("\n--- Cadastro de Adotante ---");

            System.out.print("Digite o nome completo do Adotante: ");
            String nome = scanner.nextLine();

            System.out.print("Digite o sexo (M/F): ");
            // next().charAt(0) pega o primeiro caractere da entrada
            char sexo = scanner.next().toUpperCase().charAt(0);

            // Para simplificar o teste, usaremos uma data de nascimento fixa
            LocalDate dataNascimento = LocalDate.of(1985, 10, 26);
            System.out.println("Data de Nascimento: " + dataNascimento + " (Fixa para o teste)");

            // 3. Criar o Objeto
            Adotante novoAdotante = new Adotante(nome, sexo, dataNascimento);

            // 4. Salvar no Banco de Dados
            System.out.println("\nTentando salvar Adotante...");
            repositorio.salvarAdotante(novoAdotante);

            System.out.println("✅ Cadastro concluído e salvo no MySQL!");

        } catch (Exception e) {
            System.err.println("\nErro grave durante a execução do Main: " + e.getMessage());
        }
    }
}
