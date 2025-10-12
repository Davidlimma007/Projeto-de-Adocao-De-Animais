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

        animaisAdotados.add(animal);
        animal.setAdotado(true);

        System.out.println("✅ " + nome + " adotou o animal: " + animal.getNome());
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
}