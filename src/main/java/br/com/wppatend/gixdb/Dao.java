package br.com.wppatend.gixdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.wppatend.vos.ProdutoCK;

public class Dao {
	
	public ProdutoCK loadProduct(Integer cod) throws SQLException, ClassNotFoundException {
		String sql = 
				"SELECT " + 
						"arqprod.prodcodi, " + 
						"arqprod.proddesc, " + 
						"arqtabp.tabpcodi, " + 
						"arqtabp.tabpprec, " + 
						"arqtabp.tabpprod " + 
				"FROM " + 
					"arqprod, " + 
					"arqtabp " + 
				"WHERE " + 
					"arqprod.prodcodi = arqtabp.tabpprod " + 
					"AND arqprod.prodcodi = ? " + 
					"and arqtabp.tabpcodi = 'P'";
		
		DBConnection dbConn = new DBConnection();
		ProdutoCK prod = null;
		Connection conn = dbConn.getConnection();
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, cod);
		
		ResultSet rs = stm.executeQuery();
		if(rs.next()) {
			prod = new ProdutoCK();
			prod.setProdcod(rs.getInt(1));
			prod.setProddesc(rs.getString(2));
			prod.setTabpcodi(rs.getString(3));
			prod.setTabpprec(rs.getDouble(4));
			prod.setTabpprod(rs.getInt(5));
		}
		
		rs.close();
		stm.close();
		dbConn.closeConnection(conn);
		
		return prod;
		
	}

}
