package dev.lroman.wordsummary.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */
public class StepSummaryCustomListener implements StepExecutionListener{

	@Override
	public void beforeStep(StepExecution stepExecution) {
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus result = new ExitStatus(stepExecution.getStatus().toString());
		if(stepExecution.getStatus().toString().equals("COMPLETED")) {
			System.out.printf("----------------------------------------%n");
		}
		
		return result;
	}

}
