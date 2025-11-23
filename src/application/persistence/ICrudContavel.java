package application.persistence;

import java.sql.SQLException;

public interface ICrudContavel<T> {
	public int contarTotal() throws SQLException, ClassNotFoundException;
}
