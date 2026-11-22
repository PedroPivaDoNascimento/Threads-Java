# Simulação de Corrida de Threads em Java

Projeto em Java puro que demonstra conceitos de **concorrência**, **sincronização** e **prioridades/ritmo de execução** entre múltiplas threads concorrentes.

A simulação visualiza uma corrida no terminal entre 3 corredores (`@`) percorrendo uma pista (`-`), registrando cada iteração no console e em arquivo de log `.txt`.

---

## 🎯 Objetivo Pedagógico

Demonstrar a diferença visual de dedicação e velocidade entre threads combinando:
1. **Prioridade de Thread**: `Thread.MIN_PRIORITY` (1), `Thread.NORM_PRIORITY` (5) e `Thread.MAX_PRIORITY` (10).
2. **Ritmo / Delay (`delayMs`)**: Pausa (`Thread.sleep(delayMs)`) diferenciada por passo para cada nível de dedicação:
   - `@3 [MAX]`: delay menor (ex.: 20ms) -> mais veloz.
   - `@2 [MED]`: delay intermediário (ex.: 50ms) -> velocidade média.
   - `@1 [MIN]`: delay maior (ex.: 80ms) -> menos veloz.
3. **Sincronização**: Uso de blocos sincronizados (`synchronized`) para garantir integridade na renderização da tela e gravação atômica do log.

---

## 📂 Estrutura das Classes

- **[`ConfiguracaoCorrida.java`](ConfiguracaoCorrida.java)**: Constantes do projeto (`TAMANHO_PISTA` e nome do arquivo de log).
- **[`Corredor.java`](Corredor.java)**: Herda de `java.lang.Thread` (`extends Thread`), sobrescreve `run()`, aplica a prioridade e o `delayMs` a cada passo.
- **[`GerenciadorCorrida.java`](GerenciadorCorrida.java)**: Monitor central sincronizado (`synchronized`). Coordena renderização, log e ordem de chegada ao pódio.
- **[`VisualizadorConsole.java`](VisualizadorConsole.java)**: Limpa o terminal e renderiza a pista e a classificação final.
- **[`LogCorrida.java`](LogCorrida.java)**: Reseta e grava as posições no arquivo `log_corrida.txt`.
- **[`Main.java`](Main.java)**: Ponto de entrada (`main`), instancia os 3 corredores com seus respectivos delays e inicia as threads.

---

## 🚀 Como Executar

### Execução direta (Java 11+)
```bash
java Main.java
```

### Compilar e Executar tradicional
```bash
javac *.java
java Main
```

---

## ⚙️ Customização

- **Tamanho da pista**: Altere em [`ConfiguracaoCorrida.java`](ConfiguracaoCorrida.java):
  ```java
  public static final int TAMANHO_PISTA = 100;
  ```
- **Ajustar delays e prioridades**: Altere em [`Main.java`](Main.java):
  ```java
  // Parâmetros: (id, rotulo, prioridade, delayMs, gerenciador)
  Corredor c1 = new Corredor(1, "MIN", Thread.MIN_PRIORITY, 80, gerenciador);
  Corredor c2 = new Corredor(2, "MED", Thread.NORM_PRIORITY, 50, gerenciador);
  Corredor c3 = new Corredor(3, "MAX", Thread.MAX_PRIORITY, 20, gerenciador);
  ```
