package core;

import java.util.*;

public class CronogramaGenetico{
    public List<Tarefa> melhoresTarefas = new ArrayList<>();
    public List<List<Tarefa>> geracoes = new ArrayList<>();
    private List<Tarefa> tarefas;
    private List<Tarefa> cronogramaAtual;
    public static double TAXA_MUTACAO = 0.3;
    public static double TAXA_CRUZAMENTO = 0.8;

    public CronogramaGenetico(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
        this.cronogramaAtual = new ArrayList<>(tarefas);
        Collections.shuffle(this.cronogramaAtual); // Organização aleatoria inicial
    }

    public List<Tarefa> getCronogramaAtual() {
        if (cronogramaAtual.isEmpty()) {
            return Collections.emptyList();
        }

        // Assuming the first element is the schedule
        return cronogramaAtual.subList(1, 5);
    }

    private double fitness(List<Tarefa> cronograma) {
        int totalTempo = 0;
        for (Tarefa tarefa : cronograma) {
            int tempoDaTarefa = tarefa.getTempo();
            totalTempo += tempoDaTarefa;
        }

        int totalDificuldade = 0;

        for (Tarefa tarefa : cronograma) {
            int dificuldadeDaTarefa = tarefa.getDificuldade();
            totalDificuldade += dificuldadeDaTarefa;
        }

        return totalTempo + totalDificuldade;
    }

    private List<Tarefa> mutacao(List<Tarefa> cronograma) {

        // A tarefa após receber a mutação = tentativa
        List<Tarefa> tentativa = new ArrayList<>(cronograma);
        int cont = 0;

        for (Tarefa tarefa : tentativa) {
            double R = Math.random();

            if (R < TAXA_CRUZAMENTO) {
                // CR condition: Update the task based on the formula
                int novoTempo = (int) (tarefa.getTempo() + TAXA_MUTACAO * (tarefa.getDificuldade() * Math.random() - tarefa.getDificuldade()));
                int novaDificuldade = tarefa.getDificuldade();

                // Update the task in the tentative schedule
                // Atualizando a tarefa na tarefa tentativa
                tentativa.set(cont, new Tarefa(tarefa.getNome(), novoTempo, novaDificuldade));
            }

            cont++;
        }

        return tentativa;
    }

    private List<Tarefa> crossover(List<Tarefa> pai, List<Tarefa> mae) {
        // Clone the parents to create the child
        List<Tarefa> filho = new ArrayList<>(pai);

        // Choose a random crossover point
        int crossoverPoint = new Random().nextInt(pai.size());

        // Exchange tasks between parents at the crossover point to create the child
        for (int i = crossoverPoint; i < pai.size(); i++) {
            filho.set(i, mae.get(i));
        }

        return filho;
    }

    private List<Tarefa> selecaoInicial(List<List<Tarefa>> populacao) {
        // Organizando a população baseado no fitness (ordem decrescente)
        populacao.sort(Comparator.<List<Tarefa>>comparingDouble(sublista -> fitness(sublista)).reversed());

        List<Tarefa> selectedTasks = new ArrayList<>();

        // Seleciona os 4 com maior fitness
        for (int i = 0; i < Math.min(4, populacao.size()); i++) {
            List<Tarefa> schedule = new ArrayList<>(populacao.get(i));
            // Organiza as tarefas no cronograma com base no fitness (ordem decrescente)
            schedule.sort(Comparator.<Tarefa>comparingDouble(tarefa -> fitness(Collections.singletonList(tarefa))).reversed());
            selectedTasks.addAll(schedule);
        }

        return selectedTasks;
    }

    private static List<Tarefa> gerarTarefasAleatorias(int numeroTarefas) {
        List<Tarefa> tarefas = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numeroTarefas; i++) {
            // Examplo: Gera valores aleatorios para tempo (1-120), dificuldade (1-10)
            int tempo = random.nextInt(120) + 1;
            int dificuldade = random.nextInt(10) + 1;

            // Cria uma tarefa e a adiciona na lista
            Tarefa tarefa = new Tarefa("Tarefa" + (i + 1), tempo, dificuldade);
            tarefas.add(tarefa);
        }

        return tarefas;
    }

    // Loop principal de evolução
    public void evoluir(int geracoes) {
        for (int geracao = 0; geracao < geracoes; geracao++) {
            // Seleção aleatória de 4 tarefas
            List<Tarefa> tarefasSelecionadas = selecaoInicialInicial(tarefas);

            // Imprimir as 4 tarefas selecionadas organizadas pelo fitness
            imprimirMelhoresTarefas(tarefasSelecionadas);

            // Lista para a nova população
            List<List<Tarefa>> novaPopulacao = new ArrayList<>();

            for (int i = 0; i < tarefas.size(); i++) {
                // Seleção
                List<Tarefa> pai = selecaoInicial(Arrays.asList(cronogramaAtual, mutacao(cronogramaAtual)));
                List<Tarefa> mae = selecaoInicial(Arrays.asList(cronogramaAtual, mutacao(cronogramaAtual)));

                // Crossover
                List<Tarefa> filhoCrossover = crossover(pai, mae);

                // Adiciona o filho novo à população
                novaPopulacao.add(filhoCrossover);
            }

            // Atualiza o cronograma atual
            cronogramaAtual = selecaoInicial(novaPopulacao);
        }
    }

    // Função para seleção inicial de 4 tarefas aleatórias
    private List<Tarefa> selecaoInicialInicial(List<Tarefa> tarefas) {
        List<Tarefa> tarefasSelecionadas = new ArrayList<>(tarefas);
        Collections.shuffle(tarefasSelecionadas);
        return tarefasSelecionadas.subList(0, 4);
    }

    // Função para imprimir as 4 tarefas selecionadas organizadas pelo fitness
    private void imprimirMelhoresTarefas(List<Tarefa> tarefasSelecionadas) {
        // Organiza as tarefas selecionadas pelo fitness em ordem decrescente
        tarefasSelecionadas.sort(Comparator.<Tarefa>comparingDouble(tarefa -> fitness(Collections.singletonList(tarefa))).reversed());
        this.melhoresTarefas = tarefasSelecionadas;
        
        this.geracoes.add(tarefasSelecionadas);
        // Imprime as 4 tarefas selecionadas
        System.out.println("Melhores Tarefas na Geração " + "geracao: " + tarefasSelecionadas.subList(0, 4));
    }


    public void run() {        
        this.evoluir(5);
    }
    
}
