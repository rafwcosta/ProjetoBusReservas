package com.busreservas.servico;

import com.busreservas.model.Passageiro;
import com.busreservas.model.PerfilUsuario;
import com.busreservas.model.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 * US00 - Serviço de Controle de Perfis de Usuário
 * Responsável: Uanderson
 */
public class ServicoUsuario {

    private final List<Usuario> usuarios;
    private final List<Passageiro> passageiros;
    private int proximoId = 1;

    public ServicoUsuario() {
        this.usuarios = new ArrayList<>();
        this.passageiros = new ArrayList<>();
        criarUsuariosDefault();
    }

    // US00/T1 - Criação de perfis padrão do sistema
    private void criarUsuariosDefault() {
        usuarios.add(new Usuario(proximoId++, "Admin Sistema", "admin@bus.com",
                "admin123", PerfilUsuario.ADMINISTRADOR));
        usuarios.add(new Usuario(proximoId++, "Atendente João", "joao@bus.com",
                "atend123", PerfilUsuario.ATENDENTE));
    }

    // US00/T2 - Cadastrar novo usuário com perfil
    public Usuario cadastrarUsuario(Usuario solicitante, String nome, String email,
                                    String senha, PerfilUsuario perfil) {
        if (solicitante.getPerfil() != PerfilUsuario.ADMINISTRADOR) {
            throw new SecurityException("Apenas administradores podem cadastrar usuários.");
        }
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("Email já cadastrado: " + email);
            }
        }
        Usuario novo = new Usuario(proximoId++, nome, email, senha, perfil);
        usuarios.add(novo);
        System.out.println("Usuário cadastrado: " + novo);
        return novo;
    }

    // Cadastrar passageiro (self-service)
    public Passageiro cadastrarPassageiro(String nome, String email, String senha, String cpf, String telefone) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("Email já cadastrado: " + email);
            }
        }
        Passageiro passageiro = new Passageiro(proximoId++, nome, email, senha, cpf, telefone);
        usuarios.add(passageiro);
        passageiros.add(passageiro);
        System.out.println("Passageiro cadastrado: " + passageiro);
        return passageiro;
    }

    // US00/T3 - Autenticar usuário
    public Usuario autenticar(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                System.out.println("Login realizado: " + u.getNome() + " [" + u.getPerfil() + "]");
                return u;
            }
        }
        throw new SecurityException("Email ou senha inválidos.");
    }

    // US00/T3 - Exibir regras de acesso por perfil
    public void exibirPermissoes(PerfilUsuario perfil) {
        System.out.println("=== Permissões do perfil: " + perfil + " ===");
        Usuario exemplo = new Usuario(0, "", "", "", perfil);
        System.out.println("Gerenciar Linhas:         " + exemplo.podeGerenciarLinhas());
        System.out.println("Gerenciar Horários:       " + exemplo.podeGerenciarHorarios());
        System.out.println("Cadastrar Viagens:        " + exemplo.podeCadastrarViagens());
        System.out.println("Reservar Passagem:        " + exemplo.podeReservarPassagem());
        System.out.println("Múltiplos Assentos:       " + exemplo.podeReservarMultiplosAssentos());
        System.out.println("Gerenciar Lista Espera:   " + exemplo.podeGerenciarListaEspera());
        System.out.println("Cancelar Reserva:         " + exemplo.podeCancelarReserva());
        System.out.println("Consultar Viagens:        " + exemplo.podeConsultarViagens());
    }

    public Passageiro buscarPassageiroPorId(int id) {
        for (Passageiro p : passageiros) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public List<Passageiro> listarPassageiros() {
        return new ArrayList<>(passageiros);
    }
}
