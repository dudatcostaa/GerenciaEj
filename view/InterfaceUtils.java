package view;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;

public class InterfaceUtils {

    // abre uma janela de seleção de arquivo do sistema operacional e retorna o arquivo escolhido
    public static File selecionarArquivo() {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.ICONIFIED); // minimiza a janela para não atrapalhar a visão
        JFileChooser chooser = new JFileChooser(); // componente swing de seleção de arquivo
        int res = chooser.showOpenDialog(frame); // abre o seletor e armazena a resposta do usuário
        frame.dispose(); // fecha a janela após a seleção
        return (res == JFileChooser.APPROVE_OPTION) ? chooser.getSelectedFile() : null; // retorna o arquivo ou null se cancelou
    }

    // abre o arquivo no programa padrão do sistema operacional
    public static void abrirArquivo(String path) {
        try {
            File f = new File(path);
            if (f.exists() && Desktop.isDesktopSupported())
                Desktop.getDesktop().open(f); // usa o Desktop do SO para abrir
            else
                System.out.println("Erro: Arquivo não encontrado.");
        } catch (Exception e) {
            System.out.println("Erro ao abrir: " + e.getMessage());
        }
    }
}
