package repository;

import model.Adocao;
import model.Adotante;
import model.Animal;

public interface Repositorio {

    void salvarAdotante(Adotante adotante);

    void salvarAnimal(Animal animal);
    void atualizarStatusAnimal(Animal animal);

    void salvarAdocao(Adocao adocao);

    Adotante buscarAdotantePorId(int id);
    Animal buscarAnimalPorId(int id);

}
