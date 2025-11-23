package application.persistence;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.model.Empresa;
import application.model.Genero;
import application.model.Jogo;

public class JogoDao implements ICrud<Jogo>, ICrudListavel<Jogo>, IExcluivel<Jogo> {

	private GenericDao gDao;
	
	public JogoDao(GenericDao gDao) {
		this.gDao = gDao;
	}
	@Override
	public void inserir(Jogo j) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO Jogo VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		BigDecimal valor = BigDecimal.valueOf(j.getPreco());
		ps.setString(1, j.getNome());
		ps.setBigDecimal(2, valor);
		ps.setDate(3, java.sql.Date.valueOf(j.getDataLancamento()));
		ps.setInt(4, j.getPublicadora().getId());
		ps.setInt(5, j.getDesenvolvedora().getId());
		ps.execute();
		ResultSet rs = ps.getGeneratedKeys();
		int id = 0;
		if(rs.next()) {
			id = rs.getInt(1);
		}
		ps.close();
		inserirAssociativa(id, j.getGeneros(), c);
		c.close();
	}
	
	private void inserirAssociativa(int id, List<Genero> generos, Connection c) throws ClassNotFoundException, SQLException {
		String sqlAs = "INSERT INTO Jogo_Genero VALUES (?, ?)";
		PreparedStatement psAs = c.prepareStatement(sqlAs);
		for(Genero g : generos) {
			psAs.setInt(1, id);
			psAs.setInt(2, g.getId());
			psAs.execute();
		}
		psAs.close();
	}
	@Override
	public void alterar(Jogo j) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE Jogo SET nome=?, preco=?, "
				+ "dataLancamento=?, idPublicadora=?, idDesenvolvedora=? WHERE id=?" ;
		
	        PreparedStatement ps = c.prepareStatement(sql);
	        ps.setString(1, j.getNome());
	        ps.setBigDecimal(2, BigDecimal.valueOf(j.getPreco()));
	        ps.setDate(3, java.sql.Date.valueOf(j.getDataLancamento()));
	        ps.setInt(4, j.getPublicadora().getId());
	        ps.setInt(5, j.getDesenvolvedora().getId());
	        ps.setInt(6, j.getId());
	        ps.executeUpdate();
	        ps.close();
	        excluirGeneroJogo(j, c);
	        inserirAssociativa(j.getId(), j.getGeneros(), c);
	        c.close();
	}
	@Override
	public void excluir(Jogo j) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		excluirGeneroJogo(j, c);
		String sql = "DELETE FROM Jogo WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, j.getId());
		ps.execute();
		ps.close();
		c.close();
	}
	
	private void excluirGeneroJogo(Jogo j, Connection c) throws SQLException, ClassNotFoundException {
		String sql = "DELETE FROM Jogo_Genero WHERE idJogo = ?";
        PreparedStatement ps= c.prepareStatement(sql);
        ps.setInt(1, j.getId());
        ps.executeUpdate();
        ps.close();
	}
	
	public List<Jogo> filtrarPorGeneros(List<Genero> generos) 
	        throws SQLException, ClassNotFoundException {
	    Connection c = gDao.getConnection();
	    StringBuffer interroga = new StringBuffer();
	    for (int i = 0; i < generos.size(); i++) {
	        interroga.append("?");
	        if (i < generos.size() - 1) {
	        	interroga.append(",");
	        }
	    }

	    String sql =  "SELECT j.id, j.nome, j.preco, j.dataLancamento, j.idPublicadora, j.idDesenvolvedora " +
	    	    "FROM Jogo j JOIN Jogo_Genero jg ON j.id = jg.idJogo WHERE jg.idGenero IN "+ 
	    	    "(" + interroga + ")" + " GROUP BY j.id, j.nome, j.preco, j.dataLancamento, " +
	    	    "j.idPublicadora, j.idDesenvolvedora HAVING COUNT(DISTINCT jg.idGenero) = ?";
	    PreparedStatement ps = c.prepareStatement(sql);
	    int i = 1;
	    for (Genero g : generos) {
	        ps.setInt(i++, g.getId());
	    }
	    ps.setInt(i, generos.size());
	    ResultSet rs = ps.executeQuery();
	    List<Jogo> jogos = new ArrayList<>();
	    EmpresaDao eDao = new EmpresaDao(gDao);
	    while (rs.next()) {
	    	Jogo j = new Jogo();
	        j.setId(rs.getInt("id"));
	        j.setNome(rs.getString("nome"));
	        j.setPreco(rs.getBigDecimal("preco").doubleValue());
	        j.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());
	        int idPub = rs.getInt("idPublicadora");
	        int idDev = rs.getInt("idDesenvolvedora");
	        Empresa pub = new Empresa();
	        Empresa dev = new Empresa();
	        pub.setId(idPub);
	        dev.setId(idDev);
	        j.setPublicadora(eDao.pesquisar(pub));
	        j.setDesenvolvedora(eDao.pesquisar(dev));
	        j.setGeneros(pesquisarGenerosJogo(j.getId(), c));
	        jogos.add(j);
	    }
	    rs.close();
	    ps.close();
	    c.close();
	    return jogos;
	}
	public Jogo pesquisar(Jogo j) throws SQLException, ClassNotFoundException  {
		 Connection c = gDao.getConnection();
		    String sql = "SELECT id, nome, preco, dataLancamento, idPublicadora, idDesenvolvedora "
		               + "FROM Jogo WHERE id = ?";
		    PreparedStatement ps = c.prepareStatement(sql);
		    ps.setInt(1, j.getId());
		    ResultSet rs = ps.executeQuery();
		    if (rs.next()) {
		        j.setId(rs.getInt("id"));
		        j.setNome(rs.getString("nome"));
		        j.setPreco(rs.getBigDecimal("preco").doubleValue());
		        j.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());
		        int idPub = rs.getInt("idPublicadora");
		        int idDev = rs.getInt("idDesenvolvedora");
		        Empresa pub = new Empresa();
		        Empresa dev = new Empresa();
		        pub.setId(idPub);
		        dev.setId(idDev);
		        EmpresaDao eDao = new EmpresaDao(gDao);
		        j.setPublicadora(eDao.pesquisar(pub));
		        j.setDesenvolvedora(eDao.pesquisar(dev));
		        j.setGeneros(pesquisarGenerosJogo(j.getId(), c));
		    }
		    rs.close();
		    ps.close();
		    c.close();
		    return j;
	}
	
	private List<Genero> pesquisarGenerosJogo(int idJogo, Connection c) 
	        throws SQLException, ClassNotFoundException {
		String sql = "SELECT g.id, g.nome FROM Genero g INNER JOIN Jogo_Genero jg ON g.id = jg.idGenero "
		           + "WHERE jg.idJogo = ?";
	    PreparedStatement ps = c.prepareStatement(sql);
	    ps.setInt(1, idJogo);
	    ResultSet rs = ps.executeQuery();
	    List<Genero> generos = new ArrayList<>();
	    while (rs.next()) {
	        Genero g = new Genero();
	        g.setId(rs.getInt("id"));
	        g.setNome(rs.getString("nome"));
	        generos.add(g);
	    }
	    rs.close();
	    ps.close();
	    return generos;
	}
	
	@Override
	public List<Jogo> listar() throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
	    String sql = "SELECT id, nome, preco, dataLancamento, idPublicadora, idDesenvolvedora FROM Jogo";
	    PreparedStatement ps = c.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    List<Jogo> jogos = new ArrayList<>();
	    EmpresaDao eDao = new EmpresaDao(gDao);
	    while (rs.next()) {
	        Jogo j = new Jogo();
	        j.setId(rs.getInt("id"));
	        j.setNome(rs.getString("nome"));
	        j.setPreco(rs.getBigDecimal("preco").doubleValue());
	        j.setDataLancamento(rs.getDate("dataLancamento").toLocalDate());
	        int idPub = rs.getInt("idPublicadora");
	        int idDev = rs.getInt("idDesenvolvedora");
	        Empresa pub = new Empresa();
	        Empresa dev = new Empresa();
	        pub.setId(idPub);
	        dev.setId(idDev);
	        j.setPublicadora(eDao.pesquisar(pub));
	        j.setDesenvolvedora(eDao.pesquisar(dev));
	        j.setGeneros(pesquisarGenerosJogo(j.getId(), c));
	        jogos.add(j);
	    }
	    rs.close();
	    ps.close();
	    c.close();
	    return jogos;
	}
	
	@Override
	public boolean estaEmUso(Jogo j) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) AS tem FROM Biblioteca WHERE idJogo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, j.getId());
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
	public void excluirEmUso(Jogo j) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM Biblioteca WHERE idJogo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, j.getId());
		ps.execute();
		ps.close();
		c.close();
	}
}
