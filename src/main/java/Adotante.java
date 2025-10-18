import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Adotante {
    private String nome;
    private String sexo;
    private LocalDate dataNascimento;
    private List<Animal> animaisAdotados = new ArrayList<>();

    private static final int LIMITE_ADOCOES = 3;

    //construtor com a inicialização da lista animais adotados
    public Adotante(String nome, String sexo, LocalDate dataNascimento) {
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.animaisAdotados = new ArrayList<>();
    }

    // Sobrecarga: construtor só com nome e sexo
    public Adotante(String nome, String sexo) {
        this.nome = nome;
        this.sexo = sexo;
        this.animaisAdotados = new ArrayList<>();
    }

        public String getNome() {
        return nome;
        }

        public void setNome(String nome){
        this.nome = nome;
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

        public void setDataNascimento(LocalDate dataNascimento){
        this.dataNascimento = dataNascimento;
        }

        public List<Animal> getAnimaisAdotados() {
        return animaisAdotados;
        }

        public static int getLimiteAdocoes(){
        return LIMITE_ADOCOES;
        }
}