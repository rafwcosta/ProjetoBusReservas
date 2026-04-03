package com.busreservas.model;

/**
 * US00 - Controle de Perfis de Usuário
 * Responsável: Uanderson
 */
public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private PerfilUsuario perfil;

    public Usuario(int id, String nome, String email, String senha, PerfilUsuario perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }

    // US00/T2 - Verificação de permissões por perfil
    public boolean podeGerenciarLinhas() {
        return perfil == PerfilUsuario.ADMINISTRADOR;
    }

    public boolean podeGerenciarHorarios() {
        return perfil == PerfilUsuario.ADMINISTRADOR;
    }

    public boolean podeCadastrarViagens() {
        return perfil == PerfilUsuario.ADMINISTRADOR;
    }

    public boolean podeReservarPassagem() {
        return perfil == PerfilUsuario.PASSAGEIRO || perfil == PerfilUsuario.ATENDENTE;
    }

    public boolean podeGerenciarListaEspera() {
        return perfil == PerfilUsuario.ATENDENTE || perfil == PerfilUsuario.ADMINISTRADOR;
    }

    public boolean podeCancelarReserva() {
        return perfil != null; // todos os perfis podem cancelar
    }

    public boolean podeReservarMultiplosAssentos() {
        return perfil == PerfilUsuario.ATENDENTE || perfil == PerfilUsuario.ADMINISTRADOR;
    }

    public boolean podeConsultarViagens() {
        return perfil != null; // todos os perfis podem consultar
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }

    @Override
    public String toString() {
        return String.format("[Usuário] ID: %d | Nome: %s | Email: %s | Perfil: %s",
                id, nome, email, perfil);
    }
}
