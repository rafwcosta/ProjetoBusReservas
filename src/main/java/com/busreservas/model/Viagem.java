package com.busreservas.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        if (linha == null) throw new IllegalArgumentException("Linha nao pode ser nula.");
        if (horario == null) throw new IllegalArgumentException("Horario nao pode ser nulo.");
        if (data == null) throw new IllegalArgumentException("Data nao pode ser nula.");
        // US03/T5 - Validar que a data não é passada
        if (data.isBefore(LocalDate.now())) throw new IllegalArgumentException("Data da viagem nao pode ser no passado.");
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

    private void inicializarAssentos(int quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            assentos.add(new Assento(i));
        }
    }

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
        // US03/T5 - Não sobrescrever status terminal (CANCELADA ou ENCERRADA)
        if (status == StatusViagem.CANCELADA || status == StatusViagem.ENCERRADA) return;
        this.status = getAssentosDisponiveis().isEmpty()
                ? StatusViagem.LOTADA : StatusViagem.DISPONIVEL;
    }

    public boolean isLotada() { return getAssentosDisponiveis().isEmpty(); }

    public String getDataFormatada() {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

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
        return String.format("[Viagem #%d] Linha: %s | Data: %s | Horario: %s | Assentos livres: %d/%d | Status: %s",
                id, linha.getCodigo(), getDataFormatada(), horario.getHora(),
                getQuantidadeAssentosDisponiveis(), capacidadeTotal, status);
    }
}
