package application.persistence;

import java.sql.SQLException;

public interface IExcluivel <T> {
	public boolean estaEmUso(T t) throws SQLException, ClassNotFoundException;
	public void excluirEmUso(T t) throws SQLException, ClassNotFoundException;
}
