import model.Animal;
import model.Cachorro;
import model.Gato;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema de Adoção de Animais!");
        exibirMenuPrincipal();
        scanner.close();
    }

    static void exibirMenuPrincipal(){
        boolean executando = true;
        while (executando){
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Gerenciar Animais");
            System.out.println("2. Gerenciar Adotantes");
            System.out.println("3. Realizar Adoção");
            System.out.println("4. Listar Adoções");
            System.out.println("0. Sair");
            System.out.println("Escolha uma opção: ");

            int opcao = lerInteiro();
            switch (opcao){
                case 1:
                    gerenciarAnimais();
                    break;
                case 2:
                    // gerenciarAdotantes();
                    break;
                case 3:
                    // realizarAdocao();
                    break;
                case 4:
                    // listarAdocoes();
                case 0:
                    executando = false;
                    break;
                default:
                    System.err.println("Opção inválida. Tente novamente.");
            }
            System.out.println("Obrigado por usar o sistema. Até logo!");
        }
    }

    static void gerenciarAnimais(){
        System.out.println("\n--- Gerenciar Animais ---");
        System.out.println("1. Cadastrar Animal");
        System.out.println("2. Listar Todos os Animais");
        System.out.println("3. Atualizar Animal");
        System.out.println("4. Remover Animal");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();
        switch (opcao){
            case 1: cadastrarAnimal(); break;
            case 2: listarAnimal(); break;
            case 3: atualizarAnimal(); break;
            case 4: removerAnimal(); break;
            default: System.err.println("Opção inválida.");
        }
    }

    static void cadastrarAnimal() {
        System.out.println("\n-- Cadastrar Animal --");
        System.out.println("Tipo (1-Cachorro, 2-Gato)");
        int tipo = lerInteiro();
        System.out.println("Nome: ");
        String nome = lerString();
        System.out.println("Peso: ");
        double peso = lerDouble();
        System.out.println("Altura: ");
        double altura = lerDouble();
        System.out.println("Cor: ");
        String cor = lerString();
        System.out.println("Sexo: ");
        String sexo = lerString();
        System.out.println("Data de Nascimento: ");
        LocalDate dataNascimento = lerData();

        Animal animal;
        if(tipo == 1){
            animal = new Cachorro(nome, peso, altura, cor, sexo, dataNascimento);
        } else if (tipo == 2) {
            animal = new Gato(nome, peso, altura, cor, sexo, dataNascimento);
        } else {
            System.err.println("Tipo inválido. Cadastro cancelado.");
            return;
        }

        // animalRepository.save(animal);
        System.out.println("Animal cadastrado com sucesso!");
    }

    static void listarAnimais(){
        System.out.println("\n-- Lista de Animais --");
        List<Animal> animais = animalRepository.listarTodos();
        if(animais.isEmpty()){
            System.out.println("Nenhum animal cadastrado.");
            return;
        }
        for(Animal animal : animais){
            // System.out.printf();
        }

    }
}
