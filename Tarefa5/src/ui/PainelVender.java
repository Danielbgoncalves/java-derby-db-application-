package ui;

import dao.ProdutoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class PainelVender extends JPanel {
    private ProdutoDAO produtoDAO;

    private JLabel painelLabel = new JLabel("Venda Produtos na OLX JAVAlino");
    private JLabel idVenda = new JLabel("Id do produto a ser vendido");
    private JLabel qntVenda = new JLabel("Quantas unidades vendidas");

    private JTextField entradaId = new JTextField();
    private JTextField entradaQnt = new JTextField();

    private JButton btnVender = new JButton("Vender");
    private JButton btnCompras = new JButton("Ver aba de compras");
    private JButton btnEstoque = new JButton("Ver seu estoque");


    public PainelVender(CardLayout cl, JPanel cards, ProdutoDAO produtoDAO ){
        this.produtoDAO = produtoDAO;
        setLayout(null);

        painelLabel.setBounds(100,10,400,25);
        painelLabel.setFont(new Font("Arial", Font.BOLD, 17));

        idVenda.setBounds(30, 50, 150, 30);
        qntVenda.setBounds(30, 120, 200, 30);

        entradaId.setBounds(30, 80, 200, 30);
        entradaQnt.setBounds(30, 150, 200, 30);

        btnVender.setBounds(50, 200, 100, 30);
        btnCompras.setBounds(150, 200, 150, 30);
        btnEstoque.setBounds(300, 200, 150, 30);

        btnVender.addActionListener(e -> venderProduto());
        btnCompras.addActionListener(e -> {
            limparCampos();
            cl.show(cards,"Comprar");
        });
        btnEstoque.addActionListener(e ->{
            limparCampos();

            cl.show(cards,"Estoque");
            Component[] componentes = cards.getComponents();

            for(Component c: componentes){
                if(c instanceof PainelEstoque){
                    ((PainelEstoque) c).getNewerList();
                }
            }
        });

        add(painelLabel);
        add(idVenda);
        add(qntVenda);
        add(entradaId);
        add(entradaQnt);
        add(btnCompras);
        add(btnVender);
        add(btnEstoque);

    }

    private void venderProduto(){
        int id = -1, qnt = -1;

        try{
            id = Integer.parseInt(entradaId.getText());
            qnt = Integer.parseInt(entradaQnt.getText());
        } catch (NumberFormatException e) {
            System.out.println("Não foi possível converter o text em inteiro");
            // fazer função pra mostrar erro de conversao inesperada (meio q é esperada pq eu estou escrevendo sobre ela aqui)
        }

        if(id < 1 || qnt < 1 ){
            System.out.println("Valores inválidos  de id ou qnt");
            // fazer função pra mostra pop up de erro no valor passado
            return;
        }

        try{
            produtoDAO.sellProduct(id,qnt);
            System.out.println("vendido");
        } catch (SQLException e) {
            System.out.println("Erro ao vender produto \nMensagem: " + e.getMessage());
            // fazer função pra mostra pop up de erro no SQL
            return;
        }

        limparCampos();
        System.out.println("Vendido parça!!!");
    }

    public void limparCampos(){
        entradaId.setText("");
        entradaQnt.setText("");
    }
}
