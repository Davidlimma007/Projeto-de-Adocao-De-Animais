package repository;

import model.Adocao;
import model.Adotante;
import model.Animal;

public interface Repositorio {

    void salvarAdotante(Adotante adotante) throws Exception;

    void salvarAnimal(Animal animal) throws Exception;
    void atualizarStatusAnimal(Animal animal) throws Exception;

    void salvarAdocao(Adocao adocao) throws Exception;

    Adotante buscarAdotantePorId(int id) throws Exception;
    Animal buscarAnimalPorId(int id) throws Exception;
}
