
public class Main {

    public static void main(String[] args) {
        LogCorrida log = new LogCorrida(ConfiguracaoCorrida.ARQUIVO_LOG);
        GerenciadorCorrida gerenciador = new GerenciadorCorrida(ConfiguracaoCorrida.TAMANHO_PISTA, log);

        Corredor c1 = new Corredor(1, "MIN", Thread.MIN_PRIORITY, 80, gerenciador);
        Corredor c2 = new Corredor(2, "MED", Thread.NORM_PRIORITY, 50, gerenciador);
        Corredor c3 = new Corredor(3, "MAX", Thread.MAX_PRIORITY, 20, gerenciador);

        gerenciador.registrarCorredor(c1);
        gerenciador.registrarCorredor(c2);
        gerenciador.registrarCorredor(c3);

        gerenciador.registrarPasso();

        c1.start();
        c2.start();
        c3.start();

        // Aguarda término de todos os corredores
        try {
            c1.join();
            c2.join();
            c3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        gerenciador.finalizar();
    }
}
