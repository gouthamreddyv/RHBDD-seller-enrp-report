package net.tao.rhb.dd.enrpsellerreport;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tao.rhb.dd.sellerenrp.model.EnrpSellerBlock1Data;
import net.tao.rhb.dd.sellerenrp.model.EnrpSellerBlock2Data;
import net.tao.rhb.dd.sellerenrp.model.EnrpSellerData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;


public class SellerEnrpReportWriter implements  ItemWriter <EnrpSellerData>,ItemStream{
	
	private FlatFileItemWriter<EnrpSellerBlock1Data> writer1;
	private FlatFileItemWriter<EnrpSellerBlock2Data> writer2;	
	private ExecutionContext executionContext;	
	@Value("#{environment['app.local.enrpsellerreport.localDirectory']}")
    private String localDirectory;	
	private static final Logger LOGGER = LoggerFactory.getLogger(SellerEnrpReportWriter.class);	
	public FlatFileItemWriter<EnrpSellerBlock1Data> getWriter1() {
		return writer1;
	}

	public void setWriter1(FlatFileItemWriter<EnrpSellerBlock1Data> writer1) {
		this.writer1 = writer1;
	}

	public FlatFileItemWriter<EnrpSellerBlock2Data> getWriter2() {
		return writer2;
	}

	public void setWriter2(FlatFileItemWriter<EnrpSellerBlock2Data> writer2) {
		this.writer2 = writer2;
	}

	public String getSellerReportFileName(){
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyyMMdd");
		String date=dateformat.format(new Date());
		String fileName=date+"RHB0218"+"_DENRP_SLR.DAT";
		LOGGER.info("FILE NAME TO GENERATE REPORT"+fileName);
		return fileName;
	}	

	public void write(List<? extends EnrpSellerData> item) throws Exception {
	  LOGGER.info("WRITING DATA TO LOCAL FILE SYSTEM");
	  writer1=new FlatFileItemWriter<EnrpSellerBlock1Data>();
	  writer2=new FlatFileItemWriter<EnrpSellerBlock2Data>();
	  BeanWrapperFieldExtractor<EnrpSellerBlock1Data> fieldExtractor = new BeanWrapperFieldExtractor<>(); 
  	  fieldExtractor.setNames(new String[] { "serialNO","sellerBankId","sellerId","buyerBankId","batchId","paymentRefNo","identificationType","identificationNo","customerName","accountNo","maxDebitAmount","freqMode","maxFreq","typeOfApplication","paymentPurpose",
				"telNo","email","effectiveDate","expDate","applicationDate","merchantDate","approvalStatus","mandateNo"}); 
  	  FormatterLineAggregator<EnrpSellerBlock1Data> lineAggregator = new FormatterLineAggregator<>(); 
  	  lineAggregator.setFormat("%-5s%-8s%-10s%-8s%-40s%-20s%-1s%-20s%-54s%-20s%-16s%-2s%-3s%-2s%-27s%-11s%-27s%-8s%-8s%-8s%-8s%-2s%-25s"); 
  	  lineAggregator.setFieldExtractor(fieldExtractor);  	 
  	  writer1.setLineAggregator(lineAggregator);
  	  writer1.afterPropertiesSet();
  	  
  	  
  	  BeanWrapperFieldExtractor<EnrpSellerBlock2Data> fieldExtractor1 = new BeanWrapperFieldExtractor<>(); 
	  fieldExtractor1.setNames(new String[] { "serialNO","sellerBankId","sellerId","buyerBankId","batchId","paymentRefNo","identificationType","identificationNo","customerName","accountNo","maxDebitAmount","freqMode","maxFreq","typeOfApplication","paymentPurpose",
				"telNo","email","effectiveDate","expDate","applicationDate","merchantDate","approvalStatus","mandateNo"}); 
	  FormatterLineAggregator<EnrpSellerBlock2Data> lineAggregator1 = new FormatterLineAggregator<>(); 
	  lineAggregator1.setFormat("%-5s%-8s%-10s%-8s%-40s%-20s%-1s%-20s%-54s%-20s%-16s%-2s%-3s%-2s%-27s%-11s%-27s%-8s%-8s%-8s%-8s%-2s%-25s"); 
	  lineAggregator1.setFieldExtractor(fieldExtractor1);  	 
	  writer2.setLineAggregator(lineAggregator1);  	
	  writer2.afterPropertiesSet();
 
	  for (EnrpSellerData data:item){
		  writer1.setHeaderCallback(new FlatFileHeaderCallback() {
				public void writeHeader(Writer writer) throws IOException {					
						writer.write("Block1");					
				}
			});
		  writer2.setHeaderCallback(new FlatFileHeaderCallback() {
				public void writeHeader(Writer writer) throws IOException {					
						writer.write("\nBlock2");					
				}
			});
	   		writer1.setShouldDeleteIfExists(true);
	   		writer1.setResource(new FileSystemResource(localDirectory+data.getSellerID()+"/"+getSellerReportFileName()));
	   		writer1.open(executionContext);
	   		writer2.setAppendAllowed(true);
	   		writer2.setResource(new FileSystemResource(localDirectory+data.getSellerID()+"/"+getSellerReportFileName())); 
	   		writer2.open(executionContext);
	   		writer1.write(data.getBlock1());		      
		    writer2.write(data.getBlock2()); 		
		}
	  LOGGER.info("WRITING DATA TO LOCAL FILE SYSTEM COMPLITED");		
	}
	
	
	 public void setDelegate(FlatFileItemWriter<EnrpSellerBlock1Data> writer1) {
	        this.writer1 = writer1;
	    }
	
	    @Override
	    public void open(ExecutionContext executionContext) throws ItemStreamException {
	    	this.executionContext=executionContext;
	    }

	    @Override
	    public void update(ExecutionContext executionContext) throws ItemStreamException {
	      
	    }

	    @Override
	    public void close() throws ItemStreamException {
	      
	    }

}
