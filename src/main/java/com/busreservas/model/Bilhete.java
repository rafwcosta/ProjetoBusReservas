package com.busreservas.model;

/**
 * Entidade Bilhete - Comprovante de reserva confirmada
 */
public class Bilhete {

    private int id;
    private Reserva reserva;
    private double valor;
    private String codigoBilhete;

    public Bilhete(int id, Reserva reserva, double valor) {
        this.id = id;
        this.reserva = reserva;
        this.valor = valor;
        this.codigoBilhete = gerarCodigo(id, reserva);
    }

    private String gerarCodigo(int id, Reserva reserva) {
        return String.format("BLT-%04d-%s-%d",
                id,
                reserva.getViagem().getLinha().getCodigo(),
                reserva.getAssento().getNumero());
    }

    public int getId() { return id; }
    public Reserva getReserva() { return reserva; }
    public double getValor() { return valor; }
    public String getCodigoBilhete() { return codigoBilhete; }

    @Override
    public String toString() {
        return String.format(
                "==============================\n" +
                "         BILHETE DE VIAGEM    \n" +
                "==============================\n" +
                "Código:     %s\n" +
                "Passageiro: %s\n" +
                "Linha:      %s\n" +
                "Data:       %s\n" +
                "Partida:    %s\n" +
                "Origem:     %s\n" +
                "Destino:    %s\n" +
                "Assento:    %d\n" +
                "Valor:      R$ %.2f\n" +
                "Status:     %s\n" +
                "==============================",
                codigoBilhete,
                reserva.getPassageiro().getNome(),
                reserva.getViagem().getLinha().getNome(),
                reserva.getViagem().getDataFormatada(),
                reserva.getViagem().getHorario().getHora(),
                reserva.getViagem().getLinha().getRota().getCidadeOrigem(),
                reserva.getViagem().getLinha().getRota().getCidadeDestino(),
                reserva.getAssento().getNumero(),
                valor,
                reserva.getStatus()
        );
    }
}
