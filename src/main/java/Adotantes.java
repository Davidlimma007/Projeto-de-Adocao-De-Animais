import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Adotantes {
    private int id;
    private String nome;
    private String sexo;
    private LocalDate dataNascimento;
    private List<Animal> animaisAdotados = new ArrayList<>();

    private static final int LIMITE_ADOCOES = 3;

    //construtor com a inicialização da lista animais adotados
    public Adotantes(int id,String nome, String sexo, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.animaisAdotados = new ArrayList<>();
    }

    // Sobrecarga: construtor sem id
    public Adotantes(String nome, String sexo, LocalDate dataNascimento) {
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.animaisAdotados = new ArrayList<>();
    }

    // Sobrecarga: construtor só com nome e sexo
    public Adotantes(String nome, String sexo) {
        this.nome = nome;
        this.sexo = sexo;
        this.animaisAdotados = new ArrayList<>();
    }

    //metodo principal para adotar um animal
    public void adotarAnimal(Animal animal) throws LimiteAdocoesException, AnimalIndisponivelException {

        // Regra 1: limite de 3 adoções
        //.size() percorre a lista
        if (animaisAdotados.size() >= LIMITE_ADOCOES) {
            throw new LimiteAdocoesException("O adotante " + nome + " já atingiu o limite máximo de "
                    + LIMITE_ADOCOES + " animais adotados!");
        }

        // Regra 2: animal já foi adotado
        //pega na classe animal e vê se o boolean esta tru para lançar a exception
        if (animal.isAdotado()) {
            throw new AnimalIndisponivelException("O animal '" + animal.getNome() + "' já foi adotado por outra pessoa!");
        }

        //adota o animal com isso atualiza na lista o animal e na classe animal troca de false para true
        animaisAdotados.add(animal);
        animal.setAdotado(true);

        System.out.println(nome + " adotou o animal: " + animal.getNome());
    }

    // Sobrecarga: adotar animal pelo nome, buscando numa lista
    public void adotarAnimal(String nomeAnimal, List<Animal> animaisDisponiveis)
            throws LimiteAdocoesException, AnimalIndisponivelException {

        for (Animal animal : animaisDisponiveis) {
            if (animal.getNome().equalsIgnoreCase(nomeAnimal)) {
                this.adotarAnimal(animal);
                return;
            }
        }
        System.out.println("Animal com o nome '" + nomeAnimal + "' não encontrado!");
    }

        public int getId () {
            return id;
        }

        public void setId ( int id){
            this.id = id;
        }

        public String getNome () {
            return nome;
        }

        public void setNome (String nome){
            this.nome = nome;
        }

        public String getSexo () {
            return sexo;
        }

        public void setSexo (String sexo){
            this.sexo = sexo;
        }

        public LocalDate getDataNascimento () {
            return dataNascimento;
        }

        public void setDataNascimento (LocalDate dataNascimento){
            this.dataNascimento = dataNascimento;
        }

        public List<Animal> getAnimaisAdotados() {
        return animaisAdotados;
        }
}