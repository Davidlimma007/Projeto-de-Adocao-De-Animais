package repository;

import model.Adocao;
import model.Adotante;
import model.Animal;

public interface Repositorio {

    void salvarAdotante(Adotante adotante) throws Exception;
    void atualizarAdotante(Adotante adotante) throws Exception;
    Adotante buscarAdotantePorId(int id) throws Exception;
    void excluirAdotante(int id) throws Exception;

    void salvarAnimal(Animal animal) throws Exception;
    void atualizarAnimal(Animal animal) throws Exception;
    Animal buscarAnimalPorId(int id) throws Exception;
    void excluirAnimal(int id) throws Exception;

    void salvarAdocao(Adocao adocao) throws Exception;

}
