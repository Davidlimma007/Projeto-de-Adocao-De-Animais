import java.time.LocalDate;

public class Adocao {
    private int id;
    private Adotante adotante;
    private Animal animal;
    private LocalDate dataAdocao;

    public Adocao(int id, Adotante adotante, Animal animal) {
        this.id = id;
        this.adotante = adotante;
        this.animal = animal;
        this.dataAdocao = LocalDate.now();
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

    @Override
    public String toString() {
        return id + " | " + adotante.getId() + " | " + animal.getId() + " | " + dataAdocao.toString();
    }
}
