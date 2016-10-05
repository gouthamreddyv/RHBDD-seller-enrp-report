package net.tao.rhb.dd.sellerenrp.model;

import java.math.BigDecimal;



public class EnrpSellerBlock2Data {

	private int fileId;
	private String serialNO;
	
	private String  sellerBankId;
	
	private String sellerId;
	
	private String  buyerBankId;
	
	private String batchId;
	
	private String mandateNo;
		
	private String identificationType;
	
	private String identificationNo;
	
	private String customerName;
		
	private String telNo;
	
	private String email;
	
	private String paymentPurpose;
	
	private String typeOfApplication;
	
	private String applicationDate;
	
	private String merchantDate;
	
	private String approvalStatus;
	
	private String accountNo;
	
	private String paymentRefNo;
	
	private String maxDebitAmount;
	
	private String freqMode;
	
	private String maxFreq;

	private String expDate;
	
	private String effectiveDate;
	
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getMandateNo() {
		return mandateNo;
	}

	public void setMandateNo(String mandateNo) {
		this.mandateNo = mandateNo;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPaymentRefNo() {
		return paymentRefNo;
	}

	public void setPaymentRefNo(String paymentRefNo) {
		this.paymentRefNo = paymentRefNo;
	}

	

	public String getSerialNO() {
		return serialNO;
	}

	public void setSerialNO(String serialNO) {
		this.serialNO = serialNO;
	}

	public String getSellerBankId() {
		return sellerBankId;
	}

	public void setSellerBankId(String sellerBankId) {
		this.sellerBankId = sellerBankId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getBuyerBankId() {
		return buyerBankId;
	}

	public void setBuyerBankId(String buyerBankId) {
		this.buyerBankId = buyerBankId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getTypeOfApplication() {
		return typeOfApplication;
	}

	public void setTypeOfApplication(String typeOfApplication) {
		this.typeOfApplication = typeOfApplication;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getMerchantDate() {
		return merchantDate;
	}

	public void setMerchantDate(String merchantDate) {
		this.merchantDate = merchantDate;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	

	public String getMaxDebitAmount() {
		return maxDebitAmount;
	}

	public void setMaxDebitAmount(String maxDebitAmount) {
		BigDecimal maxDebit= new BigDecimal(maxDebitAmount.trim()).multiply(new BigDecimal(100));
		this.maxDebitAmount =String.format("%016d", BigDecimal.valueOf(maxDebit.longValue(),0).toBigInteger());
	}

	public String getFreqMode() {
		return freqMode;
	}

	public void setFreqMode(String freqMode) {
		this.freqMode = freqMode;
	}

	

	public String getMaxFreq() {
		return maxFreq;
	}

	public void setMaxFreq(String maxFreq) {
		this.maxFreq = maxFreq;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	@Override
	public String toString() {
		return serialNO+sellerBankId+sellerId+buyerBankId+batchId+mandateNo+identificationType+identificationNo+customerName+telNo+email+paymentPurpose+typeOfApplication+applicationDate+merchantDate+approvalStatus+accountNo+paymentRefNo+maxDebitAmount+freqMode+maxFreq+expDate+effectiveDate;
	}
	
}
