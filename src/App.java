import java.util.Random;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) {
        var p = new Personagem();
        Random gerador = new Random();
        p.nome = JOptionPane.showInputDialog("Nome do personagem: ");
        LogAtividade logger = new LogAtividade();

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
    }
}
