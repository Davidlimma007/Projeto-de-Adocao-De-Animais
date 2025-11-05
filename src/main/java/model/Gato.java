package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Gato extends Animal implements CuidadosEspeciais {

    public Gato(String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento,
                    boolean adotado) {
        super(nome, peso, altura, cor, sexo, dataNascimento, adotado, "Gato");
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
