package com.busreservas.estrutura;

/**
 * US06/T1 - Fila Estática Genérica (FIFO)
 * Estrutura de dados principal do projeto
 * Responsável: Rafael
 *
 * Funciona com qualquer tipo (T) usando generics do Java.
 * Capacidade fixa definida na criação (array estático).
 */
public class FilaEstatica<T> {

    private Object[] elementos;
    private int inicio;
    private int fim;
    private int tamanho;
    private int capacidade;

    public FilaEstatica(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade da fila deve ser maior que zero.");
        }
        this.capacidade = capacidade;
        this.elementos = new Object[capacidade];
        this.inicio = 0;
        this.fim = 0;
        this.tamanho = 0;
    }

    // US06/T2 - Inserir elemento no fim da fila
    public boolean enqueue(T elemento) {
        if (isFull()) {
            return false; // Fila cheia
        }
        elementos[fim] = elemento;
        fim = (fim + 1) % capacidade; // Fila circular
        tamanho++;
        return true;
    }

    // US06/T4 - Remover elemento do início da fila (FIFO)
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T elemento = (T) elementos[inicio];
        elementos[inicio] = null; // Liberar referência
        inicio = (inicio + 1) % capacidade;
        tamanho--;
        return elemento;
    }

    // Consultar o primeiro elemento sem remover
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) elementos[inicio];
    }

    // US06/T3 - Verificar posição na fila (ordem de chegada)
    public int getPosicao(T elemento) {
        for (int i = 0; i < tamanho; i++) {
            int idx = (inicio + i) % capacidade;
            if (elementos[idx] != null && elementos[idx].equals(elemento)) {
                return i + 1; // Posição 1-based
            }
        }
        return -1; // Não encontrado
    }

    // Remover elemento específico da fila (para cancelamentos)
    @SuppressWarnings("unchecked")
    public boolean remover(T elemento) {
        int posicao = -1;
        for (int i = 0; i < tamanho; i++) {
            int idx = (inicio + i) % capacidade;
            if (elementos[idx] != null && elementos[idx].equals(elemento)) {
                posicao = i;
                break;
            }
        }

        if (posicao == -1) return false;

        // Reorganizar fila após remoção
        for (int i = posicao; i < tamanho - 1; i++) {
            int idxAtual = (inicio + i) % capacidade;
            int idxProximo = (inicio + i + 1) % capacidade;
            elementos[idxAtual] = elementos[idxProximo];
        }

        fim = (fim - 1 + capacidade) % capacidade;
        elementos[fim] = null;
        tamanho--;
        return true;
    }

    // Retorna todos os elementos como array para iteração
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] resultado = new Object[tamanho];
        for (int i = 0; i < tamanho; i++) {
            resultado[i] = elementos[(inicio + i) % capacidade];
        }
        return (T[]) resultado;
    }

    public boolean isEmpty() { return tamanho == 0; }
    public boolean isFull() { return tamanho == capacidade; }
    public int getTamanho() { return tamanho; }
    public int getCapacidade() { return capacidade; }

    @Override
    public String toString() {
        if (isEmpty()) return "[Fila vazia]";
        StringBuilder sb = new StringBuilder("Fila [");
        for (int i = 0; i < tamanho; i++) {
            int idx = (inicio + i) % capacidade;
            sb.append(elementos[idx]);
            if (i < tamanho - 1) sb.append(" -> ");
        }
        sb.append("]");
        return sb.toString();
    }
}
