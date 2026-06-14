package model;

public enum Funcionalidade {
    KANBAN("Acessar Quadros Kanban"),
    BIBLIOTECA("Acessar Biblioteca"),
    LEADS("Acessar Módulo de Leads"),
    BUSCAR_EJ("Buscar Empresas Juniores"),
    METRICAS("Métricas de Desempenho"),
    RELATORIO("Gerar Relatório Semanal"),
    PROJETOS("Gerenciar Projetos");

    private final String descricao;

    Funcionalidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}