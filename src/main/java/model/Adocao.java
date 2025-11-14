package model;

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
        this.dataAdocao = LocalDate.now();
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
