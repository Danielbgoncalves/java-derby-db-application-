package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    String drive;
    String dbName = "C:\\DB_derby\\OLX-JAVAlino";
    String connectionURL = "jdbc:derby:" + dbName+ ";create=true";

    public DBConnection(String drive){
        this.drive = drive;
    }

    private Connection createConnection(){
        Connection connection = null;
        // Inicia a conecção com o db aqui
        try{
            Class.forName(drive); // Serve para carregar o driver na memória, mas o que seria isso afinal ? Não sei
            connection = DriverManager.getConnection(connectionURL);
            System.out.println("Connected to database " + dbName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public Connection CreateAndTestConnection(boolean test) throws SQLException {
        Connection connection = createConnection();

        if(connection != null && test) testeConnection(connection);

        return connection;
    }

    public void testeConnection(Connection conn) throws SQLException {
        try{
            Statement s = conn.createStatement();
            s.execute("update Produto set descricao = 'Produto bom e barato' where 42 > 43");
            // O update nunca acontece de fato porque 42 > 43 é sempre falso mas serve pra testar se há algum erro na resposta
        } catch (SQLException sqle){
            String error = (sqle).getSQLState();

            if(error.equals("42X05")){ // tabela não existe
                System.out.println("Table does not exist");
                throw sqle;
            } else if (error.equals("42X14") || error.equals("42821")){ // outros problemas específicos
                System.out.println("Incorrect table definition. Drop tables and reconfigure");
                throw sqle;
            } else {
                System.out.println("Uhandled SQLException");
                throw sqle;
            }
        }
        System.out.println("Connection Tested - table exist");
    }

}
