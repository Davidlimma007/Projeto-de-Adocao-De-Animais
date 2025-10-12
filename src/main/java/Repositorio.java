import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Repositorio {

    // Listas em memória para o CRUD
    private List<Animal> animais = new ArrayList<>();
    private List<Adotantes> adotantes = new ArrayList<>();
    private List<Adocao> adocoes = new ArrayList<>();

    // Geradores de ID (garante ID único e sequencial para novos registros)
    private int nextAnimalId = 1;
    private int nextAdotanteId = 1;
    private int nextAdocaoId = 1;

    private static final String ARQUIVO_ANIMAIS = "animais.txt";
    private static final String ARQUIVO_ADOTANTES = "adotantes.txt";
    private static final String ARQUIVO_ADOCOES = "adocoes.txt";
    private static final String DELIMITADOR = "|";

    public Repositorio(){
        carregarDados();
    }

    private void carregarDados() {
        System.out.println("Carregando dados do repositório...");
        carregarAnimais();
        carregarAdotantes();
        carregarAdocoes();
        System.out.println("Dados carregados. " + animais.size() + " animais, " + adotantes.size()
                            + " adotantes, " + adocoes.size() + " adoções.");
    }

    // Método principal para salvar todos os dados
    public void salvarDados() {
        System.out.println("Salvando dados no repositório...");
        salvarAnimais();
        salvarAdotantes();
        salvarAdocoes();
        System.out.println("Dados salvos com sucesso.");
    }

    private void salvarAnimais() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ANIMAIS))) {
            for (Animal animal : animais) {
                // Formato: id|nome|peso|altura|cor|dataNascimento|adotado|tipo
                String tipo = animal instanceof Cachorro ? "CACHORRO" : "GATO";
                writer.println(
                        animal.getId() + DELIMITADOR +
                                animal.getNome() + DELIMITADOR +
                                animal.getPeso() + DELIMITADOR +
                                animal.getAltura() + DELIMITADOR +
                                animal.getCor() + DELIMITADOR +
                                animal.getDataNascimento().toString() + DELIMITADOR +
                                animal.isAdotado() + DELIMITADOR +
                                tipo
                );
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar animais: " + e.getMessage());
        }
    }

    private void carregarAnimais() {
        File file = new File(ARQUIVO_ANIMAIS);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split("\\" + DELIMITADOR); // Usa o \\ para escapar o |

                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                double peso = Double.parseDouble(dados[2]);
                double altura = Double.parseDouble(dados[3]);
                String cor = dados[4];
                LocalDate dataNascimento = LocalDate.parse(dados[5]);
                boolean adotado = Boolean.parseBoolean(dados[6]);
                String tipo = dados[7];

                Animal animal;
                if (tipo.equals("CACHORRO")) {
                    animal = new Cachorro(id, nome, peso, altura, cor, dataNascimento);
                } else { // GATO
                    animal = new Gato(id, nome, peso, altura, cor, dataNascimento);
                }

                // Força o status 'adotado' (pode ser necessário no caso de carregamento)
                animal.setAdotado(adotado);

                animais.add(animal);
                nextAnimalId = id + 1; // Atualiza o próximo ID
            }
        } catch (FileNotFoundException e) {
            // Ignora se o arquivo não existir na primeira execução
        } catch (Exception e) {
            System.err.println("Erro ao carregar animais: " + e.getMessage());
        }
    }

    private void salvarAdotantes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ADOTANTES))) {
            for (Adotantes adotante : adotantes) {
                // Formato: id|nome|sexo|dataNascimento
                writer.println(
                        adotante.getId() + DELIMITADOR +
                                adotante.getNome() + DELIMITADOR +
                                adotante.getSexo() + DELIMITADOR +
                                adotante.getDataNascimento().toString()
                );
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar adotantes: " + e.getMessage());
        }
    }

    private void carregarAdotantes() {
        File file = new File(ARQUIVO_ADOTANTES);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split("\\" + DELIMITADOR);

                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                String sexo = dados[2];
                LocalDate dataNascimento = LocalDate.parse(dados[3]);

                Adotantes adotante = new Adotantes(id, nome, sexo, dataNascimento);
                adotantes.add(adotante);
                nextAdotanteId = id + 1;
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            System.err.println("Erro ao carregar adotantes: " + e.getMessage());
        }
    }

    private void salvarAdocoes() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ADOCOES))) {
            for (Adocao adocao : adocoes) {
                // Formato: id|idAdotante|idAnimal|dataAdocao
                writer.println(
                        adocao.getId() + DELIMITADOR +
                                adocao.getAdotantes().getId() + DELIMITADOR +
                                adocao.getAnimal().getId() + DELIMITADOR +
                                adocao.getDataAdocao().toString()
                );
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar adoções: " + e.getMessage());
        }
    }

    private void carregarAdocoes() {
        File file = new File(ARQUIVO_ADOCOES);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split("\\" + DELIMITADOR);

                int id = Integer.parseInt(dados[0]);
                int idAdotante = Integer.parseInt(dados[1]);
                int idAnimal = Integer.parseInt(dados[2]);
                LocalDate dataAdocao = LocalDate.parse(dados[3]);

                // É crucial que Adotantes e Animais já tenham sido carregados!
                Adotantes adotante = buscarAdotantePorId(idAdotante);
                Animal animal = buscarAnimalPorId(idAnimal);

                if (adotante != null && animal != null) {
                    Adocao adocao = new Adocao(id, adotante, animal, dataAdocao); // Sobrecarga no construtor de Adocao (ver item 2)
                    adocoes.add(adocao);

                    // Adiciona o animal à lista do adotante (para manter a contagem de limite)
                    adotante.getAnimaisAdotados().add(animal);

                    nextAdocaoId = id + 1;
                } else {
                    System.err.println("Aviso: Adoção " + id + " ignorada - Adotante ou Animal não encontrado.");
                }
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            System.err.println("Erro ao carregar adoções: " + e.getMessage());
        }
    }

    // Cadastrar
    public void adicionarAnimal(Animal animal) {
        animal.setId(nextAnimalId++);
        animais.add(animal);
        salvarAnimais(); // Salva a alteração
    }

    // Listar
    public List<Animal> listarAnimais() {
        return animais;
    }

    // Buscar (necessário para a Adoção)
    public Animal buscarAnimalPorId(int id) {
        for (Animal animal : animais) {
            if (animal.getId() == id) {
                return animal;
            }
        }
        return null; // Não encontrado
    }

    // Atualizar (Necessário para o CRUD F.1)
    public boolean atualizarAnimal(Animal animalAtualizado) {
        for (int i = 0; i < animais.size(); i++) {
            if (animais.get(i).getId() == animalAtualizado.getId()) {
                animais.set(i, animalAtualizado);
                salvarAnimais();
                return true;
            }
        }
        return false;
    }

    // Remover (Necessário para o CRUD F.1)
    public boolean removerAnimal(int id) {
        // Regra de negócio: animal adotado não pode ser removido
        Animal animal = buscarAnimalPorId(id);
        if (animal != null && animal.isAdotado()) {
            System.err.println("Erro: Não é possível remover um animal que já foi adotado.");
            return false;
        }

        boolean removido = animais.removeIf(a -> a.getId() == id);
        if (removido) {
            salvarAnimais();
        }
        return removido;
    }

    // Cadastrar
    public void adicionarAdotante(Adotantes adotante) {
        adotante.setId(nextAdotanteId++);
        adotantes.add(adotante);
        salvarAdotantes(); // Salva a alteração
    }

    // Listar
    public List<Adotantes> listarAdotantes() {
        return adotantes;
    }

    // Buscar (necessário para a Adoção)
    public Adotantes buscarAdotantePorId(int id) {
        for (Adotantes adotante : adotantes) {
            if (adotante.getId() == id) {
                return adotante;
            }
        }
        return null; // Não encontrado
    }

    // Atualizar
    public boolean atualizarAdotante(Adotantes adotanteAtualizado) {
        for (int i = 0; i < adotantes.size(); i++) {
            if (adotantes.get(i).getId() == adotanteAtualizado.getId()) {
                adotantes.set(i, adotanteAtualizado);
                salvarAdotantes();
                return true;
            }
        }
        return false;
    }

    // Remover
    public boolean removerAdotante(int id) {
        // Regra de negócio: adotante com animais não pode ser removido
        Adotantes adotante = buscarAdotantePorId(id);
        if (adotante != null && !adotante.getAnimaisAdotados().isEmpty()) {
            System.err.println("Erro: Não é possível remover um adotante com animais adotados.");
            return false;
        }

        boolean removido = adotantes.removeIf(a -> a.getId() == id);
        if (removido) {
            salvarAdotantes();
        }
        return removido;
    }

    // Cadastrar (Chamado pelo ServicoAdocao)
    public void adicionarAdocao(Adocao adocao) {
        adocao.setId(nextAdocaoId++);
        adocoes.add(adocao);
        salvarAdocoes(); // Salva a alteração
    }

    // Listar
    public List<Adocao> listarAdocoes() {
        return adocoes;
    }

    // Listagem com filtro simples (Requisito F.4)
    public List<Adocao> listarAdocoesPorAdotante(int idAdotante) {
        List<Adocao> filtradas = new ArrayList<>();
        for (Adocao adocao : adocoes) {
            if (adocao.getAdotantes().getId() == idAdotante) {
                filtradas.add(adocao);
            }
        }
        return filtradas;
    }

    // Listagem com filtro simples (Requisito F.4)
    public List<Adocao> listarAdocoesPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Adocao> filtradas = new ArrayList<>();
        for (Adocao adocao : adocoes) {
            LocalDate data = adocao.getDataAdocao();
            // Verifica se a data está entre o início e o fim (inclusivo)
            if (!data.isBefore(inicio) && !data.isAfter(fim)) {
                filtradas.add(adocao);
            }
        }
        return filtradas;
    }
}
