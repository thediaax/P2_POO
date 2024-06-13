public class Personagem {
  String nome;
  int energia;
  private int fome;
  private int sono;
  private int usuarioId;
  private int sessaoId;

  private LogAtividade logAtividade;

  // Construtor padrão
  Personagem() {
      nome = null;
      energia = 10;
      fome = 0;
      sono = 0;
  }

  // Construtor personalizado
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
  }

  void cacar(StringBuilder output) {
      if (energia >= 2) {
          System.out.printf("%s está caçando...\n", nome);
          output.append(nome).append(" está caçando...\n");
          energia -= 2;
          logAtividade.registrarCaca(usuarioId, sessaoId);
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
      }
  }

  void dormir(StringBuilder output) {
      if (sono >= 1) {
          System.out.printf("%s está dormindo...\n", nome);
          output.append(nome).append(" está dormindo...\n");
          sono -= 1;
          energia = Math.min(energia + 1, 10);
          logAtividade.registrarSono(usuarioId, sessaoId);
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
}