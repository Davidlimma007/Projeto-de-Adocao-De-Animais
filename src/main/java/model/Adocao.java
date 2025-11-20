package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Adocao {
    private int id;
    private Adotante adotante;
    private Animal animal;
    private LocalDate dataAdocao;

    public Adocao(int id, Adotante adotante, Animal animal, LocalDate dataAdocao) {
        this.id = id;
        this.adotante = adotante;
        this.animal = animal;
        this.dataAdocao = dataAdocao;
    }

    public Adocao(Adotante adotante, Animal animal, LocalDate dataAdocao){
        this.adotante = adotante;
        this.animal = animal;
        this.dataAdocao = dataAdocao;
    }

    public int getId(){return id;}

    public void setId(int id) { this.id = id;}

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("ID: %d | Adotante: %s | Animal: %s | Data: %s",
                id, adotante.getNome(), animal.getNome(), dataAdocao.format(formatter));
    }
}
