package net.tao.rhb.dd.enrpsellerreport;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.util.Assert;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class UploadEnrpSellerReportsTasklet implements Tasklet, InitializingBean {

	 private static final Logger LOGGER = LoggerFactory.getLogger(UploadEnrpSellerReportsTasklet.class);
	 
	 private final String dateFormat = "yyyy-MM-dd";

	   @Value("#{environment['app.local.enrpsellerreport.localDirectory']}")
	    private String localDirectory;
	    
	  @Value("#{jobParameters['batch.date']}")
       private String batchDate;
	  
	  @Autowired
	   public DataSource dataSource;
	    
	    private String serverIp;
	    private String serverPort;
	    private String serverUsername;
	    private String serverPassword;
	    private String transmissionMethod; 

	    private List<String> sellerList;
	    private Map<String,String> sellerDirectory;
	    
		public String getTransmissionMethod() {
			return transmissionMethod;
		}

		public void setTransmissionMethod(String transmissionMethod) {
			this.transmissionMethod = transmissionMethod;
		}

		public void setBatchDate(String batchDate) {
	        this.batchDate = batchDate;
	    }

	    public void setLocalDirectory(String localDirectory) {
	        this.localDirectory = localDirectory;
	    }

	    public String getLocalDirectory() {
	        return localDirectory;
	    }

	    public String getBatchDate() {
	        return batchDate;
	    }
	
	    	  
    	public String getServerIp() {
			return serverIp;
		}

		public void setServerIp(String serverIp) {
			this.serverIp = serverIp;
		}

		public String getServerPort() {
			return serverPort;
		}

		public void setServerPort(String serverPort) {
			this.serverPort = serverPort;
		}

		public String getServerUsername() {
			return serverUsername;
		}

		public void setServerUsername(String serverUsername) {
			this.serverUsername = serverUsername;
		}

		public String getServerPassword() {
			return serverPassword;
		}

		public void setServerPassword(String serverPassword) {
			this.serverPassword = serverPassword;
		}

		
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		    getServerConfig();
		    getSellerList();
		    getremoteDirecories();
		    String dateString = convertDateFormat(batchDate, "yyyyMMdd");	       
	       String filenamePattern =String.format("%sRHB0218_DENRP_SLR.DAT", dateString);                
	                
	        for(String seller:sellerList){	        	
	        	String seller_id=seller;
	        	String localDirectory=getLocalDirectory()+"/"+seller_id+"/";
	        	String remoteDirectory=sellerDirectory.get(seller_id)+seller_id+"/";	       	
	        	uploadFile(remoteDirectory,localDirectory,filenamePattern);
	        	
	        }	        
		return null;
	}

   private void getremoteDirecories(){
	   LOGGER.info("GET LIST OF SELLERS AND THERE RESPECTIVE REMOTE DIRECTORIES");
	   Connection connection = null;		
		try {
			connection=dataSource.getConnection();
			
			String sql ="select seller_id,folder_path from folder_configuration where folder_type='EOD_ENRP_REPORT' ";
			Statement statement =connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			sellerDirectory=new HashMap<String, String>();
			while (result.next()) {
				sellerDirectory.put(result.getString("seller_id"),result.getString("folder_path"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("ERROR GETTING DIRECTORY LIST FOR SELLER");
		}
		
   }
	
	private void getServerConfig(){
		Connection connection = null;
		  LOGGER.info("GET FTP/SFTP DETAILS FROM DATABASE");
		try {
			connection=dataSource.getConnection();
			
			String sql ="select server_ip,server_port,transmission_method,server_username,server_password from server_configuration where server_configuration_id= "+
                        "(select max(server_configuration_id) FROM folder_configuration where folder_type ='EOD_ENRP_REPORT')";
			Statement statement =connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				setServerIp(result.getString("server_ip"));
				setServerPort(result.getString("server_port"));	
				setTransmissionMethod(result.getString("transmission_method"));
				setServerUsername(result.getString("server_username"));	
				setServerPassword(result.getString("server_password"));	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			 LOGGER.error("ERROR GETTING DETAILS OF SERVER FROM DB");
		}
		
	}
	private void getSellerList(){
		 LOGGER.info("GET ALL SELLER DEATILS FROM DB");
		Connection connection = null;		
		try {
			connection=dataSource.getConnection();
			
			String sql =" SELECT distinct(s.seller_id) from SELLER S,SELLER_CHARGE SC where s.seller_id=sc.seller_id and sc.BIZ_MODEL='DD'";
			Statement statement =connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			sellerList =new ArrayList<String>();
			while (result.next()) {
				sellerList.add(result.getString("seller_id"));	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("ERROR READING DATA FOR SELLER");
		}		
	}
	
	
	    protected void uploadFile(String remoteDirectory,String localDirectory, String filenamePattern)  throws FileNotFoundException, InterruptedException {
	    	LOGGER.info("UPLOAD FILE DEPENDING ON TRANSMISSION TYPE");
	    	if(getTransmissionMethod().equalsIgnoreCase("FTP")){	    		
	    		boolean uploaded=uploadFtpFile(filenamePattern,localDirectory,remoteDirectory);
	    		if(uploaded==true){
	    			System.out.println("file uploded sucessfully");
	    		}
	    	 }
            if(getTransmissionMethod().equalsIgnoreCase("SFTP")){	    		
	    		boolean uploaded=uploadSftpFile(filenamePattern,localDirectory,remoteDirectory);
	    		if(uploaded==true){
	    			System.out.println("file uploded sucessfully");
	    		}
	    	 }   	

	    }
	    private boolean uploadSftpFile(String filenamePattern, String localDirectory,String remoteDirectory) {
	    	LOGGER.info("UPLOAD FILE SFTP TRANSMISSION TYPE");
			boolean uploaded = false;
			
			String sftpHost = getServerIp();
			int sftpPort = Integer.parseInt(getServerPort());
			String sftpUser = getServerUsername();
			String sftpPassword = getServerPassword();
		
			/*StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
			standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
			standardPBEStringEncryptor.setPassword(System.getenv("VAR_ENC_KEY"));
			//standardPBEStringEncryptor.setPassword("tw9oG~Ji0&nsP`dU2@\"z0q"); // for testing in local
			String password = standardPBEStringEncryptor.decrypt(folderData.getPassword());*/
			
			LOGGER.info("sftpHost :"+sftpHost+", sftpPort :"+sftpPort+", sftpUser :"+sftpUser);
			LOGGER.info("Uploading the file : "+filenamePattern +", LocalFileWithPath : "+localDirectory);
			
			if (sftpHost == null || "".equals(sftpHost))
				return false;
			if (sftpPort == 0)
				return false;
			if (sftpUser == null || "".equals(sftpUser))
				return false;
			if (sftpPassword == null || "".equals(sftpPassword))
				return false;

			Session session = null;
			Channel channel = null;
			ChannelSftp channelSftp = null;
			try
			{
				JSch jsch = new JSch();
				session = jsch.getSession(sftpUser, sftpHost, sftpPort);
				session.setPassword(sftpPassword);
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();
				channel = session.openChannel("sftp");
				channel.connect();
				channelSftp = (ChannelSftp) channel;
				
//				String localReportFile = folderData.getLocalFolderPath()+"/"+sellerId+"/"+fileName;
				String remoteFileName = remoteDirectory+"/"+filenamePattern;
				LOGGER.info("LocalFileWithPath : "+filenamePattern+", remoteFileName : "+filenamePattern);
				channelSftp.put(new FileInputStream(remoteDirectory), remoteFileName);
				uploaded = true;
			}
			catch (Exception e)
			{
				LOGGER.info("Exception in uploading the file ",e);
				return false;
			}
			finally
			{
				if (channelSftp != null)
					channelSftp.exit();
				if (channel != null)
					channel.disconnect();
				if (session != null)
					session.disconnect();
			}
			
			return uploaded;
		}
	    
	    private boolean uploadFtpFile(String filenamePattern, String localDirectory,String remoteDirectory) {
			
			boolean uploaded = false;
			
			String ftpHost = getServerIp();
			int ftpPort = Integer.parseInt(getServerPort());
			String ftpUser = getServerUsername();
			String ftpPassword = getServerPassword();
			String serverFolderPath = remoteDirectory;
		
			/*StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
			standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
			standardPBEStringEncryptor.setPassword(System.getenv("VAR_ENC_KEY"));
//			standardPBEStringEncryptor.setPassword("tw9oG~Ji0&nsP`dU2@\"z0q"); //for local testing, comment it for deploying in UAT
			String password = standardPBEStringEncryptor.decrypt(folderData.getPassword());
			*/
			
			
			
			LOGGER.info("ftpHost :"+ftpHost+", ftpPort :"+ftpPort+", ftpUser :"+ftpUser);
//			LOGGER.info("Uploading the file : "+localFileName +", localFileWithPath : "+localFileWithPath);
			
			if (ftpHost == null || "".equals(ftpHost))
				return false;
			if (ftpPort == 0)
				return false;
			if (ftpUser == null || "".equals(ftpUser))
				return false;
			/*if (password == null || "".equals(password))
				return false;
			*/
			 FTPClient ftpClient = new FTPClient(); 
			try
			{
				  ftpClient.connect(ftpHost); 
				  boolean login = ftpClient.login(ftpUser, ftpPassword);
				  if (login) {
					  
					  	LOGGER.info("2. Connection established to FTP server.");  
			            //current directory
					  	LOGGER.info("Current directory in FTP server is " + ftpClient.printWorkingDirectory());
					   
					    
			            File localFile = new File(localDirectory+filenamePattern);
				        
			            LOGGER.info("Changing the working directory to :"+serverFolderPath);
			            boolean changed = ftpClient.changeWorkingDirectory(serverFolderPath);
			            LOGGER.info("Current directory, after changeWorkingDirectory is " + ftpClient.printWorkingDirectory());
			            
			            if (changed) {
			            	LOGGER.info("3. Successfully changed working directory: "+serverFolderPath);
			            } else {
			            	LOGGER.info("3. Failed to change working directory to: "+serverFolderPath);
			            }
			            
					   //enter passive mode
					   ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // by default file type is ascii type, to upload in actual format shd be binary type
					   LOGGER.info("3.1 After setting the FileType: ");
			            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(localFile));
			            LOGGER.info("3.2 After BufferedInputStream bis, before storeFile "+bis);
			            boolean isDone = ftpClient.storeFile(filenamePattern, bis);
			            LOGGER.info("3.3 After BufferedInputStream bis "+bis);
			            bis.close();
			            
			            if (isDone) {
			            	uploaded = true;
			            	LOGGER.info("4. The file is uploaded successfully. : "+filenamePattern);
			            } else {
			            	LOGGER.info("4. The file is not uploaded : "+filenamePattern);
			            }
					  
				  } else {
					  uploaded= false;
				  }
				  
			}
			catch (Exception e)
			{
				LOGGER.info("Exception in uploading the file ",e);
				uploaded= false;
			}
			finally
			{
				if (ftpClient.isConnected()) {
					try {
						ftpClient.logout();
						ftpClient.disconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			return uploaded;
		}



	    @Override
	    public void afterPropertiesSet() throws Exception {
	        Assert.hasText(getLocalDirectory(), "The root directory of ENRP BUYER is not configured");

	        if (StringUtils.isEmpty(getBatchDate())) {
	            setBatchDate(currentDate());
	        }
	    }

	    /**
	     * Generate the date to ISO 8601 format (yyyy-MM-dd).
	     *
	     * @return today's date in ISO 8601 format
	     */
	    private String currentDate() {
	        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	        return sdf.format(new java.util.Date());
	    }

	    private String convertDateFormat(String dateString, String toFormat) throws ParseException {
	        SimpleDateFormat fromFormat = new SimpleDateFormat(dateFormat);
	        Date date = fromFormat.parse(dateString);
	        return new SimpleDateFormat(toFormat).format(date);
	    }
}
