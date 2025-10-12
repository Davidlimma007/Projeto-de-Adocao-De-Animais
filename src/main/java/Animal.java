import java.time.LocalDate;

public abstract class Animal {
    private int id;
    private String nome;
    private double peso;
    private double altura;
    private String cor;
    private LocalDate dataNascimento;
    private boolean adotado;

    public Animal(int id, String nome, double peso, double altura, String cor, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.peso = peso;
        this.altura = altura;
        this.cor = cor;
        this.dataNascimento = dataNascimento;
        this.adotado = false;
    }

    public abstract void emitirSom();

    @Override
    public String toString() {
        return nome + (adotado ? " (Adotado)" : " (Disponível)");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
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
}
