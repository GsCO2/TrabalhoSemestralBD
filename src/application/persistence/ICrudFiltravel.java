package application.persistence;

import java.sql.SQLException;
import java.util.List;

public interface ICrudFiltravel<T> {
	public List<T> filtrar(String t) throws SQLException, ClassNotFoundException;
}
