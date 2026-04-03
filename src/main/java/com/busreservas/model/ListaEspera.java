package com.busreservas.model;

import com.busreservas.estrutura.FilaEstatica;
import java.time.LocalDateTime;

/**
 * US06 - Lista de Espera para viagens lotadas
 * Usa FilaEstatica (FIFO) — mantém ordem de chegada
 * Responsável: Rafael
 */
public class ListaEspera {

    public static class EntradaEspera {
        private final Passageiro passageiro;
        private final Viagem viagem;
        private final LocalDateTime datasolicitacao;
        private final int quantidadeAssentos;

        public EntradaEspera(Passageiro passageiro, Viagem viagem, int quantidadeAssentos) {
            this.passageiro = passageiro;
            this.viagem = viagem;
            this.quantidadeAssentos = quantidadeAssentos;
            this.datasolicitacao = LocalDateTime.now();
        }

        public Passageiro getPassageiro() { return passageiro; }
        public Viagem getViagem() { return viagem; }
        public LocalDateTime getDataSolicitacao() { return datasolicitacao; }
        public int getQuantidadeAssentos() { return quantidadeAssentos; }

        @Override
        public String toString() {
            return String.format("[Espera] Passageiro: %s | Viagem: #%d | Qtd: %d | Solicitado: %s",
                    passageiro.getNome(), viagem.getId(), quantidadeAssentos,
                    datasolicitacao.toString().replace("T", " ").substring(0, 16));
        }
    }

    private final FilaEstatica<EntradaEspera> fila;
    private final int viagemId;

    public ListaEspera(int viagemId, int capacidadeFila) {
        this.viagemId = viagemId;
        this.fila = new FilaEstatica<>(capacidadeFila);
    }

    // US06/T2 - Inserir passageiro na fila
    public boolean inserir(Passageiro passageiro, Viagem viagem, int quantidadeAssentos) {
        EntradaEspera entrada = new EntradaEspera(passageiro, viagem, quantidadeAssentos);
        boolean inserido = fila.enqueue(entrada);
        if (!inserido) {
            System.out.println("Lista de espera cheia para a viagem #" + viagemId);
        }
        return inserido;
    }

    // US06/T4 - Remover próximo da fila (FIFO)
    public EntradaEspera proximoDaFila() {
        return fila.dequeue();
    }

    // US06/T5 - Remover passageiro específico (cancelamento)
    public boolean removerPassageiro(Passageiro passageiro) {
        Object[] entradas = fila.toArray();
        for (Object obj : entradas) {
            EntradaEspera entrada = (EntradaEspera) obj;
            if (entrada.getPassageiro().getId() == passageiro.getId()) {
                return fila.remover(entrada);
            }
        }
        return false;
    }

    public int getPosicao(Passageiro passageiro) {
        Object[] entradas = fila.toArray();
        for (int i = 0; i < entradas.length; i++) {
            EntradaEspera entrada = (EntradaEspera) entradas[i];
            if (entrada.getPassageiro().getId() == passageiro.getId()) {
                return i + 1;
            }
        }
        return -1;
    }

    public void exibirFila() {
        if (fila.isEmpty()) {
            System.out.println("Lista de espera da viagem #" + viagemId + " está vazia.");
            return;
        }
        System.out.println("=== Lista de Espera - Viagem #" + viagemId + " ===");
        Object[] entradas = fila.toArray();
        for (int i = 0; i < entradas.length; i++) {
            System.out.println((i + 1) + "º " + entradas[i]);
        }
    }

    public boolean isEmpty() { return fila.isEmpty(); }
    public int getTamanho() { return fila.getTamanho(); }
    public int getViagemId() { return viagemId; }
}
