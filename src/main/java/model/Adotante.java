package model;

import exceptions.LimiteAdocoesException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Adotante {
    private int id;
    private String nome;
    private char sexo;
    private LocalDate dataNascimento;
    private List<Animal> animaisAdotados = new ArrayList<>();

    private static final int LIMITE_ADOCOES = 3;

    //construtor com a inicialização da lista animais adotados
    public Adotante(String nome, char sexo, LocalDate dataNascimento) {
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
    }

    //construtor sobrecarregado
    public Adotante(int id, String nome, char sexo, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
    }

        public int getId(){ return id;}

        public void setId(int id){ this.id = id;}

        public String getNome() {
        return nome;
        }

        public void setNome(String nome){
        this.nome = nome;
        }

        public char getSexo() {
        return sexo;
        }

        public void setSexo(char sexo) {
            this.sexo = sexo;
        }

        public LocalDate getDataNascimento() {
        return dataNascimento;
        }

        public void setDataNascimento(LocalDate dataNascimento){
        this.dataNascimento = dataNascimento;
        }

        public List<Animal> getAnimaisAdotados() {
        return animaisAdotados;
        }

        public static int getLimiteAdocoes(){
        return LIMITE_ADOCOES;
        }

        public boolean atingiuLimite(){
        return this.animaisAdotados.size() >= LIMITE_ADOCOES;
        }

        public void adcionarAnimal(Animal animal) throws LimiteAdocoesException {
            if(atingiuLimite()){
                throw new LimiteAdocoesException("O atodante " + getNome() + " atingiu o limite de "
                                                  + LIMITE_ADOCOES + " adoções");
            }
            this.animaisAdotados.add(animal);
        }

    @Override
    public String toString() {
        return "Nome: " + getNome() + "| Data de nascimento: " + getDataNascimento() +" | Animais adotados: " + animaisAdotados.size();
    }

}