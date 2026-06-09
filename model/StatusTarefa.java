package model;

public enum StatusTarefa {
    // usamos códigos ansi para colorir o texto no terminal para seguir o rnf04 até que a interface seja implementada
    PENDENTE("\u001B[31m"), // vermelho
    EM_PROGRESSO("\u001B[33m"), // amarelo
    EM_REVISAO("\u001B[35m"), // roxo
    PRONTO("\u001B[32m"); // verde

    private final String cor; // armazena o código da cor

    StatusTarefa(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }

    // volta ao normal depois de printar as cores
    public static final String RESET = "\u001B[0m";
}
