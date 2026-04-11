package com.busreservas.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * US03 - Entidade Viagem
 * Responsável: Gabriel
 *
 * Atributos conforme diagrama de entidades:
 *   - id, data, horario, assentos: List, listaEspera
 */
public class Viagem {

    public enum StatusViagem {
        DISPONIVEL,
        LOTADA,
        CANCELADA,
        ENCERRADA
    }

    private int id;
    private LinhaOnibus linha;
    private Horario horario;
    private LocalDate data;
    private List<Assento> assentos;
    private ListaEspera listaEspera;
    private StatusViagem status;
    private int capacidadeTotal;

    public Viagem(int id, LinhaOnibus linha, Horario horario, LocalDate data, int capacidadeTotal) {
        if (linha == null) throw new IllegalArgumentException("Linha não pode ser nula.");
        if (horario == null) throw new IllegalArgumentException("Horário não pode ser nulo.");
        if (data == null) throw new IllegalArgumentException("Data não pode ser nula.");
        if (capacidadeTotal <= 0) throw new IllegalArgumentException("Capacidade deve ser positiva.");

        this.id = id;
        this.linha = linha;
        this.horario = horario;
        this.data = data;
        this.capacidadeTotal = capacidadeTotal;
        this.status = StatusViagem.DISPONIVEL;
        this.assentos = new ArrayList<>();
        this.listaEspera = new ListaEspera(id, 50);

        inicializarAssentos(capacidadeTotal);
    }

    // US03/T4 - Inicializar assentos numerados de 1 até capacidade
    private void inicializarAssentos(int quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            assentos.add(new Assento(i));
        }
    }

    // US04/T4 - Buscar assento pelo número
    public Assento buscarAssento(int numero) {
        for (Assento a : assentos) {
            if (a.getNumero() == numero) return a;
        }
        return null;
    }

    public List<Assento> getAssentosDisponiveis() {
        List<Assento> disponiveis = new ArrayList<>();
        for (Assento a : assentos) {
            if (a.isDisponivel()) disponiveis.add(a);
        }
        return disponiveis;
    }

    public int getQuantidadeAssentosDisponiveis() {
        return getAssentosDisponiveis().size();
    }

    public void atualizarStatus() {
        this.status = getAssentosDisponiveis().isEmpty()
                ? StatusViagem.LOTADA : StatusViagem.DISPONIVEL;
    }

    public boolean isLotada() { return getAssentosDisponiveis().isEmpty(); }

    public String getDataFormatada() {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Chave única para impedir viagens duplicadas
    public String getChaveUnica() {
        return linha.getCodigo() + "_" + data.toString() + "_" + horario.getHora();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LinhaOnibus getLinha() { return linha; }
    public void setLinha(LinhaOnibus linha) { this.linha = linha; }
    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public List<Assento> getAssentos() { return new ArrayList<>(assentos); }
    public ListaEspera getListaEspera() { return listaEspera; }
    public StatusViagem getStatus() { return status; }
    public void setStatus(StatusViagem status) { this.status = status; }
    public int getCapacidadeTotal() { return capacidadeTotal; }

    @Override
    public String toString() {
        return String.format("[Viagem #%d] Linha: %s | Data: %s | Horário: %s | Assentos livres: %d/%d | Status: %s",
                id, linha.getCodigo(), getDataFormatada(), horario.getHora(),
                getQuantidadeAssentosDisponiveis(), capacidadeTotal, status);
    }
}

// Bianca

public class Viagem {

    private LinhaOnibus linha;
    private Horario horario;

    public Viagem(LinhaOnibus linha, Horario horario) {
        this.linha = linha;
        this.horario = horario;
    }

    public LinhaOnibus getLinha() {
        return linha;
    }

    public Horario getHorario() {
        return horario;
    }

    @Override
    public String toString() {
        return "Viagem: " + linha.getCodigo() +
               " | " + linha.getOrigem() +
               " -> " + linha.getDestino() +
               " | Horário: " + horario;
    }

// Cadastro de viagens

public class CadastroViagens {

    private FilaEstatica<Viagem> viagens;

    public CadastroViagens(int capacidade) {
        viagens = new FilaEstatica<>(capacidade);
    }

    public boolean cadastrar(Viagem viagem) {
        return viagens.enqueue(viagem);
    }

    public Viagem remover() {
        return viagens.dequeue();
    }

    public Viagem[] listar() {
        return viagens.toArray();
    }

    @Override
    public String toString() {
        return viagens.toString();
    }
}
