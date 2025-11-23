package application.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.model.Biblioteca;
import application.model.Jogo;
import application.model.Usuario;

public class BibliotecaDao implements ICud <Biblioteca>, ICrudListavel<Biblioteca> {
	
	private GenericDao gDao;
	
	public BibliotecaDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Biblioteca b) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO Biblioteca VALUES (?, ?, ?, ?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, b.getUsuario().getId());
		ps.setInt(2, b.getJogo().getId());
		ps.setDate(3, java.sql.Date.valueOf(b.getDataCompra()));
		ps.setInt(4, b.getHorasJogadas());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void alterar(Biblioteca b) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE Biblioteca SET dataCompra = ?, horasJogadas = ? WHERE idUsuario = ? and idJogo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setDate(1, java.sql.Date.valueOf(b.getDataCompra()));
		ps.setInt(2, b.getHorasJogadas());
		ps.setInt(3, b.getUsuario().getId());
		ps.setInt(4, b.getJogo().getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void excluir(Biblioteca b) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM Biblioteca WHERE idUsuario = ? and idJogo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, b.getUsuario().getId());
		ps.setInt(2, b.getJogo().getId()); 
		ps.execute();
	    ps.close();
	    c.close();
	}
	
	public List<Biblioteca> filtrarPorUsuario(Biblioteca b) throws SQLException, ClassNotFoundException {
		List<Biblioteca> lista = new ArrayList<>();
	    Connection c = gDao.getConnection();
	    String sql = " SELECT u.username, j.nome AS nomeJogo, b.horasJogadas FROM Biblioteca b INNER JOIN Jogo j " +
	    		"ON b.idJogo = j.ID INNER JOIN Usuario u " + 
	    		" ON b.idUsuario = u.ID WHERE b.idUsuario = ? ORDER BY nomeJogo  ";
	 	PreparedStatement ps = c.prepareStatement(sql);
	 	ps.setInt(1, b.getUsuario().getId());
	 	ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
	    	 Biblioteca bi = new Biblioteca();
	         Usuario u = new Usuario();
	         u.setId(b.getUsuario().getId());
	         u.setUsername(rs.getString("username"));
	         bi.setUsuario(u);
	         Jogo j = new Jogo();
	         j.setNome(rs.getString("nomeJogo"));
	         bi.setJogo(j);
	         bi.setHorasJogadas(rs.getInt("horasJogadas"));
	         lista.add(bi);
	    }
	    rs.close();
	    ps.close();
	    c.close();
	    return lista;
	}
	
	public List<Biblioteca> listarPorJogo(Biblioteca b) throws SQLException, ClassNotFoundException {
		List<Biblioteca> lista = new ArrayList<>();
		Connection c = gDao.getConnection();
	    String sql = " SELECT u.username, j.nome as nomeJogo, b.horasJogadas " +
	        " FROM Biblioteca b INNER JOIN Usuario u ON b.idUsuario = u.ID INNER JOIN Jogo j ON b.idJogo = j.ID " +
	        "WHERE b.idJogo = ? ORDER BY u.username ";
	    PreparedStatement ps = c.prepareStatement(sql);
	    ps.setInt(1, b.getJogo().getId());
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
	        Biblioteca bi = new Biblioteca();
	        Usuario u = new Usuario();
	        u.setUsername(rs.getString("username"));
	        bi.setUsuario(u);
	        Jogo j = new Jogo();
	        j.setId(b.getJogo().getId());
	        j.setNome(rs.getString("nomeJogo"));
	        bi.setJogo(j);
	        bi.setHorasJogadas(rs.getInt("horasJogadas"));
	        lista.add(bi);
	    }
	    rs.close();
	    ps.close();
	    c.close();
	    return lista;
	}
	
	public List<Biblioteca> jogosCompradosDtLan() throws SQLException, ClassNotFoundException {
		List<Biblioteca> lista = new ArrayList<>();
	    Connection c = gDao.getConnection();
	    String sql = " SELECT u.username, j.nome as nomeJogo, j.dataLancamento, b.dataCompra " +
	        "FROM Biblioteca b INNER JOIN Jogo j ON b.idJogo = j.ID INNER JOIN Usuario u ON b.idUsuario = u.ID  "
	        + "WHERE b.datacompra = j.dataLancamento ORDER BY j.dataLancamento ASC ";
	    PreparedStatement ps = c.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
	    	Biblioteca bi = new Biblioteca();
	        Usuario u = new Usuario();
	        u.setUsername(rs.getString("username"));
	        bi.setUsuario(u);
	        Jogo j = new Jogo();
	        j.setNome(rs.getString("nomeJogo"));
	        j.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());
	        bi.setJogo(j);
	        bi.setDataCompra(rs.getDate("dataCompra").toLocalDate());
	        lista.add(bi);
	    }
	    rs.close();
	    ps.close();
	    c.close();
	    return lista;
	}
	
	public int totalJogosUsuario(Biblioteca b) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
	    String sql = "SELECT COUNT(*) AS total FROM Biblioteca WHERE idUsuario = ?";
	    PreparedStatement ps = c.prepareStatement(sql);
	    ps.setInt(1, b.getUsuario().getId());
	    ResultSet rs = ps.executeQuery();
	    int total = 0;
	    if (rs.next()) {
	        total = rs.getInt("total");
	    }
	    rs.close();
	    ps.close();
	    c.close();
	    return total;
	}

	@Override
	public List<Biblioteca> listar() throws SQLException, ClassNotFoundException {
		List<Biblioteca> biblios = new ArrayList<>();
		Connection c = gDao.getConnection();
		UsuarioDao uDao = new UsuarioDao(gDao);
		JogoDao jDao = new JogoDao(gDao);
		String sql = "SELECT idUsuario, idJogo, dataCompra, horasJogadas FROM Biblioteca";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Biblioteca b = new Biblioteca();
			int idU = rs.getInt("idUsuario");
			int idJ = rs.getInt("idJogo");
			Usuario u = new Usuario();
			u.setId(idU);
			Jogo j = new Jogo();
			j.setId(idJ);
			u = uDao.pesquisar(u);
			j = jDao.pesquisar(j);
			b.setUsuario(u);
			b.setJogo(j);
			b.setHorasJogadas(rs.getInt("horasJogadas"));
			b.setDataCompra(rs.getDate("dataCompra").toLocalDate());
			biblios.add(b);
		}
		return biblios;
		
	}
}
