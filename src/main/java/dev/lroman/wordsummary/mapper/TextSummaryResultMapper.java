package dev.lroman.wordsummary.mapper;

import java.sql.ResultSet;

import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import dev.lroman.wordsummary.model.TextSummary;


/**
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */
@Component
public class TextSummaryResultMapper implements RowMapper<TextSummary>{
	
	/**
	 * mapper than take the result of database query 
	 * and build a TextSummary obj
	 * 
	 * @return TextSummary
	 */
	@Override
	public TextSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
		TextSummary result = new TextSummary();
		
		result.setTexto(rs.getString("wordsField"));
		result.setNumero(rs.getLong("count"));
	
		return result;
	}

}
