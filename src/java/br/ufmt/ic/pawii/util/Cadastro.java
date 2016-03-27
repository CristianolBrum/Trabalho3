package br.ufmt.ic.pawii.util;


import br.ufmt.ic.pawii.util.ConnBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Cadastro {
    public static String inserir(String nome, String cpf , String rg, String telefone) {
        try {
            PreparedStatement pstm;
            Connection con = ConnBD.getConnection();

            pstm = con.prepareStatement("INSERT INTO dados (nome,cpf,rg,telefone) VALUES (?, ?, ?, ?)");
            pstm.setString(1, nome);
            pstm.setString(2, cpf);
            pstm.setString(3, rg);
            pstm.setString(4, telefone);
            pstm.executeUpdate();
            
            System.out.println(nome);
            System.out.println(cpf);
            System.out.println(rg);
            System.out.println(telefone);
            
            JSONObject retorno = new JSONObject();
            retorno.put("sucesso", true);
            return retorno.toString();
        } catch (SQLException ex) {
            System.out.println("Error: "+ex.getMessage());
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
        
        return null;
    }
    
     public static String exibir(String buscar) {
        try {
            String strCliente;
            Statement stm;
            Connection con = ConnBD.getConnection();
            String sql;

            if (con == null) {
                throw new SQLException("Erro ao conectar.");
            }
            
            if(buscar.equals("")) {
                sql = "SELECT * FROM dados ORDER BY nome DESC";
            } else {
                sql = "SELECT * FROM dados WHERE nome LIKE '%"+buscar+"%' ORDER BY nome DESC";
            }

            stm = con.createStatement();
            
            ResultSet rs = stm.executeQuery(sql);
            
            JSONArray cliente = new JSONArray();
            //[]
            
            while(rs.next()) {
                String clienteID = rs.getString(1);
                String clienteNome = rs.getString(2);
                String clienteCPF = rs.getString(3);
                String clienteRg = rs.getString(4);
                String clienteTelefone = rs.getString(5);
                
                JSONObject jsonCliente = new JSONObject();
                jsonCliente.put("id", clienteID);
                jsonCliente.put("nome", clienteNome);
                jsonCliente.put("cpf", clienteCPF);
                jsonCliente.put("rg", clienteRg);
                jsonCliente.put("telefone", clienteTelefone);
                
                //{cpf: 123456789, nome: "Cliente 1"}
                
                cliente.put(jsonCliente);
                //[{cpf: 123456789, nome: "Cliente 1"}, {cpf: 987654321, nome: "Cliente 2"}]
            }
            
            JSONObject jsonRetorno = new JSONObject();
            jsonRetorno.put("clientes", cliente);
            
            //{cliente: [{cpf: 123456789, nome: "Cliente 1"}, {cpf: 987654321, nome: "Cliente 2"}]}

            strCliente = jsonRetorno.toString();

            return strCliente;

        } catch (SQLException ex) {
                System.out.println("Error: "+ex.getMessage());
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
       
        return null;
    }
    
    public static String excluir(String id) {
        try {
            Statement stm;
            Connection con = ConnBD.getConnection();

            if (con == null) {
                throw new SQLException("Erro ao conectar.");
            }

            stm = con.createStatement();
            stm.executeUpdate("DELETE FROM dados WHERE id = "+id);
            
            return null;

        } catch (SQLException ex) {
                System.out.println("Error: "+ex.getMessage());
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
       
        return null;
    }

}
