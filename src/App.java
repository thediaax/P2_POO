import java.util.Random;
import javax.swing.JOptionPane;
import java.util.List;

public class App {
    public static void main(String[] args) {
        while (true) {
            String menu = "1- Jogar\n2- Consultar log completo\n3- Sair";
            String escolha = JOptionPane.showInputDialog(menu);

            if (escolha.equals("1")) {
                var p = new Personagem();
                Random gerador = new Random();
                p.nome = JOptionPane.showInputDialog("Nome do personagem: ");
                LogAtividade logger = new LogAtividade();

                if (logger.getConnection() == null) {
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                    continue;
                }

                StringBuilder output = new StringBuilder();
                output.append(p.toString()).append("\n\n");

                for (int i = 0; i < 10; i++) {
                    if (p.energia > 0) {
                        int oQueFazer = gerador.nextInt(3);
                        switch (oQueFazer) {
                            case 0:
                                p.cacar();
                                logger.logActivity("caçar");
                                output.append(p.nome).append(" está caçando...\n");
                                output.append(p.toString()).append("\n");
                                break;
                            case 1:
                                p.comer();
                                logger.logActivity("comer");
                                output.append(p.nome).append(" está comendo...\n");
                                output.append(p.toString()).append("\n");
                                break;
                            case 2:
                                p.dormir();
                                logger.logActivity("dormir");
                                output.append(p.nome).append(" está dormindo...\n");
                                output.append(p.toString()).append("\n");
                                break;
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, output.toString());
                logger.close();

            } else if (escolha.equals("2")) {
                LogAtividade logger = new LogAtividade();

                if (logger.getConnection() == null) {
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                    continue;
                }

                List<String> logs = logger.getLogs();
                StringBuilder logOutput = new StringBuilder("Log de Atividades:\n\n");
                for (String log : logs) {
                    logOutput.append(log).append("\n");
                }
                JOptionPane.showMessageDialog(null, logOutput.toString());
                logger.close();

            } else if (escolha.equals("3")) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
            }
        }
    }
}