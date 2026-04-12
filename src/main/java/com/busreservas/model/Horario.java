package com.busreservas.model;

public class Horario {

    private int id;
    private String hora;

    public Horario(int id, String hora) {
        validarFormato(hora);
        this.id = id;
        this.hora = hora;
    }

    private void validarFormato(String hora) {
        if (hora == null || !hora.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            throw new IllegalArgumentException(
                    "Formato de hora invalido: '" + hora + "'. Use HH:mm (ex: 06:00).");
        }
    }

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
        return String.format("[Horario] %s", hora);
    }
}
