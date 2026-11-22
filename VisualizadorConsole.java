import java.util.List;

public class VisualizadorConsole {

    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void renderizar(int iteracao, List<Corredor> corredores, int tamanhoPista) {
        limparConsole();

        System.out.println("================================================================================");
        System.out.println(" SIMULAÇÃO DE CORRIDA DE THREADS (Tamanho da pista: " + tamanhoPista + "unidades)");
        System.out.println(" Iteração: " + iteracao);
        System.out.println("================================================================================");

        for (Corredor c : corredores) {
            int pos = Math.min(c.getPosicao(), tamanhoPista);

            StringBuilder pista = new StringBuilder();
            pista.append(String.format("@%d [%-3s] |", c.getIdCorredor(), c.getRotulo()));

            for (int i = 0; i <= tamanhoPista; i++) {
                if (i == pos) {
                    pista.append("@");
                } else {
                    pista.append("-");
                }
            }
            pista.append(String.format("| %3d/%d unidades", pos, tamanhoPista));
            System.out.println(pista.toString());
        }
        System.out.println("================================================================================");
    }

    public static void exibirClassificacaoFinal(List<Corredor> podio) {
        System.out.println("\nCORRIDA FINALIZADA!\n");
        System.out.println("--- CLASSIFICAÇÃO FINAL ---");
        for (int i = 0; i < podio.size(); i++) {
            Corredor c = podio.get(i);
            System.out.printf("%dº Lugar: @%d [%s] (Prioridade: %d)%n", (i + 1), c.getIdCorredor(), c.getRotulo(), c.getPriority());
        }
    }
}
