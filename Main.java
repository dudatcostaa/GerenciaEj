import view.EmpresaJuniorView;
import view.UsuarioView;

public class Main {
    public static void main(String[] args) {
    
        UsuarioView tela = new UsuarioView();
        tela.exibirMenuPrincipal();

        EmpresaJuniorView viewBusca = new EmpresaJuniorView();
        viewBusca.exibirMenuBusca();
    }
}