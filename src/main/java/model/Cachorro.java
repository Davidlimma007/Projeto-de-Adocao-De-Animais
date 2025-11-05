package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cachorro extends Animal implements CuidadosEspeciais {

    public Cachorro(String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento,
                    boolean adotado) {
        super(nome, peso, altura, cor, sexo, dataNascimento, adotado, "Cachorro");
    }

    @Override
    public void emitirSom() {
        System.out.println("Au Au!");
    }

    @Override
    public void vacinar() {
        System.out.println("O Cachorro " + getNome() + " tomou Vacina");
    }

    @Override
    public void vermifugar() {
        System.out.println("O Cachorro " + getNome() + " foi vermifugado");
    }
}
