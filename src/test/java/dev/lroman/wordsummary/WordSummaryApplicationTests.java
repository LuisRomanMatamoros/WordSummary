package dev.lroman.wordsummary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { WordSummaryApplication.class })
public class WordSummaryApplicationTests {

    // other test constants
	private String EXPECTED_OUTPUT = "src/main/resources/expectedResult.txt";

	private String SUBTOWORK = "src/main/resources/subtitulesTest.srt";
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	 
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
  
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    
    /**
     * get the file content 
     * 
     * @param pathFile
     * @return String
     */
	private String getFileContent(final String pathFile) {
    	StringBuilder resultStringBuilder = new StringBuilder();
        try{
            File initialFile = new File(pathFile);
            InputStream targetStream = new FileInputStream(initialFile);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(targetStream));
            
        	String line;
            while ((line = br.readLine()) != null) {
            	resultStringBuilder.append(line).append("\n");
            }
        }catch (Exception e) {
        	System.err.println(e);
		}
        
        return resultStringBuilder.toString();
    }
    
	// diferents args 
    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("fileSubs", SUBTOWORK);
        return paramsBuilder.toJobParameters();
    }

    private JobParameters nullJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("fileSubs", null);
        return paramsBuilder.toJobParameters();
    }
    
    private JobParameters directoryJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("fileSubs", "/");
        return paramsBuilder.toJobParameters();
    }
    
    private JobParameters fileNoExistJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("fileSubs", "/file");
        return paramsBuilder.toJobParameters();
    }
    
    
    // source test
	@Test(expected = JobParametersInvalidException.class)
    public void givenReferenceOutput_whenJobExecuted_thenFailByNullPath() throws Exception {        	
		jobLauncherTestUtils.launchJob(nullJobParameters());
    }
    
    
	@Test(expected = JobParametersInvalidException.class)
    public void givenReferenceOutput_whenJobExecuted_thenFailByDirectoryPath() throws Exception {        	
		jobLauncherTestUtils.launchJob(directoryJobParameters());
    }    
    
	@Test(expected = JobParametersInvalidException.class)
    public void givenReferenceOutput_whenJobExecuted_thenFailByFileNoExistPath() throws Exception {        	
		jobLauncherTestUtils.launchJob(fileNoExistJobParameters());
    }  
    
    // end to end
    @Test
    public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
        // given    	
    	String expectedResult = getFileContent(EXPECTED_OUTPUT);
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
    	
        // when
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
        
        // then
        assertThat(actualJobInstance.getJobName(), is("JobInit"));
        assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
        assertThat(expectedResult, is(outContent.toString()));
        
    }
    

}
