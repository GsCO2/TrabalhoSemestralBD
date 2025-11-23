package application.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.model.Usuario;

public class UsuarioDao implements ICrud<Usuario>, ICrudContavel <Usuario>, ICrudListavel<Usuario>, ICrudUnique<Usuario>, IExcluivel<Usuario> {
	
	private GenericDao gDao;
	
	public UsuarioDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Usuario u) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO Usuario VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, u.getNome());
		ps.setString(2, u.getUsername());
		ps.setString(3, u.getEmail());
		ps.setString(4, u.getSenha());
		ps.setDate(5, java.sql.Date.valueOf(u.getData()));
		ps.setString(6, u.getNumCelular());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void alterar(Usuario u) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE Usuario SET nome = ?, username = ?, email = ?, senha = ?, dataCadastro = ?, "
				+ "numCelular = ? WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, u.getNome());
		ps.setString(2, u.getUsername());
		ps.setString(3, u.getEmail());
		ps.setString(4, u.getSenha());
		ps.setDate(5, java.sql.Date.valueOf(u.getData()));
		ps.setString(6, u.getNumCelular());
		ps.setInt(7, u.getId());
		ps.execute();
		ps.close();
		c.close();
		
	}

	@Override
	public void excluir(Usuario u) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM Usuario WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, u.getId());
		ps.execute();
		ps.close();
		c.close();
		
	}

	@Override
	public Usuario pesquisar(Usuario u) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, username, email, senha, CONVERT (VARCHAR(10), dataCadastro, 103) as dc , numCelular "
				+ "FROM Usuario WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, u.getId());
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			u.setId(rs.getInt("id"));
			u.setNome(rs.getString("nome"));
			u.setUsername(rs.getString("username"));
			u.setEmail(rs.getString("email"));
			u.setSenha(rs.getString("senha"));
			String data = rs.getString("dc");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dataTratada = LocalDate.parse(data, formatter);
			u.setData(dataTratada);
			u.setNumCelular(rs.getString("numCelular"));
		}
		rs.close();
		ps.close();
		c.close();
		return u;
	}

	@Override
	public List<Usuario> listar() throws SQLException, ClassNotFoundException {
		List<Usuario> usuarios = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, username, email, senha, "
				+ "CONVERT (VARCHAR(10), dataCadastro, 103) as dc, numCelular FROM Usuario ";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Usuario u = new Usuario();
			u.setId(rs.getInt("id"));
			u.setNome(rs.getString("nome"));
			u.setUsername(rs.getString("username"));
			u.setEmail(rs.getString("email"));
			u.setSenha(rs.getString("senha"));
			String data = rs.getString("dc");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dataTratada = LocalDate.parse(data, formatter);
			u.setData(dataTratada);
			u.setNumCelular(rs.getString("numCelular"));
			usuarios.add(u);
		}
		rs.close();
		ps.close();
		c.close();
		return usuarios;
	}

	@Override
	public int contarTotal() throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
	    String sql = "SELECT COUNT(*) AS qtd FROM Usuario";
	    PreparedStatement ps = c.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    int total = 0;	
	    if(rs.next()){
	    	total = rs.getInt("qtd");
	    }
	    rs.close();
	    ps.close();
	    c.close();
		return total;
	}
	@Override
	public boolean verificarUnique(String campo, String unique, int id) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) AS qtd FROM Usuario WHERE " + campo + " = ? AND id <> ?";
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

	@Override
	public boolean estaEmUso(Usuario u) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) AS tem FROM Biblioteca WHERE idUsuario = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, u.getId());
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
	public void excluirEmUso(Usuario u) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM Biblioteca WHERE idUsuario = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, u.getId());
		ps.execute();
		ps.close();
		c.close();
	}
	
}
