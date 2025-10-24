package model;

import java.time.LocalDate;

public class Gato extends Animal implements CuidadosEspeciais {

    public Gato(String nome, double peso, double altura, String cor,String sexo, LocalDate dataNascimento) {
        super(nome, peso, altura, cor, sexo, dataNascimento);
    }

    @Override
    public void emitirSom() {
        System.out.println("Miau Miau!");
    }

    @Override
    public void vacinar() {
        System.out.println("O Gato " + getNome() + " tomou Vacina");
    }

    @Override
    public void vermifugar() {
        System.out.println("O Gato " + getNome() + " foi vermifugado");
    }
}
