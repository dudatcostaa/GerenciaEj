package controller;

import model.Evento;
import model.Usuario;
import model.dao.EventoDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventoController {

    private EventoDAO dao;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public EventoController() {
        this.dao = new EventoDAO();
    }

    // UC12 / RF13 — agenda um novo evento na EJ do usuário
    // RN09 — a data deve ser válida (garantido pelo parse na view) e não pode estar no passado
    // o criador entra automaticamente como convidado/participante do evento
    public String cadastrar(Usuario usuarioLogado, String titulo, String descricao, Date data) {
        Long empresaJuniorId = dao.buscarEmpresaJuniorId(usuarioLogado.getId());
        if (empresaJuniorId == null) {
            return "[ERRO] Você precisa estar vinculado a uma Empresa Júnior para agendar eventos.";
        }

        if (data.before(new Date())) {
            return "[ERRO] Não é possível agendar um evento em uma data/horário no passado.";
        }

        Evento criado = dao.cadastrar(titulo, descricao, data, empresaJuniorId, usuarioLogado.getId());
        return criado != null
            ? "[SUCESSO] Evento \"" + criado.getTitulo() + "\" agendado para " + SDF.format(criado.getData()) + "."
            : "[ERRO] Não foi possível agendar o evento.";
    }

    // RF12 — lista os eventos em que o usuário é convidado/participante num mês/ano
    public List<Evento> listarPorMes(Usuario usuarioLogado, int mes, int ano) {
        return dao.listarPorMes(usuarioLogado.getId(), mes, ano);
    }

    // lista os membros da mesma EJ que ainda não foram convidados para o evento
    // (exclui o próprio usuário logado da lista)
    public List<Usuario> listarConvidaveis(Usuario usuarioLogado, Evento evento) {
        Long empresaJuniorId = dao.buscarEmpresaJuniorId(usuarioLogado.getId());
        if (empresaJuniorId == null) return List.of();

        List<Usuario> membros = dao.listarMembrosDaEmpresa(empresaJuniorId);
        List<Usuario> jaConvidados = dao.listarConvidados(evento.getId());

        return membros.stream()
            .filter(m -> !m.getId().equals(usuarioLogado.getId()))
            .filter(m -> jaConvidados.stream().noneMatch(c -> c.getId().equals(m.getId())))
            .collect(Collectors.toList());
    }

    // UC12 / RF14 — convida um membro para um evento
    // RN04 — convites só podem ser enviados para usuários da mesma EJ
    public String convidar(Usuario usuarioLogado, Evento evento, Usuario convidado) {
        Long empresaOrganizador = dao.buscarEmpresaJuniorId(usuarioLogado.getId());
        Long empresaConvidado   = dao.buscarEmpresaJuniorId(convidado.getId());

        if (empresaOrganizador == null
                || !empresaOrganizador.equals(empresaConvidado)
                || !empresaOrganizador.equals(evento.getEmpresaJuniorId())) {
            return "[ERRO] Só é possível convidar membros da mesma Empresa Júnior.";
        }

        if (dao.jaConvidado(evento.getId(), convidado.getId())) {
            return "[AVISO] \"" + convidado.getNome() + "\" já foi convidado para esse evento.";
        }

        boolean ok = dao.convidar(evento.getId(), convidado.getId());
        return ok
            ? "[SUCESSO] \"" + convidado.getNome() + "\" foi convidado para o evento."
            : "[ERRO] Não foi possível enviar o convite.";
    }
}