package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Produto;

public class ProdutoDAO { // DAO = Data Access Object 👌
        private Connection connection;

        public ProdutoDAO(Connection connection){
            this.connection = connection;
        }

        public void createProductTable(){
            String sql = "CREATE TABLE Produto(" +
                    "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                    "nome VARCHAR(100) UNIQUE," +
                    "preco DECIMAL(10,2)," +
                    "descricao VARCHAR(255), " +
                    "quantidade INT)";

            try(Statement stmt = connection.createStatement()){
                stmt.executeUpdate(sql); // métod que não retorna dados, para CREATE TABLE, INSRT, UPDATE, DELETE
                System.out.println("Tabela Porduto criada com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao criar tabela Produto: " + e.getMessage());
            }
        }

        public void insertProduct(Produto produto) throws SQLException{
            String insertSql = "INSERT INTO Produto (nome, preco, descricao, quantidade) VALUES (?, ?, ?, ?)";
            String checkSql = "SELECT quantidade FROM Produto WHERE nome = ?";
            String updateSql = "UPDATE Produto SET quantidade = quantidade + ? WHERE nome = ?";

            // Vamos, antes de tudo, ver se o produto já existe, para isso, verificaremos o nome
            try(PreparedStatement checkStmt = connection.prepareStatement(checkSql)){
                checkStmt.setString(1,produto.getNome());
                ResultSet rs = checkStmt.executeQuery();

                // Se o resultado não é vazio é porque já há esse produto
                if(rs.next()){
                    try(PreparedStatement updateStmt = connection.prepareStatement(updateSql)){
                        updateStmt.setInt(1, produto.getQuantidade());
                        updateStmt.setString(2, produto.getNome());
                        updateStmt.executeUpdate();
                        System.out.println("Quantidade do produto atualizada.");
                    }
                } else { // Se o produto não existe ainda, adicione
                    try(PreparedStatement pstmt = connection.prepareStatement((insertSql))){ // preparedStatement é bem seguro pelo visto
                        pstmt.setString(1,produto.getNome()); // Atualiza o código SQL
                        pstmt.setDouble(2,produto.getPreco());
                        pstmt.setString(3, produto.getDescricao());
                        pstmt.setInt(4, produto.getQuantidade());
                        pstmt.executeUpdate(); // Executa o código no banco de dados
                    }
                }
            } catch(SQLException e){
                System.out.println("Erro ao inserir produto: " + e.getMessage());
                throw e;
            }
        }

        public ArrayList<Produto> listProducts() throws SQLException {
            ArrayList<Produto> lista = new ArrayList<>();
            String sql = "SELECT * FROM Produto";

            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        Produto p = new Produto(
                                rs.getInt("id"),
                                rs.getString("nome"),
                                rs.getDouble("preco"),
                                rs.getString("descricao"),
                                rs.getInt("quantidade")
                        );
                        lista.add(p);
                    }
            }catch(SQLException e){
                throw e;
            }

            return lista;
        }

        public void sellProduct(int id, int qnt) throws SQLException {
            String checkSql = "SELECT quantidade FROM Produto WHERE id = ?";
            String updateSql = "UPDATE Produto SET quantidade = ? WHERE id = ?";

            try(PreparedStatement checkStmt = connection.prepareStatement(checkSql)){
                checkStmt.setInt(1,id);
                try(ResultSet rs = checkStmt.executeQuery()) {

                    // Se existe algo na resposta é porque essa linha existe
                    if (rs.next()) {
                        int quantidadeAtual = rs.getInt("quantidade");

                        if ((quantidadeAtual - qnt) == 0) {
                            deleteProduct(id);
                        } else if((quantidadeAtual - qnt) > 0){
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                                updateStmt.setInt(1, quantidadeAtual - qnt);
                                updateStmt.setInt(2, id);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            throw new SQLException("Produto com ID " + id + " não possui unidades o suficiente em estoque\n" +
                                    "Venda cancelada por completo.");
                        }
                    } else {
                        throw new SQLException("Produto com ID " + id + " não encontrado.");
                    }
                }
            } catch (SQLException e) {
                throw e;
            }

        }

        public void deleteProduct(int id){
            String sql = "DELETE FROM Produto WHERE id = ?";

            try(PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1,id);
                stmt.executeUpdate();
                System.out.println("Produto deletado com sucesso");
            } catch (SQLException e){
                System.out.println("Erro ao deltar produto: " + e.getMessage());
            }
        }
}
