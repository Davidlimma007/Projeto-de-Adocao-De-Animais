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
            // Valida se o animal está disponível
            if(animal.isAdotado()){
                throw new AnimalIndisponivelException("O animal " + animal.getNome() + " já foi adotado e não está disponível.");
            }

            // IMPORTANTE: Recarregar o adotante do banco para ter a contagem atualizada
            // Isso garante que a validação do limite seja feita com dados atualizados
            Adotante adotanteAtualizado = mySQLRepositorio.buscarAdotantePorId(adotante.getId());
            if(adotanteAtualizado == null){
                throw new Exception("Adotante não encontrado no banco de dados.");
            }

            // Valida limite de adoções do adotante (agora com dados atualizados do banco)
            if(adotanteAtualizado.atingiuLimite()){
                throw new LimiteAdocoesException("O adotante " + adotanteAtualizado.getNome() + " atingiu o limite de " 
                    + Adotante.getLimiteAdocoes() + " adoções.");
            }

            // Adiciona o animal à lista (para manter consistência)
            adotanteAtualizado.adcionarAnimal(animal);

            // Marca animal como adotado
            animal.setAdotado(true);

            // Cria registro de adoção
            novaAdocao = new Adocao(adotanteAtualizado, animal, LocalDate.now());

            // Persiste a adoção
            this.mySQLRepositorio.salvarAdocao(novaAdocao);
            
            // Atualiza o animal no repositório
            this.mySQLRepositorio.atualizarAnimal(animal);

            System.out.println("Adoção realizada com sucesso.");

        }catch (LimiteAdocoesException e) {
            System.out.println("Erro: " + e.getMessage());
        }catch (AnimalIndisponivelException e){
            System.out.println("Erro: " + e.getMessage());
        }catch (Exception e){
            System.out.println("Erro ao realizar adoção: " + e.getMessage());
            e.printStackTrace();
        }
        return novaAdocao;
    }
}
