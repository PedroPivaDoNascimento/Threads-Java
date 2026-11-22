import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LogCorrida {

    private final String caminhoArquivo;
    private PrintWriter writer;

    public LogCorrida(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        inicializarArquivo();
    }

    private void inicializarArquivo() {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(caminhoArquivo, false)));
            this.writer.println("=== INÍCIO DA CORRIDA ===");
            this.writer.println("Pista: " + ConfiguracaoCorrida.TAMANHO_PISTA + " unidades");
            this.writer.flush();
        } catch (IOException e) {
            System.err.println("Erro ao inicializar arquivo de log: " + e.getMessage());
        }   
    }

    public synchronized void registrarIteracao(int iteracao, List<Corredor> corredores) {
        if (this.writer == null || corredores.size() < 3) {
            return;
        }

        String linha = String.format("Iteração %d | @1(%s): pos %d | @2(%s): pos %d | @3(%s): pos %d",
                iteracao,
                corredores.get(0).getRotulo(), corredores.get(0).getPosicao(),
                corredores.get(1).getRotulo(), corredores.get(1).getPosicao(),
                corredores.get(2).getRotulo(), corredores.get(2).getPosicao());

        this.writer.println(linha);
        this.writer.flush();
    }

    public synchronized void registrarFinalizacao(List<Corredor> podio) {
        if (this.writer == null) {
            return;
        }

        this.writer.println();
        this.writer.println("=== CORRIDA FINALIZADA ===");
        this.writer.println("Classificação Final:");
        for (int i = 0; i < podio.size(); i++) {
            Corredor c = podio.get(i);
            this.writer.printf("%dº Lugar: @%d [%s] (Prioridade: %d)%n",
                    (i + 1), c.getIdCorredor(), c.getRotulo(), c.getPriority());
        }
        this.writer.flush();
        this.writer.close();
    }
}
