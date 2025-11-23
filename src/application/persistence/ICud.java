package application.persistence;

import java.sql.SQLException;

public interface ICud <T> {
	
	public void inserir (T t) throws SQLException, ClassNotFoundException;
    public void alterar (T t) throws SQLException, ClassNotFoundException;
    public void excluir (T t) throws SQLException, ClassNotFoundException;
}
