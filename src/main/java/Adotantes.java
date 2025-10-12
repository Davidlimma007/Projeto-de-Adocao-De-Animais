import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Adotantes {
    private int id;
    private String nome;
    private String sexo;
    private LocalDate dataNascimento;
    private List<Animal> animaisAdotados = new ArrayList<>();

    private static final int LIMITE_ADOCOES = 3;

    //construtor com a inicialização da lista animais adotados
    public Adotantes(int id,String nome, String sexo, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.animaisAdotados = new ArrayList<>();
    }

    // Sobrecarga: construtor sem id
    public Adotantes(String nome, String sexo, LocalDate dataNascimento) {
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.animaisAdotados = new ArrayList<>();
    }

    // Sobrecarga: construtor só com nome e sexo
    public Adotantes(String nome, String sexo) {
        this.nome = nome;
        this.sexo = sexo;
        this.animaisAdotados = new ArrayList<>();
    }

        public int getId () {
            return id;
        }

        public void setId ( int id){
            this.id = id;
        }

        public String getNome () {
            return nome;
        }

        public void setNome (String nome){
            this.nome = nome;
        }

        public String getSexo () {
            return sexo;
        }

        public void setSexo (String sexo){
            this.sexo = sexo;
        }

        public LocalDate getDataNascimento () {
            return dataNascimento;
        }

        public void setDataNascimento (LocalDate dataNascimento){
            this.dataNascimento = dataNascimento;
        }

        public List<Animal> getAnimaisAdotados() {
        return animaisAdotados;
        }
}