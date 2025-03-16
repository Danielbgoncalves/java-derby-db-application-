import model.*;
import ui.*;
import dao.*;
import db.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        /// -------------- Pra tentar deixar o Swing menos feio --------------
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Não foi possível aplicar estilização\n");
            e.printStackTrace();
        }

        /// -------------- Conexão com o derby --------------
        DBConnection dbConnection= new DBConnection("org.apache.derby.jdbc.EmbeddedDriver");
        Connection cnn = null;
        try{
             cnn = dbConnection.CreateAndTestConnection(false);
        } catch (SQLException e) {
            System.out.println( e.getMessage());
            return;
        }

        ProdutoDAO produtoDAO = new ProdutoDAO(cnn);
        produtoDAO.createProductTable();

        try {
            dbConnection.testeConnection(cnn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        /// -------------- Ligação entre componentes do app --------------

        SwingUtilities.invokeLater(() -> {
            ArrayList<Produto> produtosListados = new ArrayList<>();

            /// -------------- Cria janela da aplicação --------------
            JFrame mainFrame = new JFrame("OLX do JAVAlino");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(550, 300);
            mainFrame.setLayout(new BorderLayout());

            /// -------------- Painel principal para organizar várias telas --------------
            JPanel cards = new JPanel(new CardLayout());

            /// -------------- Configuração dos cards --------------
            CardLayout cl = (CardLayout) cards.getLayout();

            /// -------------- Painel de Compras --------------
            PainelComprar painelComprar = new PainelComprar(cl,cards,produtoDAO,mainFrame);

            /// -------------- Painel de Vender --------------
            PainelVender painelVender = new PainelVender(cl,cards, produtoDAO,mainFrame);

            /// -------------- Painel de Estoque --------------
            PainelEstoque painelEstoque = new PainelEstoque(cl,cards,produtoDAO, mainFrame);

            /// -------------- Configuração dos cards --------------
            cards.add(painelComprar, "Comprar");
            cards.add(painelVender, "Vender" );
            cards.add(painelEstoque, "Estoque" );

            /// -------------- Configuração da janela --------------
            mainFrame.add(cards, BorderLayout.CENTER);
            mainFrame.setVisible(true);

            /// -------------- Imagem da aplicação --------------
            Image icon = Toolkit.getDefaultToolkit().getImage("assets/icon2.png");
            mainFrame.setIconImage(icon);
        });

    }
}
