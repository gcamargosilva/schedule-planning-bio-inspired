package core;

import java.util.Objects;

public class Tarefa {

    private String nome;
    private int tempo; // Tempo em minutos
    private int dificuldade; // Facil: +3, Medio: +5, Dificil: +10

    public Tarefa(String nome, int tempo, int dificuldade) {
        this.nome = nome;
        this.tempo = tempo;
        this.dificuldade = dificuldade;
    }

    public String getNome() {
        return nome;
    }

    public int getTempo() {
        return tempo;
    }

    public int getDificuldade() {
        return dificuldade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarefa tarefa = (Tarefa) o;
        return tempo == tarefa.tempo &&
                dificuldade == tarefa.dificuldade &&
                Objects.equals(nome, tarefa.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, tempo, dificuldade);
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "nome='" + nome + '\'' +
                ", tempo=" + tempo +
                ", dificuldade=" + dificuldade +
                '}';
    }
}
