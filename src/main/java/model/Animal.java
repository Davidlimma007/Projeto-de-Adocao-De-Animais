package model;

import exceptions.AnimalIndisponivelException;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Animal {
    private int id;
    private String nome;
    private BigDecimal peso;
    private BigDecimal altura;
    private String cor;
    private char sexo;
    private LocalDate dataNascimento;
    private boolean adotado;
    private String especie;

    public Animal(int id, String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo,
                  LocalDate dataNascimento, boolean adotado, String especie) {
        this.id = id;
        this.nome = nome;
        this.peso = peso;
        this.altura = altura;
        this.cor = cor;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.adotado = adotado;
        this.especie = especie;
    }

    public Animal(String nome, BigDecimal peso, BigDecimal altura, String cor, char sexo, LocalDate dataNascimento,
                  boolean adotado, String especie) {
        this.nome = nome;
        this.peso = peso;
        this.altura = altura;
        this.cor = cor;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.adotado = adotado;
        this.especie = especie;
    }

    public abstract String emitirSom();

    @Override
    public String toString() {
        return nome + (adotado ? " (Adotado)" : " (Disponível)");
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
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

    public String getEspecie() { return especie; }

    public void setEspecie(String especie) { this.especie = especie; }

    public void serAdotado(){
        if(isAdotado()){
            throw new AnimalIndisponivelException("O animal já foi adotado");
        }
        this.setAdotado(true);
    }
}
