package com.busreservas.model;

/**
 * Representa um assento físico em uma viagem de ônibus.
 *
 * <p>Cada assento possui um número identificador único dentro de sua viagem,
 * um estado de ocupação ({@code ocupado}) e uma referência ao {@link Passageiro}
 * que o reservou, quando aplicável.</p>
 *
 * <p>Os assentos são inicializados como disponíveis ({@code ocupado = false})
 * e sua reserva ou liberação é controlada pelos métodos {@link #reservar(Passageiro)}
 * e {@link #liberar()}.</p>
 *
 * @author Felipe
 * @version 1.0
 * @see Viagem
 * @see Passageiro
 */
public class Assento {

    /** Número identificador do assento dentro da viagem (começa em 1). */
    private int numero;

    /** Indica se o assento está ocupado ({@code true}) ou disponível ({@code false}). */
    private boolean ocupado;

    /** Passageiro que reservou este assento; {@code null} se disponível. */
    private Passageiro passageiro;

    /**
     * Cria um novo assento com o número especificado, inicialmente disponível.
     *
     * @param numero número identificador do assento; deve ser maior que zero
     * @throws IllegalArgumentException se {@code numero} for menor ou igual a zero
     */
    public Assento(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Número do assento deve ser positivo.");
        }
        this.numero = numero;
        this.ocupado = false;
        this.passageiro = null;
    }

    /**
     * Verifica se o assento está disponível para reserva.
     *
     * @return {@code true} se o assento não estiver ocupado
     */
    public boolean isDisponivel() {
        return !ocupado;
    }

    /**
     * Reserva o assento para o passageiro informado.
     *
     * <p>Define {@code ocupado = true} e armazena a referência ao passageiro.</p>
     *
     * @param passageiro passageiro que está realizando a reserva
     * @throws IllegalStateException se o assento já estiver ocupado
     */
    public void reservar(Passageiro passageiro) {
        if (ocupado) {
            throw new IllegalStateException("Assento " + numero + " já está ocupado.");
        }
        this.ocupado = true;
        this.passageiro = passageiro;
    }

    /**
     * Libera o assento, tornando-o disponível novamente.
     *
     * <p>Define {@code ocupado = false} e remove a referência ao passageiro.
     * Chamado automaticamente pelo método {@link Reserva#cancelar()}.</p>
     */
    public void liberar() {
        this.ocupado = false;
        this.passageiro = null;
    }

    /**
     * Retorna o número identificador do assento.
     * @return número do assento
     */
    public int getNumero() { return numero; }

    /**
     * Retorna se o assento está ocupado.
     * @return {@code true} se ocupado
     */
    public boolean isOcupado() { return ocupado; }

    /** @param ocupado novo estado de ocupação */
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }

    /**
     * Retorna o passageiro que reservou este assento.
     * @return passageiro associado, ou {@code null} se disponível
     */
    public Passageiro getPassageiro() { return passageiro; }

    /** @param passageiro passageiro a ser associado ao assento */
    public void setPassageiro(Passageiro passageiro) { this.passageiro = passageiro; }

    /**
     * Retorna representação textual do assento com número e status.
     * @return string no formato {@code [Assento 01 | DISPONIVEL]} ou {@code [Assento 01 | OCUPADO(Nome)]}
     */
    @Override
    public String toString() {
        String status = ocupado
                ? "OCUPADO(" + (passageiro != null ? passageiro.getNome() : "?") + ")"
                : "DISPONIVEL";
        return String.format("[Assento %02d | %s]", numero, status);
    }
}
