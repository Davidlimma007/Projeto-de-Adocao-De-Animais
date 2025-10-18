import java.time.LocalDate;

public class Gato extends Animal implements CuidadosEspeciais{

    public Gato(String nome, double peso, double altura, String cor, LocalDate dataNascimento) {
        super(nome, peso, altura, cor, dataNascimento);
    }

    @Override
    public void emitirSom() {
        System.out.println("Miau Miau!");
    }

    @Override
    public void vacinar() {
        System.out.println("Animal tomou Vacina");
    }

    @Override
    public void vermifugar() {
        System.out.println("Animal tomou vermifugar");
    }
}
