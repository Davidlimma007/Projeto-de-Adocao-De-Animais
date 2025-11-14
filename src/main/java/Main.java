import model.Adotante;
import model.Animal;
import model.Cachorro;
import model.Gato;
import model.Adocao;
import repository.MySQLRepositorio;
import java.time.LocalDate;
import java.util.Scanner;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        // 1. Instanciar o Repositório: Declaramos aqui para que seja acessível a todos os blocos (Salvar e Buscar).
        MySQLRepositorio repositorio = new MySQLRepositorio();

        // === BLOCO 1: CADASTRO E SALVAMENTO (usando Scanner) ===
        try (Scanner scanner = new Scanner(System.in)) {

            // Opcional: Teste de conexão (mantido do passo anterior)
            if (!repositorio.testarConexao()) {
                System.out.println("Não é possível continuar: Falha na conexão inicial com o banco.");
                // Decidimos não dar 'return' para que o teste de busca ainda tente rodar.
            }

            /*
            // 2. Coleta de Dados do Usuário
            System.out.println("\n--- Cadastro de Adotante ---");
            System.out.print("Digite o nome completo do Adotante: ");
            String nome = scanner.nextLine();
            System.out.print("Digite o sexo (M/F): ");
            char sexo = scanner.next().toUpperCase().charAt(0);

            // Data fixa para teste
            LocalDate dataNascimento = LocalDate.of(1985, 10, 26);
            System.out.println("Data de Nascimento: " + dataNascimento + " (Fixa para o teste)");

            // 3. Criar o Objeto (CORRETO: sem ID, ele será gerado pelo banco)
            Adotante novoAdotante = new Adotante(nome, sexo, dataNascimento);

            // 4. Salvar no Banco de Dados
            System.out.println("\nTentando salvar Adotante...");
            repositorio.salvarAdotante(novoAdotante);

            System.out.println("✅ Cadastro concluído e salvo no MySQL!");
            */
        } catch (Exception e) {
            System.err.println("\nErro durante o processo de salvar o Adotante: " + e.getMessage());
            // Continua, para que o teste de busca (BLOCO 2) ainda possa rodar.
        }

        // === BLOCO 2: TESTE DE BUSCA (SELECT) ===
        // O teste de busca PRECISA do seu próprio try-catch, pois chama um método que lança Exception.
        try {
            System.out.println("\n--- Teste de Busca por ID ---");
            int idParaBuscar = 1;

            System.out.println("Buscando adotante com ID: " + idParaBuscar + "...");

            Adotante adotanteEncontrado = repositorio.buscarAdotantePorId(idParaBuscar);

            if (adotanteEncontrado != null) {
                System.out.println("✅ Busca bem-sucedida! Detalhes do Adotante:");
                System.out.println("ID: " + adotanteEncontrado.getId());
                System.out.println("Nome: " + adotanteEncontrado.getNome());
                System.out.println("Sexo: " + adotanteEncontrado.getSexo());
                System.out.println("Nascimento: " + adotanteEncontrado.getDataNascimento());
            } else {
                System.out.println("Adotante não foi encontrado (retornou null).");
            }
        } catch (Exception e) {
            System.err.println("\nErro grave durante o teste de Busca: " + e.getMessage());
            e.printStackTrace();
        }

        // === BLOCO 3: TESTE DE ATUALIZAÇÃO (UPDATE) ===
        try {
            System.out.println("\n--- Teste de Atualização (UPDATE) ---");

            int idParaAtualizar = 1; // Vamos atualizar o primeiro adotante salvo

            // 1. Buscar o objeto que será modificado (usamos o que foi encontrado antes)
            Adotante adotanteParaAtualizar = repositorio.buscarAdotantePorId(idParaAtualizar);

            if (adotanteParaAtualizar != null) {

                // 2. Modificar os dados no objeto Java (Usando os Setters)
                String novoNome = "Carlos White";
                char novoSexo = 'M';

                adotanteParaAtualizar.setNome(novoNome);
                adotanteParaAtualizar.setSexo(novoSexo);

                System.out.println("Modificando adotante ID " + idParaAtualizar + " para " + novoNome + "...");

                // 3. Chamar o método de atualização do Repositório
                repositorio.atualizarAdotante(adotanteParaAtualizar);

                // ---------------------------------------------
                // 4. Verificação: Buscar novamente para confirmar
                System.out.println("\nVerificando a atualização...");
                Adotante adotanteVerificado = repositorio.buscarAdotantePorId(idParaAtualizar);

                if (adotanteVerificado != null) {
                    System.out.println("UPDATE confirmado!");
                    System.out.println("ID: " + adotanteVerificado.getId() + ", Novo Nome: " + adotanteVerificado.getNome());
                }

            } else {
                System.out.println("Não foi possível atualizar, adotante ID " + idParaAtualizar + " não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("\nErro durante o Teste de Atualização: " + e.getMessage());
            e.printStackTrace();
        }

        // === BLOCO 4: TESTE DE EXCLUSÃO (DELETE) ===
        try {
            System.out.println("\n--- Teste de Exclusão (DELETE) ---");

            int idParaExcluir = 1; // Vamos excluir o primeiro adotante (Carlos White)

            System.out.println("Tentando excluir adotante com ID: " + idParaExcluir + "...");

            // 1. Executa a exclusão (o método é void, não retorna nada)
            repositorio.excluirAdotante(idParaExcluir);

            // 2. Verificação: Busca o ID novamente para confirmar se ele sumiu
            System.out.println("\nVerificando se a exclusão foi bem-sucedida...");
            Adotante adotanteExcluido = repositorio.buscarAdotantePorId(idParaExcluir);

            if (adotanteExcluido == null) {
                System.out.println("✅ DELETE confirmado! O adotante com ID " + idParaExcluir + " não foi mais encontrado.");
            } else {
                System.out.println("Falha na exclusão. O adotante com ID " + idParaExcluir + " AINDA EXISTE.");
            }
        } catch (Exception e) {
            System.err.println("\nErro durante o Teste de Exclusão: " + e.getMessage());
            e.printStackTrace();
        }

        // === BLOCO 5: TESTE DE SALVAMENTO (INSERT) DO ANIMAL ===
        try{
            System.out.println("\n--- Cadastro de Animal ---");

            // 1. Criar o objeto Animal (Bolt)
            // 5kg, 0.40m, Nasc: 2023-01-15, Não adotado
            Animal novoAnimal = new Cachorro("Bolt", new BigDecimal("5.0"),  new BigDecimal("0.40"),
                    "Marrom",  'M',  LocalDate.of(2023, 1, 15),
                    false);// Status inicial: não adotado

            //2. Salvar no Repositório
            repositorio.salvarAnimal(novoAnimal);
            System.out.println("✅ Salvamento de Animal concluído.");
        }catch (Exception e){
            System.err.println("\nErro durante o processo de salvar o Animal: " + e.getMessage());
            e.printStackTrace();
        }

        // === BLOCO 6: TESTE DE BUSCA POR ID (SELECT) DO ANIMAL ===
        try {
            System.out.println("\n--- Teste de Busca de Animal por ID ---");
            int idParaBuscar = 1;

            System.out.println("Buscando animal com ID: " + idParaBuscar + "...");

            Animal animalEncontrado = repositorio.buscarAnimalPorId(idParaBuscar);

            if (animalEncontrado != null) {
                System.out.println("✅ Busca bem-sucedida! Detalhes do Animal:");
                System.out.println("ID: " + animalEncontrado.getId() + " - Nome: " + animalEncontrado.getNome() + " (" + animalEncontrado.getEspecie() + ")");
                System.out.println("Som: " + animalEncontrado.emitirSom()); // Testando o polimorfismo!
            } else {
                System.out.println("Animal não foi encontrado.");
            }
        } catch (Exception e) {
            System.err.println("\nErro durante o Teste de Busca de Animal: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            System.out.println("\n--- INICIANDO FLUXO COMPLETO DE ADOÇÃO ---");

            // 1. CADASTRO DO ADOTANTE (ID 1)
            System.out.println("\n1. Cadastrando Novo Adotante (ID 1)...");
            Adotante novoAdotante = new Adotante("Maria Teste", 'F', LocalDate.of(1985, 10, 20));
            // Definimos o ID para 1 apenas para uso interno no teste, se o banco gera ID, remova esta linha
            novoAdotante.setId(1);
            repositorio.salvarAdotante(novoAdotante);

            // 2. CADASTRO DO ANIMAL (ID 1)
            System.out.println("\n2. Cadastrando Novo Animal (Cachorro ID 1)...");
            // O status adotado deve ser 'false' inicialmente!
            Cachorro novoAnimal = new Cachorro("Rex", new BigDecimal("10.0"), new BigDecimal("0.45"),
                    "Marrom", 'M', LocalDate.of(2024, 1, 1), false, "Cachorro");
            // Definimos o ID para 1 apenas para uso interno no teste
            novoAnimal.setId(1);
            repositorio.salvarAnimal(novoAnimal);

            // 3. REGISTRAR ADOÇÃO (TRANSAÇÃO)
            System.out.println("\n3. Registrando a Adoção (INSERT + UPDATE Transacional)...");
            // É CRÍTICO que os IDs do Adotante e Animal existam no banco!
            Adocao novaAdocao = new Adocao(novoAdotante, novoAnimal, LocalDate.now());

            // A chamada a salvarAdocao deve fazer o INSERT na tabela 'adocoes' E o UPDATE na tabela 'animais'
            repositorio.salvarAdocao(novaAdocao);

            // 4. VERIFICAÇÃO FINAL
            System.out.println("\n4. Verificação Final do Status do Animal...");
            Animal animalVerificado = repositorio.buscarAnimalPorId(novoAnimal.getId());

            if (animalVerificado != null && animalVerificado.isAdotado()) {
                System.out.println("✅ SUCESSO! O fluxo completo funcionou. O animal '"
                        + animalVerificado.getNome() + "' está agora como adotado=true.");
            } else {
                System.out.println("❌ FALHA! A transação não atualizou o status 'adotado' do animal.");
            }

        } catch (Exception e) {
            System.err.println("\nERRO CRÍTICO NO FLUXO DE TESTE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

