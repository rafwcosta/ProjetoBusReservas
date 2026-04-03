package com.busreservas.model;

/**
 * US01 - Entidade Rota
 * Representa origem e destino de uma linha de ônibus
 * Responsável: Arthur
 */
public class Rota {

    private int id;
    private String cidadeOrigem;
    private String cidadeDestino;
    private double distanciaKm;
    private int duracaoEstimadaMinutos;

    public Rota(int id, String cidadeOrigem, String cidadeDestino, double distanciaKm, int duracaoEstimadaMinutos) {
        this.id = id;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.distanciaKm = distanciaKm;
        this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCidadeOrigem() { return cidadeOrigem; }
    public void setCidadeOrigem(String cidadeOrigem) { this.cidadeOrigem = cidadeOrigem; }

    public String getCidadeDestino() { return cidadeDestino; }
    public void setCidadeDestino(String cidadeDestino) { this.cidadeDestino = cidadeDestino; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }

    public int getDuracaoEstimadaMinutos() { return duracaoEstimadaMinutos; }
    public void setDuracaoEstimadaMinutos(int duracaoEstimadaMinutos) {
        this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
    }

    @Override
    public String toString() {
        return String.format("[Rota] %s → %s | %.1f km | ~%d min",
                cidadeOrigem, cidadeDestino, distanciaKm, duracaoEstimadaMinutos);
    }
}
