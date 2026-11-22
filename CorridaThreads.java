import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CorridaThreads {
    public static final int TAMANHO_PISTA = 100;
    public static final String ARQUIVO_LOG = "log_corrida.txt";
    public static final Object LOCK = new Object();
    private static int iteracao = 0;
    private static volatile boolean corridaFinalizada = false;
    private static final List<Corredor> podio = Collections.synchronizedList(new ArrayList<>());
    private static PrintWriter logWriter;

    // Array com os corredores para renderização
    private static Corredor[] corredores;

    public static void main(String[] args) {
        inicializarLog();

        // Criação dos 3 corredores herdando de Thread:
        // Parâmetros: id, label, prioridade, delayBaseMs, variacaoDelayMs
        Corredor c1 = new Corredor(1, "MIN", Thread.MIN_PRIORITY, 90, 40);
        Corredor c2 = new Corredor(2, "MED", Thread.NORM_PRIORITY, 50, 25);
        Corredor c3 = new Corredor(3, "MAX", Thread.MAX_PRIORITY, 25, 15);

        corredores = new Corredor[]{c1, c2, c3};

        // Renderiza estado inicial
        synchronized (LOCK) {
            atualizarTelaELog();
        }

        // Inicia as threads
        c1.start();
        c2.start();
        c3.start();

        // Aguarda todas as threads terminarem
        try {
            c1.join();
            c2.join();
            c3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Exibe e grava encerramento da corrida
        finalizarCorrida();
    }

    /**
     * Inicializa o arquivo de log do zero a cada nova execução.
     */
    private static void inicializarLog() {
        try {
            logWriter = new PrintWriter(new BufferedWriter(new FileWriter(ARQUIVO_LOG, false)));
            logWriter.println("=== INÍCIO DA CORRIDA ===");
            logWriter.println("Pista: " + TAMANHO_PISTA + " metros");
            logWriter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao inicializar arquivo de log: " + e.getMessage());
        }
    }

    /**
     * Atualiza tela do console e grava linha no log.
     * DEVE ser chamado dentro de bloco synchronized (LOCK).
     */
    public static void atualizarTelaELog() {
        iteracao++;

        // 1. Limpa console (ANSI com fallback)
        limparConsole();

        // 2. Renderiza visual da pista no console
        System.out.println("================================================================================");
        System.out.println(" SIMULAÇÃO DE CORRIDA DE THREADS (Tamanho da pista: " + TAMANHO_PISTA + "m)");
        System.out.println(" Iteração: " + iteracao);
        System.out.println("================================================================================");

        for (Corredor c : corredores) {
            int pos = c.getPosicao();
            // Garante que pos não ultrapasse TAMANHO_PISTA
            pos = Math.min(pos, TAMANHO_PISTA);

            StringBuilder pista = new StringBuilder();
            pista.append(String.format("@%d [%-3s] |", c.getIdCorredor(), c.getRotulo()));

            for (int i = 0; i <= TAMANHO_PISTA; i++) {
                if (i == pos) {
                    pista.append("@");
                } else {
                    pista.append("-");
                }
            }
            pista.append(String.format("| %3d/%d m", pos, TAMANHO_PISTA));
            System.out.println(pista.toString());
        }
        System.out.println("================================================================================");

        // 3. Grava log no formato especificado
        if (logWriter != null && corredores != null && corredores.length == 3) {
            String linhaLog = String.format("Iteração %d | @1(%s): pos %d | @2(%s): pos %d | @3(%s): pos %d",
                    iteracao,
                    corredores[0].getRotulo(), corredores[0].getPosicao(),
                    corredores[1].getRotulo(), corredores[1].getPosicao(),
                    corredores[2].getRotulo(), corredores[2].getPosicao());
            logWriter.println(linhaLog);
            logWriter.flush();
        }
    }

    /**
     * Limpa tela com suporte a ANSI e fallback.
     */
    private static void limparConsole() {
        // Tenta limpar via sequência de escape ANSI
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Registra chegada de corredor e finalização da corrida.
     */
    public static void registrarChegada(Corredor c) {
        synchronized (LOCK) {
            podio.add(c);
            if (!corridaFinalizada) {
                corridaFinalizada = true;
            }
            atualizarTelaELog();
        }
    }

    /**
     * Encerra log e exibe classificação final no console.
     */
    private static void finalizarCorrida() {
        synchronized (LOCK) {
            System.out.println("\n🏁 CORRIDA FINALIZADA! 🏁\n");
            System.out.println("--- CLASSIFICAÇÃO FINAL ---");

            if (logWriter != null) {
                logWriter.println("\n=== CORRIDA FINALIZADA ===");
                logWriter.println("Classificação Final:");
            }

            for (int i = 0; i < podio.size(); i++) {
                Corredor c = podio.get(i);
                String posTxt = String.format("%dº Lugar: @%d [%s] (Prioridade: %d)",
                        (i + 1), c.getIdCorredor(), c.getRotulo(), c.getPriority());
                System.out.println(posTxt);
                if (logWriter != null) {
                    logWriter.println(posTxt);
                }
            }

            if (logWriter != null) {
                logWriter.flush();
                logWriter.close();
            }
        }
    }

    /**
     * Classe que representa o corredor e a execução da thread.
     * Obrigatório: herda de Thread (extends Thread) e sobrescreve run().
     */
    public static class Corredor extends Thread {
        private final int idCorredor;
        private final String rotulo;
        private final int delayBaseMs;
        private final int variacaoDelayMs;
        private final Random random = new Random();

        private int posicao = 0;

        /**
         * @param idCorredor Identificador numérico (1, 2, 3)
         * @param rotulo Rótulo de dedicação ("MIN", "MED", "MAX")
         * @param prioridade Prioridade da Thread (Thread.MIN_PRIORITY, NORM_PRIORITY, MAX_PRIORITY)
         * @param delayBaseMs Tempo base de pausa por passo em milissegundos
         * @param variacaoDelayMs Variação aleatória do delay para dinamismo
         */
        public Corredor(int idCorredor, String rotulo, int prioridade, int delayBaseMs, int variacaoDelayMs) {
            super("Corredor-" + idCorredor + "-" + rotulo);
            this.idCorredor = idCorredor;
            this.rotulo = rotulo;
            this.delayBaseMs = delayBaseMs;
            this.variacaoDelayMs = variacaoDelayMs;
            this.setPriority(prioridade);
        }

        public int getIdCorredor() {
            return idCorredor;
        }

        public String getRotulo() {
            return rotulo;
        }

        public int getPosicao() {
            return posicao;
        }

        @Override
        public void run() {
            while (posicao < TAMANHO_PISTA) {
                // Avança 1 posição na pista
                posicao++;

                // Sincroniza renderização no console e gravação no log
                synchronized (LOCK) {
                    atualizarTelaELog();
                }

                // Verifica se cruzou a linha de chegada
                if (posicao >= TAMANHO_PISTA) {
                    break;
                }

                // Ritmo / delay diferenciado por nível de dedicação
                int delay = delayBaseMs + (variacaoDelayMs > 0 ? random.nextInt(variacaoDelayMs) : 0);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Registra chegada ao pódio
            registrarChegada(this);
        }
    }
}
