package application.persistence;

import java.sql.SQLException;

public interface ICrudUnique<T> {
	public boolean verificarUnique(String campo, String unique, int idAtual) throws SQLException, ClassNotFoundException;
}
