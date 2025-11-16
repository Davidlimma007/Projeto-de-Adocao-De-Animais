package repository;

import exceptions.LimiteAdocoesException;
import model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TxtRepositorio implements Repositorio {

    // --- Configuração dos Arquivos ---
    private static final String DIR_PATH = "data";
    private static final String ADOTANTES_FILE = DIR_PATH + File.separator + "adotantes.txt";
    private static final String ANIMAIS_FILE = DIR_PATH + File.separator + "animais.txt";
    private static final String ADOCOES_FILE = DIR_PATH + File.separator + "adocoes.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public TxtRepositorio() {
        try {
            // Cria o diretório 'data' se ele não existir
            Files.createDirectories(Paths.get(DIR_PATH));
            // Cria os arquivos se eles não existirem
            ensureFileExists(ADOTANTES_FILE);
            ensureFileExists(ANIMAIS_FILE);
            ensureFileExists(ADOCOES_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar o repositório e criar arquivos/diretórios.", e);
        }
    }

    private void ensureFileExists(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    // --- Métodos de Leitura e Escrita de Linhas ---

    private List<String> readAllLines(String filePath) throws IOException {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + filePath + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private void writeAllLines(String filePath, List<String> lines) throws IOException {
        Files.write(Paths.get(filePath), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // --- Mapeamentos de Objeto para String ---

    private String toLine(Adotante adotante) {
        // ID;NOME;SEXO;DATA_NASCIMENTO
        return String.format("%d;%s;%c;%s",
                adotante.getId(),
                adotante.getNome(),
                adotante.getSexo(),
                adotante.getDataNascimento().format(DATE_FORMATTER));
    }

    private String toLine(Animal animal) {
        // ID;ESPECIE;NOME;PESO;ALTURA;COR;SEXO;DATA_NASCIMENTO;ADOTADO
        return String.format("%d;%s;%s;%s;%s;%c;%s;%b",
                animal.getId(),
                animal.getEspecie(),
                animal.getNome(),
                animal.getPeso().toPlainString(),
                animal.getAltura().toPlainString(),
                animal.getSexo(),
                animal.getDataNascimento().format(DATE_FORMATTER),
                animal.isAdotado());
    }

    private String toLine(Adocao adocao) {
        // ID;ID_ADOTANTE;ID_ANIMAL;DATA_ADOCAO
        return String.format("%d;%d;%d;%s",
                adocao.getId(),
                adocao.getAdotante().getId(),
                adocao.getAnimal().getId(),
                adocao.getDataAdocao().format(DATE_FORMATTER));
    }

    // --- Mapeamentos de String para Objeto (Carregamento) ---

    private Adotante fromAdotanteLine(String line) {
        String[] parts = line.split(";");
        int id = Integer.parseInt(parts[0]);
        String nome = parts[1];
        char sexo = parts[2].charAt(0);
        LocalDate dataNascimento = LocalDate.parse(parts[3], DATE_FORMATTER);

        return new Adotante(id, nome, sexo, dataNascimento);
    }

    private Animal fromAnimalLine(String line) {
        String[] parts = line.split(";");
        int id = Integer.parseInt(parts[0]);
        String especie = parts[1];
        String nome = parts[2];
        BigDecimal peso = new BigDecimal(parts[3]);
        BigDecimal altura = new BigDecimal(parts[4]);
        char sexo = parts[5].charAt(0);
        LocalDate dataNascimento = LocalDate.parse(parts[6], DATE_FORMATTER);
        boolean adotado = Boolean.parseBoolean(parts[7]);

        if ("Cachorro".equals(especie)) {
            return new Cachorro(id, nome, peso, altura, "Não Informado", sexo, dataNascimento, adotado, especie);
        } else if ("Gato".equals(especie)) {
            return new Gato(id, nome, peso, altura, "Não Informado", sexo, dataNascimento, adotado, especie);
        }
        return new Cachorro(id, nome, peso, altura, "Não Informado", sexo, dataNascimento, adotado, especie); // Default ou Exceção
    }

    private Adocao fromAdocaoLine(String line) throws Exception {
        String[] parts = line.split(";");
        int id = Integer.parseInt(parts[0]);
        int idAdotante = Integer.parseInt(parts[1]);
        int idAnimal = Integer.parseInt(parts[2]);
        LocalDate dataAdocao = LocalDate.parse(parts[3], DATE_FORMATTER);

        // Busca o adotante e o animal no repositório
        Adotante adotante = buscarAdotantePorId(idAdotante);
        Animal animal = buscarAnimalPorId(idAnimal);

        // Adiciona o animal à lista de adotados do adotante (reconstrói o relacionamento)
        if (!adotante.getAnimaisAdotados().contains(animal)) {
            adotante.getAnimaisAdotados().add(animal);
        }

        return new Adocao(id, adotante, animal, dataAdocao);
    }

    // --- Lógica de Repositório (Baseada em Arquivos) ---

    private int getNextId(String filePath) throws IOException {
        List<String> lines = readAllLines(filePath);
        if (lines.isEmpty()) {
            return 1;
        }
        int maxId = lines.stream()
                .map(line -> line.split(";")[0])
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return maxId + 1;
    }

    // ---------------------- ADOTANTE ----------------------

    @Override
    public void salvarAdotante(Adotante adotante) throws Exception {
        List<String> lines = readAllLines(ADOTANTES_FILE);
        adotante.setId(getNextId(ADOTANTES_FILE));
        lines.add(toLine(adotante));
        writeAllLines(ADOTANTES_FILE, lines);
        System.out.println("Adotante salvo e persistido: " + adotante.getNome() + " (ID: " + adotante.getId() + ")");
    }

    @Override
    public List<Adotante> listaTodosAdotantes() throws Exception {
        return readAllLines(ADOTANTES_FILE).stream()
                .map(this::fromAdotanteLine)
                .collect(Collectors.toList());
    }

    @Override
    public Adotante buscarAdotantePorId(int id) throws Exception {
        return listaTodosAdotantes().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new Exception("Adotante com ID " + id + " não encontrado."));
    }

    @Override
    public void atualizarAdotante(Adotante adotante) throws Exception {
        List<Adotante> allAdotantes = listaTodosAdotantes();
        boolean found = false;

        for (int i = 0; i < allAdotantes.size(); i++) {
            if (allAdotantes.get(i).getId() == adotante.getId()) {
                allAdotantes.set(i, adotante); // Substitui pelo objeto atualizado
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Adotante com ID " + adotante.getId() + " não encontrado para atualização.");
        }

        List<String> updatedLines = allAdotantes.stream().map(this::toLine).collect(Collectors.toList());
        writeAllLines(ADOTANTES_FILE, updatedLines);
        System.out.println("Adotante atualizado e persistido: " + adotante.getNome() + " (ID: " + adotante.getId() + ")");
    }

    @Override
    public void excluirAdotante(int id) throws Exception {
        List<Adotante> allAdotantes = listaTodosAdotantes();
        long initialSize = allAdotantes.size();

        List<Adotante> updatedAdotantes = allAdotantes.stream()
                .filter(a -> a.getId() != id)
                .collect(Collectors.toList());

        if (updatedAdotantes.size() == initialSize) {
            throw new Exception("Adotante com ID " + id + " não encontrado para exclusão.");
        }

        // 1. Atualiza arquivo de adotantes
        List<String> updatedLines = updatedAdotantes.stream().map(this::toLine).collect(Collectors.toList());
        writeAllLines(ADOTANTES_FILE, updatedLines);

        // 2. Remove as adoções associadas (por ID do Adotante)
        List<Adocao> allAdocoes = listaTodasAdocoes();
        List<Adocao> updatedAdocoes = allAdocoes.stream()
                .filter(a -> a.getAdotante().getId() != id)
                .collect(Collectors.toList());

        List<String> updatedAdocaoLines = updatedAdocoes.stream().map(this::toLine).collect(Collectors.toList());
        writeAllLines(ADOCOES_FILE, updatedAdocaoLines);

        System.out.println("Adotante (ID: " + id + ") excluído e persistido. Adoções relacionadas removidas.");
    }

    // ---------------------- ANIMAL ----------------------

    @Override
    public void salvarAnimal(Animal animal) throws Exception {
        List<String> lines = readAllLines(ANIMAIS_FILE);
        animal.setId(getNextId(ANIMAIS_FILE));
        lines.add(toLine(animal));
        writeAllLines(ANIMAIS_FILE, lines);
        System.out.println("Animal salvo e persistido: " + animal.getNome() + " (ID: " + animal.getId() + ")");
    }

    @Override
    public Animal buscarAnimalPorId(int id) throws Exception {
        // Carrega todos, pois precisamos do estado de todos os animais para reconstruir as adoções
        return listaTodosAnimais().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new Exception("Animal com ID " + id + " não encontrado."));
    }

    @Override
    public void atualizarAnimal(Animal animal) throws Exception {
        List<Animal> allAnimals = listaTodosAnimais();
        boolean found = false;

        for (int i = 0; i < allAnimals.size(); i++) {
            if (allAnimals.get(i).getId() == animal.getId()) {
                allAnimals.set(i, animal); // Substitui pelo objeto atualizado
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Animal com ID " + animal.getId() + " não encontrado para atualização.");
        }

        List<String> updatedLines = allAnimals.stream().map(this::toLine).collect(Collectors.toList());
        writeAllLines(ANIMAIS_FILE, updatedLines);
        System.out.println("Animal atualizado e persistido: " + animal.getNome() + " (ID: " + animal.getId() + ")");

        // **Nota importante:** Atualizar um animal, especialmente o status 'adotado',
        // exige que todos os adotantes que possam tê-lo em sua lista interna sejam atualizados
        // para manter a coerência em memória. No entanto, em um repositório baseado em arquivo,
        // isso geralmente é gerenciado pela camada de Serviço, ou a lista interna do Adotante
        // só é reconstruída na busca (como feito no fromAdocaoLine).
    }

    @Override
    public void excluirAnimal(int id) throws Exception {
        List<Animal> allAnimals = listaTodosAnimais();
        long initialSize = allAnimals.size();

        List<Animal> updatedAnimals = allAnimals.stream()
                .filter(a -> a.getId() != id)
                .collect(Collectors.toList());

        if (updatedAnimals.size() == initialSize) {
            throw new Exception("Animal com ID " + id + " não encontrado para exclusão.");
        }

        // 1. Atualiza arquivo de animais
        List<String> updatedLines = updatedAnimals.stream().map(this::toLine).collect(Collectors.toList());
        writeAllLines(ANIMAIS_FILE, updatedLines);

        // 2. Remove as adoções associadas (por ID do Animal)
        List<Adocao> allAdocoes = listaTodasAdocoes();
        List<Adocao> updatedAdocoes = allAdocoes.stream()
                .filter(a -> a.getAnimal().getId() != id)
                .collect(Collectors.toList());

        List<String> updatedAdocaoLines = updatedAdocoes.stream().map(this::toLine).collect(Collectors.toList());
        writeAllLines(ADOCOES_FILE, updatedAdocaoLines);

        System.out.println("Animal (ID: " + id + ") excluído e persistido. Adoções relacionadas removidas.");
    }

    @Override
    public List<Animal> listaTodosAnimais() throws Exception {
        return readAllLines(ANIMAIS_FILE).stream()
                .map(this::fromAnimalLine)
                .collect(Collectors.toList());
    }

    // ---------------------- ADOÇÃO ----------------------

    @Override
    public void salvarAdocao(Adocao adocao) throws Exception {
        // Obtenha as instâncias mais recentes (com seus relacionamentos)
        Adotante adotante = buscarAdotantePorId(adocao.getAdotante().getId());
        Animal animal = buscarAnimalPorId(adocao.getAnimal().getId());

        // 1. Validações
        if (animal.isAdotado()) {
            throw new Exception("Animal " + animal.getNome() + " já foi adotado.");
        }
        if (adotante.atingiuLimite()) {
            throw new LimiteAdocoesException("Adotante " + adotante.getNome() + " atingiu o limite de adoções.");
        }

        // 2. Atualiza objetos e salva no repositório de origem (Animal e Adotante)
        animal.setAdotado(true);
        adotante.adcionarAnimal(animal); // Adiciona na lista interna do Adotante

        atualizarAnimal(animal); // Persiste mudança de status do animal
        atualizarAdotante(adotante); // Persiste o Adotante (o que é crucial para futuras cargas)

        // 3. Salva a Adoção
        List<String> lines = readAllLines(ADOCOES_FILE);
        adocao.setId(getNextId(ADOCOES_FILE));
        adocao.setAdotante(adotante);
        adocao.setAnimal(animal);

        lines.add(toLine(adocao));
        writeAllLines(ADOCOES_FILE, lines);

        System.out.println("Adoção salva e persistida: ID " + adocao.getId() + ".");
    }

    @Override
    public Adocao buscarAdocaoPorId(int id) throws Exception {
        return listaTodasAdocoes().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new Exception("Adoção com ID " + id + " não encontrada."));
    }

    @Override
    public List<Adocao> listaTodasAdocoes() throws Exception {
        // Carrega Adotantes e Animais primeiro para que o 'fromAdocaoLine' possa fazer a busca
        // (Isso é uma simplificação. Um repositório real faria a carga lazy ou usaria um cache)

        List<Adotante> allAdotantes = listaTodosAdotantes(); // Garante que os adotantes estão em cache/memória
        List<Animal> allAnimals = listaTodosAnimais(); // Garante que os animais estão em cache/memória

        return readAllLines(ADOCOES_FILE).stream()
                .map(line -> {
                    try {
                        return fromAdocaoLine(line);
                    } catch (Exception e) {
                        System.err.println("Erro ao carregar linha de adoção: " + line + ". " + e.getMessage());
                        return null; // Ignora a linha com erro
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }
}
