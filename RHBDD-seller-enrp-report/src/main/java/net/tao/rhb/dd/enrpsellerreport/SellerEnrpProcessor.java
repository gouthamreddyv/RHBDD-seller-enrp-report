package net.tao.rhb.dd.enrpsellerreport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.tao.rhb.dd.sellerenrp.model.EnrpSellerBlock1Data;
import net.tao.rhb.dd.sellerenrp.model.EnrpSellerBlock2Data;
import net.tao.rhb.dd.sellerenrp.model.EnrpSellerData;
import net.tao.rhb.dd.sellerenrp.model.Seller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@StepScope
public class SellerEnrpProcessor implements ItemProcessor<Seller, EnrpSellerData>{
	@Autowired
    public DataSource dataSource;
	private static final Logger LOGGER = LoggerFactory.getLogger(SellerEnrpProcessor.class);
	
	public EnrpSellerData process(Seller seller) throws Exception {
		Connection connection = null;
		EnrpSellerData enrpList=new EnrpSellerData();		
		enrpList.setSellerID(seller.getSeller_id());
		LOGGER.info("PROCESS SELLER DATA FOR SELLER"+seller.getSeller_id());
		List<EnrpSellerBlock1Data> listBlock1=new ArrayList<EnrpSellerBlock1Data>();
		List<EnrpSellerBlock2Data> listBlock2=new ArrayList<EnrpSellerBlock2Data>();
		try {
			connection = dataSource.getConnection();
			Statement seqstmt = connection.createStatement();
			if(seller.getEnrp_file_block1().equalsIgnoreCase("Y")){
				LOGGER.info("GET BLOCK1 RECORDS FOR SELLER"+seller.getSeller_id());	
			String query = " Select SERIALNO, SELLERBANKID,SELLERID,BUYERBANKID,BATCHID,PYMTREFNO,IDTYPE,IDNUMBER,ACCHOLDERNAME,ACCNO,MAXAMT"
	                +",MODEOFFREQ,MAXFREQ,TYPEOFAPPLICATION,PURPOSEOFPYMT,TELNO,EMAIL,EFFECTIVEDATE,EXPIRYDATE,"
					+"APPLICATIONDATE,MERCHANTDATE,APPROVALSTATUS,MANDATENO,FILE_ID from SELLER_ENRP_BLOCK1 where SELLERID = '"+seller.getSeller_id()
					+ "' AND "
					+ " FILE_ID =(SELECT MAX(FILE_ID) FROM SELLER_ENRP_FILE)";
			
			ResultSet rs=seqstmt.executeQuery(query);
			EnrpSellerBlock1Data block1Line=null;
			
			while(rs.next()) {
				block1Line=new EnrpSellerBlock1Data();
				block1Line.setAccountNo(rs.getString("ACCNO"));
		        block1Line.setApplicationDate(rs.getString("APPLICATIONDATE"));
		        block1Line.setApprovalStatus(rs.getString("APPROVALSTATUS"));
		        block1Line.setBatchId(rs.getString("BATCHID"));
		        block1Line.setBuyerBankId(rs.getString("BUYERBANKID"));
		        block1Line.setCustomerName(rs.getString("ACCHOLDERNAME"));
		        block1Line.setEffectiveDate(rs.getString("EFFECTIVEDATE"));
		        block1Line.setEmail(rs.getString("EMAIL"));
		        block1Line.setExpDate(rs.getString("EXPIRYDATE"));
		        block1Line.setFileId(rs.getInt("FILE_ID"));
		        block1Line.setFreqMode(rs.getString("MODEOFFREQ"));
		        block1Line.setIdentificationNo(rs.getString("IDNUMBER"));
		        block1Line.setIdentificationType(rs.getString("IDTYPE"));
		        block1Line.setMandateNo(rs.getString("MANDATENO"));
		        block1Line.setMaxDebitAmount(rs.getString("MAXAMT"));
		        block1Line.setMaxFreq(rs.getString("MAXFREQ"));
		        block1Line.setMerchantDate(rs.getString("MERCHANTDATE"));
		        block1Line.setPaymentPurpose(rs.getString("PURPOSEOFPYMT"));
		        block1Line.setPaymentRefNo(rs.getString("PYMTREFNO"));
		        block1Line.setSellerBankId(rs.getString("SELLERBANKID"));
		        block1Line.setSellerId(rs.getString("SELLERID"));
		        block1Line.setSerialNO(rs.getString("SERIALNO"));
		        block1Line.setTelNo(rs.getString("TELNO"));
		        block1Line.setTypeOfApplication(rs.getString("TYPEOFAPPLICATION"));
		       
		        listBlock1.add(block1Line);
			}
			
			} 
			if(seller.getEnrp_file_block2().equalsIgnoreCase("Y")) {
				LOGGER.info("GET BLOCK2 RECORDS FOR SELLER"+seller.getSeller_id());	
				String query = " Select SERIALNO, SELLERBANKID,SELLERID,BUYERBANKID,BATCHID,PYMTREFNO,IDTYPE,IDNUMBER,ACCHOLDERNAME,ACCNO,MAXAMT"
		                +",MODEOFFREQ,MAXFREQ,TYPEOFAPPLICATION,PURPOSEOFPYMT,TELNO,EMAIL,EFFECTIVEDATE,EXPIRYDATE,"
						+"APPLICATIONDATE,MERCHANTDATE,APPROVALSTATUS,MANDATENO,FILE_ID from SELLER_ENRP_BLOCK2 where STATUS='00' AND SELLERID = '"+seller.getSeller_id()
						+ "' AND "
						+ " FILE_ID =(SELECT MAX(FILE_ID) FROM SELLER_ENRP_FILE)";
				
				ResultSet rs=seqstmt.executeQuery(query);
				EnrpSellerBlock2Data block2Line=null;
				
				while(rs.next()) {
					block2Line=new EnrpSellerBlock2Data();
					block2Line.setAccountNo(rs.getString("ACCNO"));
			        block2Line.setApplicationDate(rs.getString("APPLICATIONDATE"));
			        block2Line.setApprovalStatus(rs.getString("APPROVALSTATUS"));
			        block2Line.setBatchId(rs.getString("BATCHID"));
			        block2Line.setBuyerBankId(rs.getString("BUYERBANKID"));
			        block2Line.setCustomerName(rs.getString("ACCHOLDERNAME"));
			        block2Line.setEffectiveDate(rs.getString("EFFECTIVEDATE"));
			        block2Line.setEmail(rs.getString("EMAIL"));
			        block2Line.setExpDate(rs.getString("EXPIRYDATE"));
			        block2Line.setFileId(rs.getInt("FILE_ID"));
			        block2Line.setFreqMode(rs.getString("MODEOFFREQ"));
			        block2Line.setIdentificationNo(rs.getString("IDNUMBER"));
			        block2Line.setIdentificationType(rs.getString("IDTYPE"));
			        block2Line.setMandateNo(rs.getString("MANDATENO"));
			        block2Line.setMaxDebitAmount(rs.getString("MAXAMT"));
			        block2Line.setMaxFreq(rs.getString("MAXFREQ"));
			        block2Line.setMerchantDate(rs.getString("MERCHANTDATE"));
			        block2Line.setPaymentPurpose(rs.getString("PURPOSEOFPYMT"));
			        block2Line.setPaymentRefNo(rs.getString("PYMTREFNO"));
			        block2Line.setSellerBankId(rs.getString("SELLERBANKID"));
			        block2Line.setSellerId(rs.getString("SELLERID"));
			        block2Line.setSerialNO(rs.getString("SERIALNO"));
			        block2Line.setTelNo(rs.getString("TELNO"));
			        block2Line.setTypeOfApplication(rs.getString("TYPEOFAPPLICATION"));
			       
			        listBlock2.add(block2Line);
				}
					
			}
			
			seqstmt.close();
			
		  } catch (Exception e) {
			  e.printStackTrace();
			  LOGGER.error("ERROR OCCURED IN GETTING ENRP BLOCK TABLES"+e.getLocalizedMessage());
		  }
			finally {
			connection.close();
			LOGGER.info("PROCESSOR  COMPLITED READIG DATA FROM ENRP BLOCKS FOR SELLER "+seller.getSeller_id());	
			}
		enrpList.setBlock1(listBlock1);
		enrpList.setBlock2(listBlock2);
		return enrpList;
	}
	

}
