package com.busreservas.servico;

import com.busreservas.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * US05, US06, US07, US08 - Serviço de Reservas
 * Responsável: Igor (US05), Rafael (US06), Rodolfo (US07), Victor (US08)
 */
public class ServicoReserva {

    private final List<Reserva> reservas;
    private final List<Bilhete> bilhetes;
    private final Map<Integer, ListaEspera> listasEspera; // chave = viagemId
    private int proximoIdReserva = 1;
    private int proximoIdBilhete = 1;

    private static final int CAPACIDADE_LISTA_ESPERA = 50;

    public ServicoReserva() {
        this.reservas = new ArrayList<>();
        this.bilhetes = new ArrayList<>();
        this.listasEspera = new HashMap<>();
    }

    // US05 - Reservar um assento
    public Bilhete reservar(Passageiro passageiro, Viagem viagem, int numeroAssento) {
        if (!passageiro.podeReservarPassagem()) {
            throw new SecurityException("Usuário sem permissão para reservar passagem.");
        }

        // US05/T3 - Validar disponibilidade
        Assento assento = viagem.buscarAssento(numeroAssento);
        if (assento == null) {
            throw new IllegalArgumentException("Assento " + numeroAssento + " não existe nesta viagem.");
        }

        if (!assento.isDisponivel()) {
            // Viagem lotada: adicionar na lista de espera
            if (viagem.isLotada()) {
                System.out.println("Viagem lotada! Adicionando " + passageiro.getNome() +
                        " à lista de espera...");
                adicionarListaEspera(passageiro, viagem, 1);
                return null;
            }
            throw new IllegalStateException("Assento " + numeroAssento + " já está reservado.");
        }

        // US05/T4 - Criar reserva
        assento.reservar(passageiro);
        Reserva reserva = new Reserva(proximoIdReserva++, passageiro, viagem, assento);
        reservas.add(reserva);
        passageiro.adicionarReserva(reserva);

        // Atualizar status da viagem
        viagem.atualizarStatus();

        // Gerar bilhete
        Bilhete bilhete = new Bilhete(proximoIdBilhete++, reserva, viagem.getLinha().getValorPassagem());
        bilhetes.add(bilhete);

        System.out.println("Reserva confirmada!");
        System.out.println(bilhete);
        return bilhete;
    }

    // US08 - Reservar múltiplos assentos para grupos
    public List<Bilhete> reservarMultiplos(Usuario usuario, Passageiro passageiro,
                                            Viagem viagem, List<Integer> numerosAssentos) {
        // US08/T1,T2 - Verificar permissão e disponibilidade
        if (!usuario.podeReservarMultiplosAssentos()) {
            throw new SecurityException("Apenas atendentes e administradores podem " +
                    "reservar múltiplos assentos.");
        }

        // US08/T3 - Validar disponibilidade total
        int disponiveis = viagem.getQuantidadeAssentosDisponiveis();
        if (numerosAssentos.size() > disponiveis) {
            throw new IllegalStateException("Assentos insuficientes. Solicitados: " +
                    numerosAssentos.size() + " | Disponíveis: " + disponiveis);
        }

        // US08/T4 e T5 - Selecionar e criar múltiplas reservas
        List<Bilhete> bilhetesGerados = new ArrayList<>();
        for (int numero : numerosAssentos) {
            Assento assento = viagem.buscarAssento(numero);
            if (assento == null || !assento.isDisponivel()) {
                throw new IllegalStateException("Assento " + numero + " indisponível.");
            }
            assento.reservar(passageiro);
            Reserva reserva = new Reserva(proximoIdReserva++, passageiro, viagem, assento);
            reservas.add(reserva);
            passageiro.adicionarReserva(reserva);
            Bilhete bilhete = new Bilhete(proximoIdBilhete++, reserva, viagem.getLinha().getValorPassagem());
            bilhetes.add(bilhete);
            bilhetesGerados.add(bilhete);
        }

        viagem.atualizarStatus();
        System.out.println(bilhetesGerados.size() + " bilhete(s) gerado(s) com sucesso!");
        return bilhetesGerados;
    }

    // US07 - Cancelar reserva e promover da lista de espera
    public boolean cancelar(Passageiro passageiro, int reservaId) {
        // US07/T1 - Localizar a reserva
        Reserva reserva = buscarReservaPorId(reservaId);
        if (reserva == null) {
            System.out.println("Reserva #" + reservaId + " não encontrada.");
            return false;
        }
        if (reserva.getPassageiro().getId() != passageiro.getId()) {
            throw new SecurityException("Passageiro não autorizado a cancelar esta reserva.");
        }

        // US07/T2 e T3 - Cancelar e liberar assento
        reserva.cancelar(); // Libera o assento internamente
        passageiro.removerReserva(reserva);

        System.out.println("Reserva #" + reservaId + " cancelada. Assento " +
                reserva.getAssento().getNumero() + " liberado.");

        // US06/T5 / US07/T4 - Promover próximo da lista de espera
        Viagem viagem = reserva.getViagem();
        viagem.atualizarStatus();
        promoverListaEspera(viagem, reserva.getAssento().getNumero());

        return true;
    }

    // US06 - Adicionar à lista de espera
    public void adicionarListaEspera(Passageiro passageiro, Viagem viagem, int qtd) {
        listasEspera.putIfAbsent(viagem.getId(),
                new ListaEspera(viagem.getId(), CAPACIDADE_LISTA_ESPERA));
        ListaEspera lista = listasEspera.get(viagem.getId());
        boolean inserido = lista.inserir(passageiro, viagem, qtd);
        if (inserido) {
            int posicao = lista.getPosicao(passageiro);
            System.out.println(passageiro.getNome() + " adicionado à lista de espera. " +
                    "Posição: " + posicao + "º");
        }
    }

    // US06/T5 - Promover primeiro da lista de espera automaticamente
    private void promoverListaEspera(Viagem viagem, int numeroAssento) {
        ListaEspera lista = listasEspera.get(viagem.getId());
        if (lista == null || lista.isEmpty()) return;

        ListaEspera.EntradaEspera proximo = lista.proximoDaFila();
        if (proximo != null) {
            System.out.println("Promovendo " + proximo.getPassageiro().getNome() +
                    " da lista de espera...");
            reservar(proximo.getPassageiro(), viagem, numeroAssento);
        }
    }

    // US05/T6 / US09 - Exibir reservas do passageiro
    public void exibirReservasPassageiro(Passageiro passageiro) {
        List<Reserva> minhas = passageiro.getMinhasReservas();
        if (minhas.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada para " + passageiro.getNome());
            return;
        }
        System.out.println("=== Reservas de " + passageiro.getNome() + " ===");
        for (Reserva r : minhas) {
            System.out.println(r);
        }
    }

    public void exibirListaEspera(int viagemId) {
        ListaEspera lista = listasEspera.get(viagemId);
        if (lista == null || lista.isEmpty()) {
            System.out.println("Sem passageiros na lista de espera para a viagem #" + viagemId);
            return;
        }
        lista.exibirFila();
    }

    private Reserva buscarReservaPorId(int id) {
        for (Reserva r : reservas) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public List<Reserva> getTodasReservas() { return new ArrayList<>(reservas); }
    public List<Bilhete> getTodosBilhetes() { return new ArrayList<>(bilhetes); }
}
