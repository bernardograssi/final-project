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

@Repository
public class NamesDaoImpl extends JdbcDaoSupport implements NamesDao {
    @Autowired
    DataSource dataSource;
    
    @PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}

    @Override
	public List<Names> getAllNames(){
		String sql = "SELECT * FROM sn_names;";
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
		
		List<Names> result = new ArrayList<Names>();
		for(Map<String, Object> row:rows){
			Names nam = new Names((int) row.get("id"), (String) row.get("name"));
			result.add(nam);
		}
		
		return result;
	}

	@Override
	public boolean deleteById(int id) {
		String sqlDelete = "{CALL deleteById(?)};";
		int update = getJdbcTemplate().update(sqlDelete, id);

		return update > 0;
	}

}
