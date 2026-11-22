
public class Corredor extends Thread {

    private final int idCorredor;
    private final String rotulo;
    private final int delayMs;
    private final GerenciadorCorrida gerenciador;

    private int posicao = 0;

    /**
     * @param idCorredor Identificador numérico (1, 2, 3)
     * @param rotulo Rótulo de dedicação ("MIN", "MED", "MAX")
     * @param prioridade Prioridade da Thread (Thread.MIN_PRIORITY, NORM_PRIORITY, MAX_PRIORITY)
     * @param delayMs Tempo de pausa por passo em milissegundos
     * @param gerenciador Monitor central da corrida
     */
    public Corredor(int idCorredor, String rotulo, int prioridade, int delayMs, GerenciadorCorrida gerenciador) {
        super("Thread-Corredor-" + idCorredor + "-" + rotulo);
        this.idCorredor = idCorredor;
        this.rotulo = rotulo;
        this.delayMs = delayMs;
        this.gerenciador = gerenciador;

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
        int limitePista = gerenciador.getTamanhoPista();

        while (this.posicao < limitePista) {
            this.posicao++;

            gerenciador.registrarPasso();

            if (this.posicao >= limitePista) {
                break;
            }

            try {
                Thread.sleep(this.delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        gerenciador.registrarChegada(this);
    }
}
