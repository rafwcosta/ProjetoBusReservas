package com.busreservas.model;

/**
 * Entidade Bilhete - Comprovante de reserva confirmada
 */
public class Bilhete {

    private final int id;
    private final Reserva reserva;
    private final double valor;
    private final String codigoBilhete;

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
        return String.format("""
                            ==============================
                                    BILHETE DE VIAGEM    
                            ==============================
                            C\u00f3digo:     %s
                            Passageiro: %s
                            Linha:      %s
                            Data:       %s
                            Partida:    %s
                            Origem:     %s
                            Destino:    %s
                            Assento:    %d
                            Valor:      R$ %.2f
                            Status:     %s
                            ==============================""",
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
