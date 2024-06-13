import java.util.Random;
import javax.swing.JOptionPane;

public class Personagem {
    String nome;
    int energia;
    private int fome;
    private int sono;
    private int usuarioId;
    private int sessaoId;

    private LogAtividade logAtividade;
    private int cacadas;
    private int sonos;

    Personagem() {
        nome = null;
        energia = 10;
        fome = 0;
        sono = 0;
        cacadas = 0;
        sonos = 0;
    }

    Personagem(int energia, int fome, int sono, int usuarioId, int sessaoId, LogAtividade logAtividade) {
        if (energia >= 0 && energia <= 10)
            this.energia = energia;
        if (fome >= 0 && fome <= 10)
            this.fome = fome;
        if (sono >= 0 && sono <= 10)
            this.sono = sono;
        this.usuarioId = usuarioId;
        this.sessaoId = sessaoId;
        this.logAtividade = logAtividade;
        this.cacadas = 0;
        this.sonos = 0;
    }

    void cacar(StringBuilder output) {
        if (energia >= 2) {
            System.out.printf("%s está caçando...\n", nome);
            output.append(nome).append(" está caçando...\n");
            energia -= 2;
            logAtividade.registrarCaca(usuarioId, sessaoId);
            logAtividade.logActivity(nome + " caçou e ganhou 2 pontos", usuarioId);
            cacadas++;
            System.out.println("Pontuação acrescida por caçar: +2 pontos");
            output.append("Pontuação acrescida por caçar: +2 pontos\n");
        } else {
            System.out.printf("%s sem energia para caçar...\n", nome);
            output.append(nome).append(" sem energia para caçar...\n");
        }
        fome = Math.min(fome + 1, 10);
        sono = sono < 10 ? sono + 1 : sono;
    }

    void comer(StringBuilder output) {
        switch (fome) {
            case 0:
                System.out.printf("%s sem fome...\n", nome);
                output.append(nome).append(" sem fome...\n");
                break;
            default:
                System.out.printf("%s comendo...\n", nome);
                output.append(nome).append(" comendo...\n");
                --fome;
                energia = (energia == 10 ? energia : energia + 1);
                logAtividade.logActivity(nome + " comeu e recuperou energia", usuarioId);
        }
    }

    void dormir(StringBuilder output) {
        if (sono >= 1) {
            System.out.printf("%s está dormindo...\n", nome);
            output.append(nome).append(" está dormindo...\n");
            sono -= 1;
            energia = Math.min(energia + 1, 10);
            logAtividade.registrarSono(usuarioId, sessaoId);
            logAtividade.logActivity(nome + " dormiu e perdeu 1 ponto", usuarioId);
            sonos++;
            System.out.println("Pontuação decrescida por dormir: -1 ponto");
            output.append("Pontuação decrescida por dormir: -1 ponto\n");
        } else {
            System.out.printf("%s sem sono...\n", nome);
            output.append(nome).append(" sem sono...\n");
        }
    }

    public String toString() {
        return String.format(
            "%s: (e:%d, f:%d, s:%d)",
            nome, energia, fome, sono
        );
    }

    public boolean nomear() {
        this.nome = JOptionPane.showInputDialog("Digite o nome do personagem:");
        if (nome == null) {
            return false;
        }
        if (nome.trim().isEmpty()) {
            nome = "Personagem Sem Nome";
        }
        logAtividade.logActivity("Personagem nomeado: " + nome, usuarioId);
        return true;
    }

    public void jogar(Random gerador) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int acao = gerador.nextInt(3);
            switch (acao) {
                case 0:
                    cacar(output);
                    break;
                case 1:
                    comer(output);
                    break;
                case 2:
                    dormir(output);
                    break;
            }
        }
        JOptionPane.showMessageDialog(null, output.toString());
        int pontuacaoFinal = logAtividade.calcularPontuacaoFinal(usuarioId, sessaoId);
        System.out.println("Pontuação final: " + pontuacaoFinal);
        logAtividade.logActivity(nome + " finalizou o jogo com pontuação final: " + pontuacaoFinal, usuarioId);
    }

    public String getStatus() {
        return String.format("Nome: %s\nEnergia: %d\nCaçadas: %d\nDormidas: %d\n", nome, energia, cacadas, sonos);
    }
}
