package dev.lroman.wordsummary;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */
@EnableBatchProcessing
@SpringBootApplication
public class WordSummaryApplication implements CommandLineRunner{
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	public static void main(String[] args) {
		SpringApplication.run(WordSummaryApplication.class, args);
	}
	
	@Override
	public void run(String... args) {
		try{
			
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("fileSubs", args[0])
					.toJobParameters();
			JobExecution execution = jobLauncher.run(job, jobParameters);

			System.out.println("STATUS :: " + execution.getStatus());

			
		}catch(IllegalArgumentException   
					| JobExecutionAlreadyRunningException | JobRestartException 
					| JobInstanceAlreadyCompleteException | JobParametersInvalidException 
				e) {
			
			
			System.out.println("Error: " + e.getMessage());
		}
		

	}

}
