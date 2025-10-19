import javax.xml.transform.Source;

public class ServicoAdocao {
    private Repositorio repositorio;

    public ServicoAdocao(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Adocao realizarAdoção(Adotante adotante, Animal animal){
        Adocao novaAdocao = null;

        try{
            adotante.adcionarAnimal(animal);

            animal.setAdotado(true);

            novaAdocao = new Adocao(adotante, animal);

            //repositorio.salvarAdocao(novaAdocao);

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
