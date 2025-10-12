import java.time.LocalDate;

public class Adocao {
    private int id;
    private Adotante adotante;
    private Animal animal;
    private LocalDate dataAdocao;

    public Adocao(int id, Adotante adotante, Animal animal, LocalDate dataAdocao) {
        this.id = id;
        this.adotante = adotante;
        this.animal = animal;
        this.dataAdocao = LocalDate.now(); //Data de registro da adoção
    }

    // Sobrecarga de Construtor (Para o Repositório - Carregamento de dados)
    public Adocao(int id, Adotante adotante, Animal animal, LocalDate dataAdocao) {
        this.id = id;
        this.adotante = adotante;
        this.animal = animal;
        this.dataAdocao = dataAdocao;
    }

    // na parte de (%s) é para as subclasses de Animal por isso coloca animal.getClass().getSimpleName()
    @Override
    public String toString() {
        return String.format("Adoção #%d: Animal '%s' (%s) foi adotado por '%s' em %s",
                id, animal.getNome(), animal.getClass().getSimpleName(),
                adotante.getNome(), dataAdocao.toString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public void setAdotante(Adotante adotante) {
        this.adotante = adotante;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public LocalDate getDataAdocao() {
        return dataAdocao;
    }

    public void setDataAdocao(LocalDate dataAdocao) {
        this.dataAdocao = dataAdocao;
    }


}
