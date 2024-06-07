import java.sql.Connection;
import java.sql.DriverManager;


public class ConnectionFactory {
  private static String host = "pg-38f1bace-henrique.f.aivencloud.com";
  private static String port = "13284";
  private static String db = "defaultdb";
  private static String user = "avnadmin";
  private static String password = "AVNS_wzSW9U_S0izbbG02RAw";
  //https://google.com:80/search
  //string de conexão
  //jdbc:postgresql://localhost:5432/20241_fatec_ipi_poo_pessoas
  public static Connection getConnection() 
                              throws Exception{
    //cláusula catch or declare
      var s = String.format(
        "jdbc:postgresql://%s:%s/%s",
        host, port, db
      );
      Connection conexao = DriverManager.getConnection(s, user, password);
      return conexao;
  }
}
