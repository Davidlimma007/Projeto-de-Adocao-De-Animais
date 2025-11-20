package service;

import exceptions.AnimalIndisponivelException;
import exceptions.LimiteAdocoesException;
import model.Adocao;
import model.Adotante;
import model.Animal;
import repository.MySQLRepositorio;

import java.time.LocalDate;

public class ServicoAdocao {
    private MySQLRepositorio mySQLRepositorio;

    public ServicoAdocao(MySQLRepositorio mySQLRepositorio) {
        this.mySQLRepositorio = mySQLRepositorio;
    }

    public Adocao realizarAdoção(Adotante adotante, Animal animal){
        Adocao novaAdocao = null;

        try{
            adotante.adcionarAnimal(animal);

            animal.setAdotado(true);

            novaAdocao = new Adocao(adotante, animal, LocalDate.now());

            this.mySQLRepositorio.salvarAdocao(novaAdocao);

            System.out.println("Adoção realizada com sucesso.");

        }catch (LimiteAdocoesException e) {
            System.out.println(e.getMessage());
        }catch (AnimalIndisponivelException e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return novaAdocao;
    }
}
