package controller;

import java.io.File;
import java.util.List;
import model.Arquivo;
import model.BibliotecaService;
import model.Usuario;
import view.BibliotecaView;
import view.InterfaceUtils;

public class BibliotecaController {
    private BibliotecaService service; // regras de negócio da biblioteca, como quem pode excluir
    private BibliotecaView view; // a view é injetada pelo construtor
    private Usuario usuarioLogado;
    private List<Usuario> usuariosSistema;
    private List<Long> idsAtivos; // id dos membros ativos para verificar permissão de exclusão -> depois, vamos adicionar ou lista de membros ativos na ej pra pegar de lá ou um status de ativo em usuário

    // construtor
    public BibliotecaController(BibliotecaService s, BibliotecaView v, Usuario u, List<Usuario> us, List<Long> ids) {
        this.service = s;
        this.view = v;
        this.usuarioLogado = u;
        this.usuariosSistema = us;
        this.idsAtivos = ids;
    }

    // loop da bibioteca, chama o método de acordo com oq o usuário escolher
    public void iniciar() {
        while (true) {
            int op = view.mostrarMenu(usuarioLogado.getNome(), usuarioLogado.getCargo());
            if (op == 1) upload();
            else if (op == 2) abrir();
            else if (op == 3) excluir();
            else if (op == 0) break;
        }
    }

    // abre o seletor de arquivos do sistema e envia o arquivo escolhido para a biblioteca
    private void upload() {
        File f = InterfaceUtils.selecionarArquivo(); // abre janela de seleção de arquivo
        if (f != null) {
            service.adicionarArquivo(new Arquivo(System.currentTimeMillis(), f.getName(), f.getAbsolutePath(), usuarioLogado.getId()));
            view.mostrarMensagem("Arquivo enviado!");
        }
    }

    // exibe a lista de arquivos e abre o arquivo que o usuário escolher
    private void abrir() {
        List<Arquivo> lista = service.listarTodos();
        view.exibirLista(lista, usuariosSistema); // exibe a lista com o nome do autor de cada arquivo
        int idx = view.pedirIndice("ABRIR");
        if (idx >= 0 && idx < lista.size()) InterfaceUtils.abrirArquivo(lista.get(idx).getUrl());
    }

    // exibe a lista de arquivos e exclui o arquivo escolhido, se o usuário tiver permissão
    private void excluir() {
        List<Arquivo> lista = service.listarTodos();
        view.exibirLista(lista, usuariosSistema); // exibe a lista com o nome do autor de cada arquivo
        int idx = view.pedirIndice("EXCLUIR");
        if (idx >= 0 && idx < lista.size()) {
            Arquivo alvo = lista.get(idx);
            // a regra de quem pode excluir fica no service
            if (service.podeExcluir(usuarioLogado, alvo, idsAtivos)) {
                service.removerArquivo(alvo);
                view.mostrarMensagem("Removido com sucesso.");
            } else {
                view.mostrarMensagem("Permissão negada.");
            }
        }
    }
}
