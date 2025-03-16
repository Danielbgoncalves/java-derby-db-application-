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
    public static void main(String[] args) throws SQLException {

        /// -------------- Pra tentar deixar o Swing menos feio --------------
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Não foi possível aplicar estilização\n");
            e.printStackTrace();
        }

        /*
        *
        * todo parece ter conectado legal ao derby
        *   falta fazer a compra e a venda ligarem ao ProdutoDAO -> parece ok
        *   precisa fazer o chamdo de Estoque atualizar a listagem, chat deu uma ideia boa. ->  deu certo
        *   esta sendo possível vender mais produtos do q o q existe em estoque -> resolvi
        *   esta sendo possível vender sem o id existir -> parece q consertei
        *      * ha produtos com quantidade negativa, remover eles
        *
        *
        *
        * */


        /// -------------- Conexão com o derby --------------
        DBConnection dbConnection= new DBConnection("org.apache.derby.jdbc.EmbeddedDriver");
        Connection cnn = dbConnection.CreateAndTestConnection(false);

        ProdutoDAO produtoDAO = new ProdutoDAO(cnn);
        produtoDAO.createProductTable();

        dbConnection.testeConnection(cnn);

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
            PainelComprar painelComprar = new PainelComprar(cl,cards,produtoDAO);

            /// -------------- Painel de Vender --------------
            PainelVender painelVender = new PainelVender(cl,cards, produtoDAO);

            /// -------------- Painel de Estoque --------------
            PainelEstoque painelEstoque = new PainelEstoque(cl,cards,produtoDAO);

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
