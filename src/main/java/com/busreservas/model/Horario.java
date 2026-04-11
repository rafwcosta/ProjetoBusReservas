package com.busreservas.model;

/**
 * US02 - Entidade Horario
 * Responsável: Bianca
 *
 * Atributos conforme diagrama de entidades:
 *   - hora (String no formato "HH:mm")
 */
public class Horario {

    private String horario;

    private static final Pattern PADRAO = 
        Pattern.compile("^([01]\\d|2[0-3]):([0-5]\\d)$");

    public Horario(String horario) {
        if (!isValid(horario)) {
            throw new IllegalArgumentException("Formato inválido. Use HH:mm");
        }
        this.horario = horario;
    }

    public String getHorario() {
        return horario;
    }

    public static boolean isValido(String horario) {
        if (horario == null) return false;
        return PADRAO.matcher(horario).matches();
    }

    @Override
    public String toString() {
        return horario;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Horario)) return false;
        Horario outro = (Horario) obj;
        return horario.equals(outro.horario);
    }
}

