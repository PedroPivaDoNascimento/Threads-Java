import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class GerenciadorCorrida {

    private final int tamanhoPista;
    private final LogCorrida logCorrida;
    private final List<Corredor> corredores = new ArrayList<>();
    private final List<Corredor> podio = Collections.synchronizedList(new ArrayList<>());

    private int iteracao = 0;
    private volatile boolean finalizada = false;

    public GerenciadorCorrida(int tamanhoPista, LogCorrida logCorrida) {
        this.tamanhoPista = tamanhoPista;
        this.logCorrida = logCorrida;
    }

    public synchronized void registrarCorredor(Corredor corredor) {
        this.corredores.add(corredor);
    }

    public int getTamanhoPista() {
        return this.tamanhoPista;
    }

    public synchronized void registrarPasso() {
        this.iteracao++;
        VisualizadorConsole.renderizar(this.iteracao, this.corredores, this.tamanhoPista);
        this.logCorrida.registrarIteracao(this.iteracao, this.corredores);
    }

    public synchronized void registrarChegada(Corredor corredor) {
        this.podio.add(corredor);
        if (!this.finalizada) {
            this.finalizada = true;
        }
        this.registrarPasso();
    }

    public synchronized void finalizar() {
        VisualizadorConsole.exibirClassificacaoFinal(this.podio);
        this.logCorrida.registrarFinalizacao(this.podio);
    }
}
