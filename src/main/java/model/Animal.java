package model;

import exceptions.AnimalIndisponivelException;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Animal {
    private int animal_id;
    private String nome;
    private BigDecimal peso;
    private BigDecimal altura;
    private String cor;
    private String sexo;
    private LocalDate dataNascimento;
    private boolean adotado;

    public Animal(int animal_id, String nome, BigDecimal peso, BigDecimal altura, String cor, String sexo, LocalDate dataNascimento) {
        this.animal_id = animal_id;
        this.nome = nome;
        this.peso = peso;
        this.altura = altura;
        this.cor = cor;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.adotado = false;
    }

    public Animal(String nome, BigDecimal peso, BigDecimal altura, String cor, String sexo, LocalDate dataNascimento) {
        this.nome = nome;
        this.peso = peso;
        this.altura = altura;
        this.cor = cor;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.adotado = false;
    }

    public abstract void emitirSom();

    @Override
    public String toString() {
        return nome + (adotado ? " (Adotado)" : " (Disponível)");
    }

    public int getAnimal_id() { return animal_id; }

    public void setAnimal_id(int animal_id) { this.animal_id = animal_id; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isAdotado() {
        return adotado;
    }

    public void setAdotado(boolean adotado) {
        this.adotado = adotado;
    }

    public void serAdotado(){
        if(isAdotado()){
            throw new AnimalIndisponivelException("O animal já foi adotado");
        }
        this.setAdotado(true);
    }
}
