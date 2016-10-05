package net.tao.rhb.dd.enrpsellerreport;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.tao.rhb.dd.commons.listener.ProtocolListener;
import net.tao.rhb.dd.sellerenrp.model.EnrpSellerData;
import net.tao.rhb.dd.sellerenrp.model.Seller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

@Configuration

@EnableBatchProcessing
public class SellerEnrpreportBatchConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SellerEnrpreportBatchConfiguration.class);
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    public DataSource dataSource;
    /**
     * 
     * SMS Notification job.
     *
     * @return job configuration of SMS Notification job and insert into the delivery status into db
     */
    @Bean
    public Job sendSmsNotificationJob() {
        return jobBuilderFactory.get("sellerEnrpReportJob")
                .incrementer(new RunIdIncrementer())
                .listener(new ProtocolListener())
                .start(step1())
                .next(step2())
                .build();
        		
    }

    @Bean
    public Step step1() {
    	 return stepBuilderFactory.get("step1")
    			 .allowStartIfComplete(true)
         		 .<Seller,EnrpSellerData>chunk(1)
                 .reader(reader())
                 .processor(processor())
                 .writer(writer())
                 .faultTolerant()
                 .skipLimit(99999)
                 .skip(ValidationException.class)
                 .noRollback(ValidationException.class)
                 .build();
    	         
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
        		.tasklet(createUploadEnrpSellerReportsTasklet())
                .build();
    }
    @Bean
    @StepScope
    protected UploadEnrpSellerReportsTasklet createUploadEnrpSellerReportsTasklet() {
    	UploadEnrpSellerReportsTasklet tasklet = new UploadEnrpSellerReportsTasklet();
        
        return tasklet;
    }
    
     @Bean
     @StepScope
    public JdbcCursorItemReader <Seller> reader() {
    	LOGGER.info("READING SELLER  DETAILS FROM SELLER TABLE");    	 
    	
    	JdbcCursorItemReader<Seller> dataReader = new JdbcCursorItemReader<Seller>();    	
    	dataReader.setRowMapper(new RowMapper<Seller>() {
            @Override
            public Seller mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Seller(
                        resultSet.getString("seller_id"),
                        resultSet.getString("enrp_file_block1"),
                        resultSet.getString("enrp_file_block2")
                        );            	
            }
        });
    	dataReader.setSql("SELECT  distinct(s.seller_id),sc.enrp_file_block1,sc.enrp_file_block2 from SELLER S,SELLER_CHARGE SC where s.seller_id=sc.seller_id and sc.BIZ_MODEL='DD' ");
    	dataReader.setDataSource(dataSource);    	
		LOGGER.info("READING SELLER  DETAILS FROM SELLER TABLE COMPLIITED");		
    	return dataReader;
    }
    @Bean
    @StepScope
    public SellerEnrpProcessor processor() {
        return new SellerEnrpProcessor(); 
    }
    @Bean
    @StepScope    
    public 	SellerEnrpReportWriter writer() {    	
    	SellerEnrpReportWriter writer=new SellerEnrpReportWriter();    	
        return writer;
    }    
  
}
