package facade;

import model.EmpresaJunior;
import model.Usuario;
import model.SolicitacaoEJ;
import dao.EmpresaJuniorDAO;
import dao.SolicitacaoDAO;

public class CadastroEJFacade {
    private EmpresaJuniorDAO ejDAO;
    private SolicitacaoDAO solicitacaoDAO;

    public CadastroEJFacade() {
        this.ejDAO = new EmpresaJuniorDAO();
        this.solicitacaoDAO = new SolicitacaoDAO();
    }

    // Método que consolida o processo: recebe as strings de texto e o objeto do usuário logado
    public boolean solicitarCadastroCompleto(String nomeEj, String cnpjEj, String documentoUrl, Usuario usuarioLogado) {
        
        // usa o DAO já existente para criar a EJ no banco e pegar o objeto completo
        EmpresaJunior novaEj = ejDAO.cadastrar(nomeEj, cnpjEj);

        if (novaEj == null || novaEj.getId() == null) {
            return false; // Se o banco falhou em criar a EJ, aborta o processo
        }

        // chama o seu DAO de solicitações passando exatamente os 3 parâmetros que ele exige
        SolicitacaoEJ solicitacaoCriada = solicitacaoDAO.cadastrar(documentoUrl, usuarioLogado, novaEj);

        // se a solicitação foi criada com sucesso e não retornou null, o padrão Facade cumpriu seu papel
        return solicitacaoCriada != null;
    }
}