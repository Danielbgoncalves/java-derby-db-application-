package ui;

import dao.ProdutoDAO;
import dialogMessage.DialogMessageHelper;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PainelEstoque extends JPanel implements DialogMessageHelper {

    private JFrame mainFrame;
    private JTextArea txtArea;
    private ProdutoDAO produtoDAO;

    private JLabel painelLabel = new JLabel("Seu estoque na OLX JAVAlino", SwingConstants.CENTER);
    private JButton btnVoltar = new JButton("Voltar às compras");

    public PainelEstoque(CardLayout cl, JPanel cards, ProdutoDAO produtoDAO, JFrame mainFrame){
        this.mainFrame= mainFrame;
        this.produtoDAO = produtoDAO;
        setLayout(new BorderLayout());

        painelLabel.setFont(new Font("Arial", Font.BOLD, 17));

        txtArea = new JTextArea();
        txtArea.setEditable(false);
        JScrollPane scrollPane= new JScrollPane(txtArea);

        btnVoltar.addActionListener(e -> cl.show(cards, "Comprar"));

        add(painelLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnVoltar,BorderLayout.SOUTH);

        getNewerList();

    }

    public void getNewerList(){
        txtArea.setText("");

        ArrayList<Produto> list = new ArrayList<>();
        try{
            list = produtoDAO.listProducts();
        } catch( SQLException e){
            DialogMessageHelper.dialogMessgae(mainFrame,"Erro ao buscar informações do servidor", "erro");
            System.out.println("Erro ao buscar lista de Produto: " + e.getMessage());
            // função de pop up
        }

        if(list.isEmpty()){
            System.out.println("A lista veio vazia");
            txtArea.append("""
                    =================================================================
                    \t   No momento não há Produto no estoque,
                    \t   Compre algum na OLX JAVAlino e o veja no estoque !
                    \t   Dica: use o id mostrado aqui para te orientar na hora da venda
                    \t   （￣︶￣）↗　
                    =================================================================
                    """);
        } else{
            for(Produto p: list)
                txtArea.append(p.toString() + "\n");
        }


    }



}
