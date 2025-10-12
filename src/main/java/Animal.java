import java.time.LocalDate;

public abstract class Animal {
    private int id;
    private String nome;
    private double peso;
    private double altura;
    private String cor;
    private LocalDate dataNascimento;
    private boolean adotado;

    public abstract void emitirSom();
}
