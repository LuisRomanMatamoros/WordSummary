package dev.lroman.wordsummary.job;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import dev.lroman.wordsummary.listener.StepSummaryCustomListener;
import dev.lroman.wordsummary.listener.StepTextCustomListener;
import dev.lroman.wordsummary.mapper.TextSummaryResultMapper;
import dev.lroman.wordsummary.model.Text;
import dev.lroman.wordsummary.model.TextSummary;
import dev.lroman.wordsummary.parameterValidator.ParameterValidator;
import dev.lroman.wordsummary.processor.ProcessorText;
import dev.lroman.wordsummary.writer.ConsoleItemWriterSummary;

/**
 * 
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */

@Configuration

public class JobText {
	
	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	/**
	 * this method is responsible of read the information from memory database 
	 * 
	 * @return ItemReader<TextSummary>
	 */
    public ItemReader<TextSummary> readSummary() {
    	JdbcCursorItemReader<TextSummary> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("select"
		 		+ "	\"WT1\".\"WORDSFIELD\", "
		 		+ "	( "
		 		+ "		select count(1) from \"PUBLIC\".\"WORDSTABLE\" as WT2 where \"WT2\".\"WORDSFIELD\" = \"WT1\".\"WORDSFIELD\" "
		 		+ "	) as count "
		 		+ "from "
		 		+ "	\"PUBLIC\".\"WORDSTABLE\" as WT1 "
		 		+ "group by "
		 		+ "	\"WT1\".\"WORDSFIELD\" "
		 		+ "order by "
		 		+ "count desc;");
        reader.setIgnoreWarnings(true);
        reader.setVerifyCursorPosition(true);
        reader.setRowMapper(new TextSummaryResultMapper());
        reader.setQueryTimeout(10000);
        reader.setSaveState(true);
        
        
        return reader;
	}
    
    /**
	 * This method build a new Step is responsible of read 
	 * the information with summary format and show
	 * 
     * @return TaskletStep
     * @throws Exception
     */
    @Bean
    public TaskletStep stepSummary() throws Exception {
        return stepBuilderFactory
                .get("stepSummary")
                .<TextSummary, TextSummary>chunk(3)
                .reader(readSummary())
                .writer(new ConsoleItemWriterSummary())
                .listener(new StepSummaryCustomListener())
                .build();
    }
	
	/**
	 * this method build the writer that is responsible of insert the 
	 * information on the memory database 
	 * 
	 * @return JdbcBatchItemWriter<Text>
	 * @throws Exception
	 */
	@Bean
	public JdbcBatchItemWriter<Text> writerText() throws Exception {
		JdbcBatchItemWriterBuilder<Text> jdbcWritter = new JdbcBatchItemWriterBuilder<Text>();
		jdbcWritter.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		jdbcWritter.itemPreparedStatementSetter((item, ps) -> {
			ps.setString(0, (String) item.getTexto());
		});
		 
		jdbcWritter.sql("INSERT " +
		            "INTO \"PUBLIC\".\"WORDSTABLE\"(\"WORDSFIELD\") " +
		            "VALUES (:texto)");
		 
		jdbcWritter.dataSource(dataSource);
		 
		return jdbcWritter.build();

	 }
	 
     /**
      * this method just return a new instance of ProcessorText
      * 
      * @return ProcessorText
      */
     public ProcessorText processorText() {
         return new ProcessorText(); 
     }
    
     @Bean
     @StepScope
     public FlatFileItemReader<Text> readerText(@Value("#{jobParameters['fileSubs']}") String fileSubs) throws Exception {
    	 
    	 FlatFileItemReader<Text> reader = new FlatFileItemReader<Text>();
    	 reader.setResource(new FileSystemResource(fileSubs));
    	 reader.setLinesToSkip(1);   
    	 DefaultLineMapper<Text> lineMapper = new DefaultLineMapper<>();  
    	 BeanWrapperFieldSetMapper<Text> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    	 DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		  
    	 lineTokenizer.setDelimiter(" ");
    	 lineTokenizer.setStrict(false);
    	 lineTokenizer.setNames(new String[]{"texto"});		
    	 fieldSetMapper.setTargetType(Text.class);
		
    	 lineMapper.setLineTokenizer(lineTokenizer);
    	 lineMapper.setFieldSetMapper(fieldSetMapper);
	  
    	 reader.setLineMapper(lineMapper);
		  
    	 return reader;
	}
	
	/**
	 * This method build a new Step is responsible of to read 
	 * the information and insert in a memory database
	 * 
	 * @return TaskletStep
	 * @throws Exception
	 */
    @Bean
    public TaskletStep stepText() throws Exception {
        return stepBuilderFactory
                .get("stepText")
                .<Text, Text>chunk(3)
                .reader(readerText(null))
                .processor(processorText())
                .writer(writerText())       
                .listener(new StepTextCustomListener())
                .build();
    }
    
	/**
	 * This method set the Steps to follow and him order
	 * 
	 * @return Job
	 * @throws Exception
	 */
	@Bean
	public Job jobInit() throws Exception {
		
	    return jobBuilderFactory
	            .get("JobInit")
	            .validator(new ParameterValidator())
	            .start(stepText())
	            .next(stepSummary())
	            .incrementer(new RunIdIncrementer())
	            .build();
	}
	
	


}
