import java.time.LocalDate;

public class Adocao {
    private int id;
    private Adotantes adotantes;
    private Animal animal;
    private LocalDate dataAdocao;

    public Adocao(int id, Adotantes adotantes, Animal animal, LocalDate dataAdocao) {
        this.id = id;
        this.adotantes = adotantes;
        this.animal = animal;
        this.dataAdocao = LocalDate.now(); //Data de registro da adoção
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

    public Adotantes getAdotantes() {
        return adotantes;
    }

    public void setAdotantes(Adotantes adotantes) {
        this.adotantes = adotantes;
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
