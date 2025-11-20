import model.Animal;
import model.Cachorro;
import model.Gato;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Adotante;
import model.Adocao;
import repository.MySQLRepositorio;
import repository.Repositorio;
import service.ServicoAdocao;

import java.math.BigDecimal;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static Repositorio repo = new MySQLRepositorio();
    static ServicoAdocao servicoAdocao = new ServicoAdocao(new MySQLRepositorio());

    static void main(String[] args) throws Exception {
        System.out.println("Bem-vindo ao Sistema de Adoção de Animais!");
        exibirMenuPrincipal();
        scanner.close();
    }

    static void exibirMenuPrincipal() throws Exception {
        boolean executando = true;
        while (executando) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Gerenciar Animais");
            System.out.println("2. Gerenciar Adotantes");
            System.out.println("3. Realizar Adoção");
            System.out.println("4. Listar Adoções");
            System.out.println("0. Sair");
            System.out.println("Escolha uma opção: ");

            int opcao = lerInteiro();
            switch (opcao) {
                case 1:
                    gerenciarAnimais();
                    break;
                case 2:
                    gerenciarAdotantes();
                    break;
                case 3:
                    realizarAdocao();
                    break;
                case 4:
                    listarAdocoes();
                case 0:
                    executando = false;
                    break;
                default:
                    System.err.println("Opção inválida. Tente novamente.");
            }
            System.out.println("Obrigado por usar o sistema. Até logo!");
        }
    }

    // * GERENCIAR ANIMAIS
    static void gerenciarAnimais() throws Exception {
        System.out.println("\n--- Gerenciar Animais ---");
        System.out.println("1. Cadastrar Animal");
        System.out.println("2. Listar Todos os Animais");
        System.out.println("3. Atualizar Animal");
        System.out.println("4. Remover Animal");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1: cadastrarAnimal(); break;
            case 2: listarAnimais(); break;
            case 3: atualizarAnimal(); break;
            case 4: removerAnimal(); break;
            case 0: break;
            default: System.err.println("Opção inválida.");
        }
    }

    // TODO: CRUD DE ===ANIMAL===
    // ? CADASTRAR ANIMAL
    static void cadastrarAnimal() throws Exception {
        System.out.println("\n-- Cadastrar Animal --");
        System.out.println("Tipo (1-Cachorro, 2-Gato)");
        int tipo = lerInteiro();
        System.out.println("Nome: ");
        String nome = lerString();
        System.out.println("Peso: ");
        BigDecimal peso = lerBigDecimal();
        System.out.println("Altura: ");
        BigDecimal altura = lerBigDecimal();
        System.out.println("Cor: ");
        String cor = lerString();
        System.out.println("Sexo: ");
        char sexo = lerChar();
        System.out.println("Data de Nascimento: ");
        LocalDate dataNascimento = lerData();

        Animal animal;
        if (tipo == 1) {
            animal = new Cachorro(nome, peso, altura, cor, sexo, dataNascimento, false);
        } else if (tipo == 2) {
            animal = new Gato(nome, peso, altura, cor, sexo, dataNascimento, false);
        } else {
            System.err.println("Tipo inválido. Cadastro cancelado.");
            return;
        }

        repo.salvarAnimal(animal);
        System.out.println("Animal cadastrado com sucesso!");
    }

    // ? LISTAR ANIMAIS
    static void listarAnimais() throws Exception {
        System.out.println("\n-- Lista de Animais --");
        List<Animal> animais = repo.listaTodosAnimais();
        if (animais.isEmpty()) {
            System.out.println("Nenhum animal cadastrado.");
            return;
        }
        for (Animal animal : animais) {
            System.out.printf(animal.toString());
        }
    }

    // ? ATUALIZAR ANIMAL
    static void atualizarAnimal() throws Exception {
        System.out.print("\nDigite o ID do animal para atualizar: ");
        int id = lerInteiro();
        Animal animal = repo.buscarAnimalPorId(id);
        if(animal == null){
            System.err.println("Animal não encontrado.");
            return;
        }

        System.out.println("Novo Nome (Deixe em branco para manter: " + animal.getNome() + "): ");
        String nome = lerStringOpcional(animal.getNome());
        System.out.println();
        System.out.print("Novo Status (1-DISPONIVEL, 2-ADOTADO) (Deixe em branco para manter: " + animal.isAdotado() + "): ");
        String statusInput = lerString();

        animal.setNome(nome);
        if (statusInput.equals("1")) animal.setAdotado(false);
        if (statusInput.equals("2")) animal.setAdotado(true);

        repo.atualizarAnimal(animal);
        System.out.println("Animal atualizado com sucesso!");
    }

    // ? REMOVER ANIMAL
    static void removerAnimal() throws Exception {
        System.out.print("\nDigite o ID do animal para remover: ");
        int id = lerInteiro();
        Animal animal = repo.buscarAnimalPorId(id);
        if(animal == null){
            System.err.println("Animal não encontrado.");
            return;
        }
        repo.excluirAnimal(id);
        System.out.println("Animal " + animal.getNome() + " removido com sucesso.");
    }

    // * GERENCIAR ADOTANTES
    static void gerenciarAdotantes() throws Exception {
        System.out.println("\n--- Gerenciar Adotantes ---");
        System.out.println("1. Cadastrar Adotante");
        System.out.println("2. Atualizar Adotante");
        System.out.println("3. Listar Todos os Adotantes");
        System.out.println("4. Remover Adotante");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        int op = lerInteiro();
        switch (op){
            case 1: cadastrarAdotante(); break;
            case 2: atualizarAdotante(); break;
            case 3: listarAdotantes(); break;
            case 4: removerAdotante(); break;
            case 0: break;
            default: System.err.println("Opção inválida.");
        }
    }

    // TODO: CRUD DE ADOTANTE
    // ? CADASTRAR ADOTANTE
    static void cadastrarAdotante() throws Exception {
        System.out.print("Nome: ");
        String nome = lerString();
        System.out.print("Sexo: ");
        char sexo = lerChar();
        System.out.println("Data de Nascimento: ");
        LocalDate dataNascimento = lerData();

        Adotante adotante = new Adotante(nome, sexo, dataNascimento);
        repo.salvarAdotante(adotante);
        System.out.println("Adotante cadastrado com sucesso! ID: " + adotante.getId());
    }

    // ? ATUALIZAR ADOTANTE
    static void atualizarAdotante() throws Exception {
        System.out.print("\nDigite o ID do adotante para atualizar: ");
        int id = lerInteiro();
        Adotante adotante = repo.buscarAdotantePorId(id);
        if(adotante == null){
            System.err.println("Adotante não encontrado.");
            return;
        }

        System.out.println("Novo Nome (Deixe em branco para manter: " + adotante.getNome() + "): ");
        String nome = lerStringOpcional(adotante.getNome());

        adotante.setNome(nome);

        repo.atualizarAdotante(adotante);
        System.out.println("Adotante atualizado com sucesso!");
    }

    // ? LISTAR TODOS ADOTANTES
    static void listarAdotantes() throws Exception {
        System.out.println("\n-- Lista de Adotantes --");
        List<Adotante> adotantes = repo.listaTodosAdotantes();
        if(adotantes.isEmpty()){
            System.out.println("Nenhum adotante cadastrado.");
            return;
        }
        for(Adotante adotante : adotantes){
            System.out.println(adotante.toString());
        }
    }

    // ? REMOVER ADOTANTE
    static void removerAdotante() throws Exception{
        System.out.print("\nDigite o ID do adotante para remover: ");
        int id = lerInteiro();
        Adotante adotante = repo.buscarAdotantePorId(id);
        if(adotante == null){
            System.out.println("Adotante não encontrado.");
            return;
        }
        repo.excluirAdotante(id);
        System.out.println("Adotante " + adotante.getNome() + " removido com sucesso!");
    }

    // * REALIZAR ADOÇÃO
    static void realizarAdocao() throws Exception {
        System.out.println("\\n--- Realizar Adoção ---");
        System.out.print("Digite o ID do Adotante: ");
        int idAdotante = lerInteiro();
        System.out.println("Digite o ID do Animal: ");
        int idAnimal = lerInteiro();

        Adotante adotante = repo.buscarAdotantePorId(idAdotante);
        Animal animal = repo.buscarAnimalPorId(idAnimal);

        Adocao adocao = servicoAdocao.realizarAdoção(adotante, animal);

        if(adocao != null){
            System.out.println("Adoção realizada com sucesso!");
        } else {
            System.out.println("Não foi possível realizar a adoção.");
        }
    }

    // * LISTAR ADOÇÕES
    static void listarAdocoes() throws Exception {
        System.out.println("\n--- Listar Adoções ---");
        System.out.println("1. Listar todas");
        System.out.println("2. Filtrar por Adotante");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();
        List<Adocao> adocoes = new ArrayList<>();

        switch (opcao){
            case 1:
                adocoes = repo.listaTodasAdocoes();
                break;
            case 2:
                System.out.println("Digite o ID do adotante: ");
                int idAdotante = lerInteiro();
                repo.buscarAdocaoPorId(idAdotante);
                break;
            default:
                System.err.println("Opção inválida.");
                return;
        }

        if(adocoes.isEmpty()){
            System.out.println("Nenhum registro de adoção encontrado para este filtro.");
            return;
        }
        System.out.println("\\n-- Registros de Adoção --");
        for(Adocao adocao : adocoes){
            System.out.println(adocao.toString());
        }
    }


    // ! ===MÉTODOS UTILITÁRIOS===
    private static String lerString() {
        return scanner.nextLine();
    }

    private static char lerChar() {
        while (true) {
            String entrada = scanner.nextLine();

            if (entrada.length() == 1) {
                return entrada.charAt(0);
            }

            System.err.print("Entrada inválida. Digite apenas um caractere: ");
        }
    }

    private static String lerStringOpcional(String valorPadrao) {
        String input = scanner.nextLine();
        return input.isEmpty() ? valorPadrao : input;
    }

    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.print("Entrada inválida. Digite um número inteiro: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.print("Entrada inválida. Digite um número decimal: ");
            }
        }
    }

    private static BigDecimal lerBigDecimal() {
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.print("Entrada inválida. Digite um número decimal (ex: 10.50): ");
            }
        }
    }

    private static int lerInteiroOpcional(int valorPadrao) {
        String input = scanner.nextLine();
        if (input.isEmpty()) return valorPadrao;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida. Usando valor padrão: " + valorPadrao);
            return valorPadrao;
        }
    }

    private static long lerLong() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.print("Entrada inválida. Digite um número (ID): ");
            }
        }
    }

    private static LocalDate lerData() {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), dateFormatter);
            } catch (DateTimeParseException e) {
                System.err.print("Formato de data inválido. Use dd/MM/yyyy: ");
            }
        }
    }
}