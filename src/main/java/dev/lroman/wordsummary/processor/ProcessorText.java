package dev.lroman.wordsummary.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

import dev.lroman.wordsummary.model.Text;

/**
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */
@Configuration
public class ProcessorText implements ItemProcessor<Text, Text>{

	/**
	 * this method receive the informacion than get in to 
	 * the system and clean him to insert in the memory database
	 * 
	 * @return Text 
	 */
    public Text process(Text item) throws Exception {
    	
    	
    	if(isTimePosition(item.getTexto())) {
    		return null;
    	}
    	
    	item.setTexto(cleanText(item.getTexto()));
    	
    	if (item.getTexto().isEmpty()) {
			return null;
		}    	

    	if(isOnlyNumber(item.getTexto())) {
    		return null;
    	}
    	
    	item.setTexto(StringUtils.capitalize(item.getTexto()));
    	
        return item;
    }
    
    /**
     * 	method than clean the text, erase a few simbol
     * 
     * @param text
     * @return String 
     */
    private String cleanText(String text) {
    	
    	String result = text;
    	// clean from html label
    	result = result.replaceAll("\\<[^>]*>","");
    	result = result.replace("<font","");
    	
    	// clean 
    	result = result.replace("-","");
    	result = result.replace(":","");
    	result = result.replace("...","");
    	result = result.replace(".","");
    	result = result.replace(",","");
    	result = result.replace("?","");
    	result = result.replace("!","");
    	result = result.replace("\"","");
    	
    	return result;
    }
    
    /**
     * method than validate if the text is part of a time position
     * (this position is where is defined when show the sub)
     * 
     * @param text
     * @return
     */
    private Boolean isTimePosition(String text) {
    	
    	if(text.contains("-->")) {
    		return true;
    	}
    	
    	if(text.matches("/^[a-z0-9_-]{3,16}$/")) {
    		return true;
    	}
    	
    	return false;
    	
    }
    
    /**
     * method than validate if the content of String is only number
     * 
     * @param text
     * @return
     */
    private Boolean isOnlyNumber(String text) {
    	if(text.matches("-?\\d+(\\.\\d+)?")) {
    		return true;
    	}
    	return false;
    	
    }
}
