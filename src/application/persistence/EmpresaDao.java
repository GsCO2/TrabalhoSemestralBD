package application.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.model.Empresa;


public class EmpresaDao implements ICrud<Empresa>, ICrudFiltravel<Empresa>, ICrudListavel<Empresa>, IExcluivel<Empresa>{
	
	private GenericDao gDao;
	
	public EmpresaDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Empresa e) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO Empresa VALUES (?, ?, ?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, e.getNome());
		ps.setString(2, e.getPais());
		ps.setString(3, e.getTipo());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void alterar(Empresa e) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE Empresa SET nome = ?, pais = ?, tipo = ? WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, e.getNome());
		ps.setString(2, e.getPais());
		ps.setString(3, e.getTipo());
		ps.setInt(4, e.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void excluir(Empresa e) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM Empresa WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, e.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public Empresa pesquisar(Empresa e) throws SQLException, ClassNotFoundException {
			Connection c = gDao.getConnection();
			String sql = "SELECT id, nome, pais, tipo FROM Empresa WHERE id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, e.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				e.setId(rs.getInt("id"));
				e.setNome(rs.getString("nome"));
				e.setPais(rs.getString("pais"));
				e.setTipo(rs.getString("tipo"));
			}
			rs.close();
			ps.close();
			c.close();
			return e;
	}

	@Override
	public List<Empresa> listar() throws SQLException, ClassNotFoundException {
		List<Empresa> empresas = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, pais, tipo FROM Empresa";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Empresa e = new Empresa();
			e.setId(rs.getInt("id"));
			e.setNome(rs.getString("nome"));
			e.setPais(rs.getString("pais"));
			e.setTipo(rs.getString("tipo"));
			empresas.add(e);
		}
		rs.close();
		ps.close();
		c.close();
		return empresas;
	}


	@Override
	public List<Empresa> filtrar(String tipo) throws SQLException, ClassNotFoundException {
		List<Empresa> empresas = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, pais, tipo FROM Empresa WHERE tipo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, tipo);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Empresa e = new Empresa();
			e.setId(rs.getInt("id"));
			e.setNome(rs.getString("nome"));
			e.setPais(rs.getString("pais"));
			e.setTipo(rs.getString("tipo"));
			empresas.add(e);
		}
		rs.close();
		ps.close();
		c.close();
		return empresas;
	}
	
	@Override
	public boolean estaEmUso(Empresa e) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) AS total FROM Jogo WHERE idPublicadora = ? OR idDesenvolvedora = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, e.getId());
        ps.setInt(2, e.getId());
		ResultSet rs = ps.executeQuery();
		int tem = 0;	
	    if(rs.next()){
	    	tem = rs.getInt("total");
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
	public void excluirEmUso(Empresa e) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
        String sql = "SELECT ID FROM Jogo WHERE idPublicadora = ? OR idDesenvolvedora = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, e.getId());
        ps.setInt(2, e.getId());
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int idJogo = rs.getInt("ID");
            PreparedStatement psBib = c.prepareStatement("DELETE FROM Biblioteca WHERE idJogo = ?");
            psBib.setInt(1, idJogo);
            psBib.execute();
            psBib.close();
            
            PreparedStatement psJG = c.prepareStatement("DELETE FROM Jogo_Genero WHERE idJogo = ?");
            psJG.setInt(1, idJogo);
            psJG.execute();
            psJG.close();
       
            PreparedStatement psJ = c.prepareStatement("DELETE FROM Jogo WHERE ID = ?");
            psJ.setInt(1, idJogo);
            psJ.execute();
            psJ.close();
        }

        rs.close();
        ps.close();
        c.close();
    }
}



