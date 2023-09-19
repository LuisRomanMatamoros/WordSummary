package dev.lroman.wordsummary.parameterValidator;

import java.io.File;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

/**
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */
public class ParameterValidator implements JobParametersValidator {
	
    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        String fileName = jobParameters.getString("fileSubs");

		if(fileName == null) {
			throw new JobParametersInvalidException("1 parameter is required.");
		} 
		File file = new File(fileName);
		
		if(!file.exists()) {
			throw new JobParametersInvalidException("The indicated file doesn't exist.");
		}
		
		if(file.isDirectory()) {
			throw new JobParametersInvalidException("The path entered is a folder.");
		}
    }
}