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
}
