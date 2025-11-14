package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cachorro extends Animal implements CuidadosEspeciais {

    public Cachorro(String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento,
                    boolean adotado) {
        super(nome, peso, altura, cor, sexo, dataNascimento, adotado, "Cachorro");
    }

    public Cachorro(int id, String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento, boolean adotado, String especie) {
        super(id, nome, peso, altura, cor, sexo, dataNascimento, especie);
    }

    public Cachorro(String nome, BigDecimal peso, BigDecimal altura, String cor,
                    char sexo, LocalDate dataNascimento, boolean adotado, String especie) {

        super(nome, peso, altura, cor, sexo, dataNascimento, adotado, especie);
    }

    @Override
    public String emitirSom() { return "Au Au!"; }

    @Override
    public void vacinar() {
        System.out.println("O Cachorro " + getNome() + " tomou Vacina");
    }

    @Override
    public void vermifugar() {
        System.out.println("O Cachorro " + getNome() + " foi vermifugado");
    }
}
