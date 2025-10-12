import java.time.LocalDate;

public class ServicoAdocao {
    private Repositorio repositorio;

    public ServicoAdocao(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Adocao realizarAdocao (Adotante adotante, Animal animal)
                    throws LimiteAdocoesException, AnimalIndisponivelException {

        //validação

        if (adotante.getAnimaisAdotados().size() >= 3) {
            throw new LimiteAdocoesException("O adotante " + adotante.getNome() + " Já atingiu o limite de 3 adoções.");
        }

        if (animal.isAdotado()) {
            throw new AnimalIndisponivelException("O animal '" + animal.getNome() + "' Já foi adotado!");
        }

        //se avaliação foi aprovada
        //marca animal como adotado
        animal.setAdotado(true);

        // Adiciona à lista interna do Adotante (para contagem)
        adotante.getAnimaisAdotados().add(animal);

        LocalDate dataAdocao;
        Adocao novaAdocao = new Adocao(0, adotante, animal, dataAdocao);
        repositorio.adicionarAdocao(novaAdocao);

        repositorio.salvarDados();

        return novaAdocao;
    }
}
