package com.busreservas.model;

/**
 * US02 - Entidade Horario
 * Responsável: Bianca
 *
 * Atributos conforme diagrama de entidades:
 *   - hora (String no formato "HH:mm")
 */
public class Horario {

    private int id;
    private String hora; // formato "HH:mm" — ex: "06:00"

    public Horario(int id, String hora) {
        validarFormato(hora);
        this.id = id;
        this.hora = hora;
    }

    // US02/T5 - Validar formato de horário
    private void validarFormato(String hora) {
        if (hora == null || !hora.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            throw new IllegalArgumentException(
                    "Formato de hora inválido: '" + hora + "'. Use HH:mm (ex: 06:00).");
        }
    }

    // Retorna minutos totais para comparação e ordenação
    public int getHoraEmMinutos() {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[0]) * 60 + Integer.parseInt(partes[1]);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getHora() { return hora; }
    public void setHora(String hora) {
        validarFormato(hora);
        this.hora = hora;
    }

    @Override
    public String toString() {
        return String.format("[Horário] %s", hora);
    }
}

