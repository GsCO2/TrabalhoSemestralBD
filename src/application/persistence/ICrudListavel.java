package application.persistence;

import java.sql.SQLException;
import java.util.List;

public interface ICrudListavel<T> {
	public List<T> listar () throws SQLException, ClassNotFoundException;
}
