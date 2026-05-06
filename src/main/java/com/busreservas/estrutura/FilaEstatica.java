package com.busreservas.estrutura;

/**
 * Implementação de Fila Estática Genérica (FIFO - First In, First Out).
 *
 * <p>Estrutura de dados central do sistema BusReservas, utilizada para
 * gerenciar a lista de espera de passageiros em viagens lotadas.</p>
 *
 * <p>A fila é implementada com array de capacidade fixa definida no momento
 * da criação, usando índices circulares para {@code inicio} e {@code fim},
 * o que permite o reaproveitamento de posições liberadas por {@link #dequeue()}
 * sem necessidade de deslocamento de elementos.</p>
 *
 * <p><b>Complexidade das operações:</b></p>
 * <ul>
 *   <li>{@link #enqueue(Object)} — O(1)</li>
 *   <li>{@link #dequeue()} — O(1)</li>
 *   <li>{@link #peek()} — O(1)</li>
 *   <li>{@link #getPosicao(Object)} — O(n)</li>
 *   <li>{@link #remover(Object)} — O(n)</li>
 * </ul>
 *
 * @param <T> tipo dos elementos armazenados na fila
 * @author Rafael
 * @version 1.0
 * @see com.busreservas.model.ListaEspera
 */
public class FilaEstatica<T> {

    /** Array interno que armazena os elementos da fila. */
    private Object[] elementos;

    /** Índice do primeiro elemento (frente da fila). */
    private int inicio;

    /** Índice onde o próximo elemento será inserido (fim da fila). */
    private int fim;

    /** Quantidade atual de elementos na fila. */
    private int tamanho;

    /** Capacidade máxima da fila, definida no construtor. */
    private int capacidade;

    /**
     * Cria uma nova fila estática com a capacidade especificada.
     *
     * @param capacidade número máximo de elementos que a fila pode armazenar
     * @throws IllegalArgumentException se {@code capacidade} for menor ou igual a zero
     */
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

    /**
     * Insere um elemento no fim da fila (enfileirar).
     *
     * <p>O índice {@code fim} avança circularmente: {@code fim = (fim + 1) % capacidade}.</p>
     *
     * @param elemento elemento a ser inserido; não pode ser {@code null}
     * @return {@code true} se o elemento foi inserido com sucesso;
     *         {@code false} se a fila estiver cheia
     */
    public boolean enqueue(T elemento) {
        if (isFull()) {
            return false;
        }
        elementos[fim] = elemento;
        fim = (fim + 1) % capacidade;
        tamanho++;
        return true;
    }

    /**
     * Remove e retorna o elemento do início da fila (desenfileirar — FIFO).
     *
     * <p>O índice {@code inicio} avança circularmente: {@code inicio = (inicio + 1) % capacidade}.
     * A referência ao elemento é anulada após a remoção para liberar memória.</p>
     *
     * @return o elemento removido do início da fila, ou {@code null} se a fila estiver vazia
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T elemento = (T) elementos[inicio];
        elementos[inicio] = null;
        inicio = (inicio + 1) % capacidade;
        tamanho--;
        return elemento;
    }

    /**
     * Retorna o elemento do início da fila sem removê-lo.
     *
     * @return o primeiro elemento da fila, ou {@code null} se a fila estiver vazia
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) elementos[inicio];
    }

    /**
     * Retorna a posição (1-based) de um elemento na fila.
     *
     * <p>A posição 1 indica que o elemento é o próximo a ser atendido.</p>
     *
     * @param elemento elemento a ser localizado
     * @return posição do elemento na fila (começando em 1),
     *         ou {@code -1} se não encontrado
     */
    public int getPosicao(T elemento) {
        for (int i = 0; i < tamanho; i++) {
            int idx = (inicio + i) % capacidade;
            if (elementos[idx] != null && elementos[idx].equals(elemento)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Remove um elemento específico da fila, independente de sua posição.
     *
     * <p>Utilizado principalmente para remover um passageiro da lista de espera
     * em caso de desistência antes da promoção automática.</p>
     *
     * <p>Após a remoção, os elementos seguintes são deslocados para preencher
     * o espaço vazio, mantendo a ordem FIFO.</p>
     *
     * @param elemento elemento a ser removido
     * @return {@code true} se o elemento foi encontrado e removido;
     *         {@code false} caso contrário
     */
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

        for (int i = posicao; i < tamanho - 1; i++) {
            int idxAtual  = (inicio + i) % capacidade;
            int idxProximo = (inicio + i + 1) % capacidade;
            elementos[idxAtual] = elementos[idxProximo];
        }
        fim = (fim - 1 + capacidade) % capacidade;
        elementos[fim] = null;
        tamanho--;
        return true;
    }

    /**
     * Retorna todos os elementos da fila como array, na ordem FIFO.
     *
     * @return array com os elementos da fila na ordem de chegada
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] resultado = new Object[tamanho];
        for (int i = 0; i < tamanho; i++) {
            resultado[i] = elementos[(inicio + i) % capacidade];
        }
        return (T[]) resultado;
    }

    /**
     * Verifica se a fila está vazia.
     *
     * @return {@code true} se não houver elementos na fila
     */
    public boolean isEmpty() { return tamanho == 0; }

    /**
     * Verifica se a fila está cheia.
     *
     * @return {@code true} se a quantidade de elementos atingiu a capacidade máxima
     */
    public boolean isFull() { return tamanho == capacidade; }

    /**
     * Retorna a quantidade atual de elementos na fila.
     *
     * @return número de elementos presentes
     */
    public int getTamanho() { return tamanho; }

    /**
     * Retorna a capacidade máxima da fila.
     *
     * @return capacidade definida no construtor
     */
    public int getCapacidade() { return capacidade; }

    /**
     * Retorna uma representação textual da fila, mostrando os elementos
     * na ordem de chegada (frente → fim).
     *
     * @return string no formato {@code Fila [elem1 -> elem2 -> ...]}
     *         ou {@code [Fila vazia]} se não houver elementos
     */
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
