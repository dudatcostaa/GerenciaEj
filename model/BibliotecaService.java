package model;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaService {
    private List<Arquivo> arquivos = new ArrayList<>(); // lista que armazena todos os arquivos da biblioteca

    // adiciona um arquivo novo a biblioteca
    public void adicionarArquivo(Arquivo arquivo) {
        arquivos.add(arquivo);
    }

    // retorna todos os arquivos da biblioteca
    public List<Arquivo> listarTodos() {
        return arquivos;
    }

    // remove um arquivo da biblioteca
    public void removerArquivo(Arquivo arquivo) {
        arquivos.remove(arquivo);
    }

    // verifica se o usuário logado tem permissão para excluir o arquivo
    public boolean podeExcluir(Usuario logado, Arquivo arquivo, List<Long> idsMembrosAtivos) {
        if (arquivo.getAutorId().equals(logado.getId())) return true; // o autor sempre pode excluir o próprio arquivo
        if (logado.getCargo() == Cargo.DIRETOR) {
            return !idsMembrosAtivos.contains(arquivo.getAutorId()); // diretor só pode excluir arquivos de membros inativos
        }
        return false; // membro não pode excluir arquivos de outros
    }
}
