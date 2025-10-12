public class ServicoAdocao {
    private Repositorio repositorio;

    public ServicoAdocao(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Adocao adotar(Adotante adotante, Animal animal)
            throws AnimalIndisponivelException, LimiteAdocoesException{

        //Regra 1: Limite de 3 adoções
        if(adotante.getAnimaisAdotados().size() >= adotante.getLimiteAdocoes()){
            throw new LimiteAdocoesException("o Adotante " + adotante.getNome() + "atingiu o limite de "
                                            + adotante.getLimiteAdocoes() + " adoções.");
        }

        //Regra 2: Animal disponivel
        if(animal.isAdotado()){
            throw new AnimalIndisponivelException("O animal '" + animal.getNome() + "' já foi adotado!");
        }

        //atualiza status do animal
        animal.setAdotado(true);
        //adiciona o animal a lista do adotante
        adotante.getAnimaisAdotados().add(animal);
        //cria o registro de adoção
        Adocao novaAdocao = new Adocao(0, adotante, animal);
        //salva a nova adoção
        repositorio.adicionarAdocao(novaAdocao);

        repositorio.atualizarAnimal(animal);
        repositorio.atualizarAdotante(adotante);

        System.out.println("SUCESSO: " + adotante.getNome() + " adotou " + animal.getNome());
        return novaAdocao;
    }
}
