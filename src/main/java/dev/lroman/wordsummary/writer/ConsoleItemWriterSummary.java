package dev.lroman.wordsummary.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import dev.lroman.wordsummary.model.TextSummary;

/**
 * 
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */
public class ConsoleItemWriterSummary implements ItemWriter<TextSummary> {
	
	/**
	 * This method is responsible of print the result.
	 * 
	 */
    @Override
    public void write(List<? extends TextSummary> items) throws Exception {

        for (TextSummary textSummary : items) {
        	System.out.printf("| %-25s | %-8s |%n", textSummary.getTexto(), textSummary.getNumero());
		}
        
    }
}
