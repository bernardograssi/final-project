package com.example.finalproject.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.example.finalproject.dao.interfaces.NamesDao;
import com.example.finalproject.model.Names;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * This class implements the methods related to the NamesDao object.
 */
@Repository
public class NamesDaoImpl extends JdbcDaoSupport implements NamesDao {

	/**
	 * Data Source object that connects to the MySQL database.
	 */
	@Autowired
	DataSource dataSource;

	/**
	 * Data Source object initializer.
	 */
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	/**
	 * This method calls the getAllNames stored procedure, parses the result and
	 * returns a list of Names to the caller.
	 * 
	 * @return list of Names objects.
	 */
	@Override
	public List<Names> getAllNames() {
		String sql = "SELECT * FROM sn_names;";
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql); // Call stored procedure.

		// For each row in the result, add to the list.
		List<Names> result = new ArrayList<Names>();
		for (Map<String, Object> row : rows) {
			Names nam = new Names((int) row.get("id"), (String) row.get("name"));
			result.add(nam);
		}

		// Returnt the list.
		return result;
	}

	/**
	 * This method calls the deleteById stored procedure and returns true if the
	 * procedure modified more than zero rows, false otherwise.
	 * 
	 * @return true if result has modified more than zero rows, false otherwise.
	 */
	@Override
	public boolean deleteById(int id) {
		String sqlDelete = "{CALL deleteById(?)};";
		int update = getJdbcTemplate().update(sqlDelete, id); // Call stored procedure.

		// Return true if update modified more than zero rows, false otherwise.
		return update > 0;
	}

}
