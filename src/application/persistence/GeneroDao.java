package application.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.model.Genero;

public class GeneroDao implements ICrud<Genero>, ICrudListavel<Genero>, IExcluivel<Genero>, ICrudUnique<Genero>{
	
	private GenericDao gDao;
	
	public GeneroDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Genero g) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO Genero VALUES (?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, g.getNome());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void alterar (Genero g) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE Genero SET nome = ? WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, g.getNome());
		ps.setInt(2, g.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void excluir(Genero g) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM Genero WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, g.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public Genero pesquisar(Genero g) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome FROM GENERO WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, g.getId());
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			g.setId(rs.getInt("id"));
			g.setNome(rs.getString("nome"));
		}
		rs.close();
		ps.close();
		c.close();
		return g;
	}

	@Override
	public List<Genero> listar() throws SQLException, ClassNotFoundException {
		List<Genero> generos = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome FROM Genero";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Genero g = new Genero();
			g.setId(rs.getInt("id"));
			g.setNome(rs.getString("nome"));
			generos.add(g);
		}
		rs.close();
		ps.close();
		c.close();
		return generos;
	}

	@Override
	public boolean estaEmUso(Genero g) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) as tem FROM Jogo_Genero WHERE idGenero = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, g.getId());
		ResultSet rs = ps.executeQuery();
		int tem = 0;	
	    if(rs.next()){
	    	tem = rs.getInt("tem");
	    }
	    rs.close();
	    ps.close();
	    c.close();
		
	    if(tem > 0) {
	    	return true;
	    } 
	    return false;
	}

	@Override
	public void excluirEmUso(Genero g) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM Jogo_Genero WHERE idGenero = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, g.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public boolean verificarUnique(String campo, String unique, int id) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) AS qtd FROM Genero WHERE " + campo + " = ? AND id <> ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, unique);
		ps.setInt(2, id);
		ResultSet rs = ps.executeQuery();
	    int total = 0;	
	    if(rs.next()){
	    	total = rs.getInt("qtd");
	    }
	    rs.close();
	    ps.close();
	    c.close();
		
	    if(total > 0) {
	    	return true;
	    } 
	    return false;
	}
	
}
