package net.tao.rhb.dd.sellerenrp.model;

import java.util.List;



public class EnrpSellerData {

	private List<EnrpSellerBlock1Data> block1;
	private List<EnrpSellerBlock2Data> block2;
	private String sellerID;
	
	
	public String getSellerID() {
		return sellerID;
	}
	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}
	public List<EnrpSellerBlock1Data> getBlock1() {
		return block1;
	}
	public void setBlock1(List<EnrpSellerBlock1Data> block1) {
		this.block1 = block1;
	}
	public List<EnrpSellerBlock2Data> getBlock2() {
		return block2;
	}
	public void setBlock2(List<EnrpSellerBlock2Data> block2) {
		this.block2 = block2;
	}
	
}
