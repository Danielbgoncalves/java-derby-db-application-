package ui;

import dao.ProdutoDAO;
import db.*;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class PainelComprar extends JPanel{
    private ProdutoDAO produtoDAO;

    private JLabel painelLabel = new JLabel("Compre Produtos na OLX JAVAlino");
    private JLabel Nome = new JLabel("Nome");
    private JLabel Preco = new JLabel("Preço");
    private JLabel Descricao = new JLabel("Descricao");
    private JLabel Qnt = new JLabel("Qantidade");

    private JTextField entradaNome = new JTextField();
    private JTextField entradaPreco = new JTextField();
    private JTextField entradaDescricao = new JTextField();
    private JTextField entradaQnt = new JTextField();

    private JButton btnComprar = new JButton("Comprar");
    private JButton btnVendas = new JButton("Ver aba de vendas");
    private JButton btnEstoque = new JButton("Ver seu estoque");

    public PainelComprar(CardLayout cl, JPanel cards, ProdutoDAO produtoDAO){
        this.produtoDAO = produtoDAO;
        setLayout(null);

        painelLabel.setBounds(100,10,400,25);
        painelLabel.setFont(new Font("Arial", Font.BOLD, 17));

        Nome.setBounds(30, 50, 100, 30);
        Preco.setBounds(30, 80, 100, 30);
        Descricao.setBounds(30, 110, 100, 30);
        Qnt.setBounds(30, 140, 100, 30);

        entradaNome.setBounds(130, 50, 200, 30);
        entradaPreco.setBounds(130, 80, 200, 30);
        entradaDescricao.setBounds(130, 110, 390, 30);
        entradaQnt.setBounds(130, 140, 60, 30);

        btnComprar.setBounds(50, 200, 100, 30);
        btnVendas.setBounds(150, 200, 150, 30);
        btnEstoque.setBounds(300, 200, 150, 30);

        btnComprar.addActionListener(e -> comprarProduto());
        btnVendas.addActionListener(e -> {
            limparCampos();
            cl.show(cards, "Vender");
        });
        btnEstoque.addActionListener(e -> {
            limparCampos();

            cl.show(cards, "Estoque");
            Component[] componentes = cards.getComponents();

            for(Component c: componentes){
                if(c instanceof PainelEstoque){
                    ((PainelEstoque) c).getNewerList();
                }
            }
        });

        add(painelLabel);
        add(Nome);
        add(Preco);
        add(Descricao);
        add(Qnt);
        add(entradaNome);
        add(entradaPreco);
        add(entradaDescricao);
        add(entradaQnt);
        add(btnComprar);
        add(btnVendas);
        add(btnEstoque);
    }

    private void comprarProduto(){
        String nome = entradaNome.getText();
        String descricao = entradaDescricao.getText();

        try{
            double preco = Double.parseDouble(entradaPreco.getText()) ;
            int quantidade = Integer.parseInt(entradaQnt.getText());

            Produto p = new Produto(nome, preco, descricao,quantidade);
            produtoDAO.insertProduct(p);
        } catch (NumberFormatException e) {
            System.out.println("Tipo de dado inválido");
            // fazer função pra mostra pop up de erro tipo nnumerico esperado
            return;
        } catch (SQLException e) {
            System.out.println("Erro no SQL");
            // fazer função pra mostra pop up de erro no SQL
            return;
        }

        limparCampos();
        System.out.println("Comprado irmão!");
    }

    public void limparCampos(){
        entradaNome.setText("");
        entradaDescricao.setText("");
        entradaPreco.setText("") ;
        entradaQnt.setText("");
    }
}
