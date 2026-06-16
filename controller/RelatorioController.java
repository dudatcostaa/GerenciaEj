package controller;

import model.Gasto;
import model.Proposta;
import model.Usuario;
import model.dao.RelatorioDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RelatorioController {

    private RelatorioDAO dao;
    private static final SimpleDateFormat SDF      = new SimpleDateFormat("dd/MM/yyyy");

    public RelatorioController() {
        this.dao = new RelatorioDAO();
    }

    // calcula o início (segunda) e fim (domingo) da semana escolhida
    public Date[] calcularIntervalo(int semana, int ano) {
        Calendar cal = Calendar.getInstance();
        cal.setMinimalDaysInFirstWeek(1);
        cal.set(Calendar.YEAR, ano);
        cal.set(Calendar.WEEK_OF_YEAR, semana);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicio = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.WEEK_OF_YEAR, 1); // domingo da semana seguinte = fim da semana atual
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date fim = cal.getTime();

        return new Date[]{inicio, fim};
    }

    // gera o arquivo .txt e retorna o caminho onde foi salvo
    public String gerarRelatorio(int semana, int ano, Usuario usuario) throws IOException {
        Date[] intervalo = calcularIntervalo(semana, ano);
        Date inicio = intervalo[0];
        Date fim    = intervalo[1];

        String nomeEJ    = dao.buscarNomeEJ(usuario.getId());
        double faturamento = dao.buscarFaturamentoPeriodo(inicio, fim);
        List<Gasto>    gastos    = dao.buscarGastosPeriodo(inicio, fim);
        List<Proposta> propostas = dao.buscarPropostasPeriodo(inicio, fim);
        Map<String, Integer> leads    = dao.buscarLeadsPorStatus();
        Map<String, Integer> projetos = dao.buscarProjetosPorStatus();

        double totalGastos = gastos.stream().mapToDouble(Gasto::getValor).sum();
        int totalLeads     = leads.values().stream().mapToInt(Integer::intValue).sum();
        int totalProjetos  = projetos.values().stream().mapToInt(Integer::intValue).sum();

        String nomeArquivo = String.format("relatorio_semana%02d_%d.txt", semana, ano);
        String caminho     = System.getProperty("user.home") + "/" + nomeArquivo;

        try (PrintWriter pw = new PrintWriter(new FileWriter(caminho))) {
            linha(pw, "=", 56);
            pw.println("  RELATÓRIO SEMANAL — GERENCIA EJ");
            linha(pw, "=", 56);
            pw.printf("  Empresa       : %s%n", nomeEJ);
            pw.printf("  Gerado por    : %s%n", usuario.getNome());
            pw.printf("  Período       : %s a %s (Semana %d/%d)%n",
                    SDF.format(inicio), SDF.format(fim), semana, ano);
            pw.printf("  Gerado em     : %s%n", SDF.format(new Date()));
            pw.println();

            // ── Faturamento ─────────────────────────────────────────────────
            secao(pw, "FATURAMENTO DA SEMANA");
            pw.printf("  Projetos iniciados no período : R$ %,.2f%n", faturamento);
            pw.println();

            // ── Gastos ──────────────────────────────────────────────────────
            secao(pw, "GASTOS DA SEMANA");
            if (gastos.isEmpty()) {
                pw.println("  Nenhum gasto registrado no período.");
            } else {
                for (Gasto g : gastos) {
                    pw.printf("  %-30s R$ %,.2f  (%s)%n",
                            g.getDescricao(), g.getValor(), SDF.format(g.getData()));
                }
                pw.println();
                pw.printf("  Total de gastos : R$ %,.2f%n", totalGastos);
            }
            pw.println();

            // ── Propostas ───────────────────────────────────────────────────
            secao(pw, "PROPOSTAS DA SEMANA");
            if (propostas.isEmpty()) {
                pw.println("  Nenhuma proposta registrada no período.");
            } else {
                for (Proposta p : propostas) {
                    pw.printf("  %-30s R$ %,.2f  (%s)%n",
                            p.getNomeCliente(), p.getValor(), SDF.format(p.getData()));
                }
                pw.println();
                pw.printf("  Total de propostas enviadas : %d%n", propostas.size());
            }
            pw.println();

            // ── Leads ───────────────────────────────────────────────────────
            secao(pw, "FUNIL DE LEADS (snapshot atual)");
            pw.printf("  Prospecção  : %d%n", leads.getOrDefault("PROSPECCAO", 0));
            pw.printf("  Negociação  : %d%n", leads.getOrDefault("NEGOCIACAO", 0));
            pw.printf("  Fechados    : %d%n", leads.getOrDefault("FECHADO", 0));
            pw.printf("  Perdidos    : %d%n", leads.getOrDefault("PERDIDO", 0));
            pw.printf("  Total       : %d%n", totalLeads);
            double txConv = totalLeads > 0
                    ? (leads.getOrDefault("FECHADO", 0) * 100.0) / totalLeads : 0;
            pw.printf("  Tx. conversão : %.1f%%%n", txConv);
            pw.println();

            // ── Projetos ────────────────────────────────────────────────────
            secao(pw, "PROJETOS (snapshot atual)");
            pw.printf("  Em planejamento : %d%n", projetos.getOrDefault("EM_PLANEJAMENTO", 0));
            pw.printf("  Em execução     : %d%n", projetos.getOrDefault("EM_EXECUCAO", 0));
            pw.printf("  Finalizados     : %d%n", projetos.getOrDefault("FINALIZADO", 0));
            pw.printf("  Total           : %d%n", totalProjetos);
            pw.println();

            linha(pw, "=", 56);
            pw.println("  FIM DO RELATÓRIO");
            linha(pw, "=", 56);
        }

        return caminho;
    }

    // número da semana atual
    public int semanaAtual() {
        Calendar cal = Calendar.getInstance();
        cal.setMinimalDaysInFirstWeek(1);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public int anoAtual() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    private void secao(PrintWriter pw, String titulo) {
        pw.println("--- " + titulo + " ---");
    }

    private void linha(PrintWriter pw, String char_, int tamanho) {
        pw.println(char_.repeat(tamanho));
    }
}