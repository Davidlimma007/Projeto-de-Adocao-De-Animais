import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Repositorio {

    // criando a lista internas de animais disponiveis e pessoas cadastradas
    private List<Animal> animais = new ArrayList<>();
    private List<Adotante> adotantes = new ArrayList<>();
    private List<Adocao> adocoes = new ArrayList<>();

    private int nextAnimalId = 1;
    private int nextAdotanteId = 1;
    private int nextAdocaoId = 1;

    // arquivos onde os dados seram salvos
    private final String arquivoAnimais = "animais.txt";
    private final String arquivoAdotantes = "adotantes.txt";
    private final String arquivoAdocoes = "adocoes.txt";
    private final String DELIMITADOR = "|";

    // Construtor: Carrega todos os dados na inicialização
    public Repositorio(){
        carregarAdotantes(); // Carregar Adotantes primeiro
        carregarAnimais();   // Carregar Animais
        carregarAdocoes();   // Carregar Adoções (depende de Animais e Adotantes)
        ajustarNextIDs();
    }

    // --- Métodos de Suporte de ID ---

    private void ajustarNextIDs() {
        // Se houverem dados, o próximo ID será o maior ID + 1
        if (!animais.isEmpty()) {
            nextAnimalId = animais.stream().mapToInt(Animal::getId).max().orElse(0) + 1;
        }
        if (!adotantes.isEmpty()) {
            nextAdotanteId = adotantes.stream().mapToInt(Adotante::getId).max().orElse(0) + 1;
        }
        if (!adocoes.isEmpty()) {
            nextAdocaoId = adocoes.stream().mapToInt(Adocao::getId).max().orElse(0) + 1;
        }
    }

    // Métodos para obter o próximo ID e incrementá-lo
    public int getNextAnimalId() { return nextAnimalId++; }
    public int getNextAdotanteId() { return nextAdotanteId++; }
    public int getNextAdocaoId() { return nextAdocaoId++; }

    // ********************* Animal ************************ //

    // adicionar animal
    public void adicionarAnimal(Animal animal){
        if (animal.getId() == 0) { // Assume que 0 significa que ainda não tem ID
            animal.setId(getNextAnimalId());
        }
        animais.add(animal);
        SalvarAnimais();
    }

    // Atualiza o estado da lista de animais no arquivo
    public void atualizarAnimal(Animal animal) {
        SalvarAnimais();
    }

    // Corrigido: Movido return null para fora do for
    public Animal buscarAnimalPorNome(String nome){
        for (Animal a : animais){
            if(a.getNome().equalsIgnoreCase(nome)) {
                return a;
            }
        }
        return  null;
    }

    public Animal buscarAnimalPorId(int id){
        for (Animal a : animais){
            if(a.getId() == id) {
                return a;
            }
        }
        return  null;
    }

    public List<Animal> listarAnimais(){
        return new ArrayList<>(animais);
    }

    // Salvar Animais (RETIFICADO: Inclui o tipo do animal para persistência)
    private void SalvarAnimais() {
        try(PrintWriter pw = new PrintWriter(new FileWriter(arquivoAnimais))){
            for(Animal a : animais){
                // 1. Inclui o nome da classe concreta (ex: Cachorro)
                String tipoAnimal = a.getClass().getSimpleName();

                pw.println(tipoAnimal + DELIMITADOR + // NOVO CAMPO
                        a.getId() + DELIMITADOR +
                        a.getNome() + DELIMITADOR +
                        a.getPeso() + DELIMITADOR +
                        a.getAltura() + DELIMITADOR +
                        a.getCor() + DELIMITADOR +
                        a.getDataNascimento().toString() + DELIMITADOR +
                        a.isAdotado());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Carregar Animais (RETIFICADO: Lê o tipo e instancia a classe correta)
    private void carregarAnimais() {
        File file = new File(arquivoAnimais);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // O delimitador precisa de escape na expressão regular
                String[] dados = linha.split("\\"+DELIMITADOR);

                // Agora esperamos 8 campos: Tipo|ID|Nome|Peso|Altura|Cor|DataNasc|Adotado
                if (dados.length < 8) continue;

                String tipo = dados[0].trim(); // Lê o tipo
                int id = Integer.parseInt(dados[1].trim());
                String nome = dados[2].trim();
                double peso = Double.parseDouble(dados[3].trim());
                double altura = Double.parseDouble(dados[4].trim());
                String cor = dados[5].trim();
                LocalDate dataNascimento = LocalDate.parse(dados[6].trim());
                boolean adotado = Boolean.parseBoolean(dados[7].trim());

                Animal a = null;

                // Instanciação da classe correta
                if (tipo.equalsIgnoreCase("Cachorro")) {
                    a = new Cachorro(id, nome, peso, altura, cor, dataNascimento);
                } else if (tipo.equalsIgnoreCase("Gato")) {
                    a = new Gato(id, nome, peso, altura, cor, dataNascimento);
                }

                if (a != null) {
                    a.setAdotado(adotado);
                    animais.add(a);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter número ou data ao carregar animais: " + e.getMessage());
        }
    }


    // ********************* Adotante ************************ //

    public void adicionarAdotante(Adotante a) {
        a.setId(getNextAdotanteId());
        adotantes.add(a);
        salvarAdotantes();
    }

    public void atualizarAdotante(Adotante adotante) {
        salvarAdotantes();
    }

    public List<Adotante> listarAdotantes() {
        return new ArrayList<>(adotantes);
    }

    public Adotante buscarAdotantePorId(int id) {
        for (Adotante a : adotantes) {
            if (a.getId() == id) return a;
        }
        return null;
    }


    // salvar Adotantes
    private void salvarAdotantes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivoAdotantes))) {
            for (Adotante a : adotantes) {
                // Salvando todos os atributos do Adotante
                pw.println(a.getId() + DELIMITADOR +
                        a.getNome() + DELIMITADOR +
                        a.getSexo() + DELIMITADOR +
                        a.getDataNascimento().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // carregar Adotantes
    private void carregarAdotantes() {
        File file = new File(arquivoAdotantes);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split("\\"+DELIMITADOR);

                if (dados.length < 4) continue;

                int id = Integer.parseInt(dados[0].trim());
                String nome = dados[1].trim();
                String sexo = dados[2].trim();
                LocalDate dataNascimento = LocalDate.parse(dados[3].trim());

                Adotante a = new Adotante(id, nome, sexo, dataNascimento);
                adotantes.add(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ********************* Adoção ************************ //

    public void adicionarAdocao(Adocao adocao) {
        // Atribui ID se for uma nova adoção
        if (adocao.getId() == 0) {
            adocao.setId(getNextAdocaoId());
        }
        adocoes.add(adocao);
        salvarAdocoes();
    }

    public List<Adocao> listarAdocoes(){
        return new ArrayList<>(adocoes);
    }

    // Salva Adocoes
    private void salvarAdocoes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivoAdocoes))) {
            for (Adocao a : adocoes) {
                // Persistimos os IDs para que possamos re-linkar os objetos
                pw.println(a.getId() + DELIMITADOR +
                        a.getAdotante().getId() + DELIMITADOR +
                        a.getAnimal().getId() + DELIMITADOR +
                        a.getDataAdocao().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carrega Adocoes (Retificado para buscar objetos por ID)
    private void carregarAdocoes() {
        File file = new File(arquivoAdocoes);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split("\\"+DELIMITADOR);

                if (dados.length < 4) continue;

                int id = Integer.parseInt(dados[0].trim());
                int adotanteId = Integer.parseInt(dados[1].trim());
                int animalId = Integer.parseInt(dados[2].trim());
                LocalDate dataAdocao = LocalDate.parse(dados[3].trim());

                // Busca os objetos Adotante e Animal que já foram carregados
                Adotante adotante = buscarAdotantePorId(adotanteId);
                Animal animal = buscarAnimalPorId(animalId);

                if (adotante != null && animal != null) {
                    // Recria o objeto Adocao. Assumindo que a classe Adocao foi corrigida para aceitar a data
                    // Se não foi corrigida, a data de adoção será sempre a data atual!
                    Adocao a = new Adocao(id, adotante, animal);

                    adocoes.add(a);

                    // Recria o vínculo Adotante -> Animal (estado transiente que não é persistido no arquivo Adotantes/Animais)
                    if (!adotante.getAnimaisAdotados().contains(animal)){
                        adotante.getAnimaisAdotados().add(animal);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
