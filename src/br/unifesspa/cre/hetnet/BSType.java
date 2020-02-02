package br.unifesspa.cre.hetnet;

public enum BSType implements Cloneable {

	Macro(3.0, 100.0), 
	Small(1.0, 100.0);
	
	private Double sectors;
	
	private Double availableRBs;
	
	BSType(Double sectors, Double availableRBs){
		this.sectors = sectors;
		this.availableRBs = availableRBs;
	}

	public Double getSectors() {
		return sectors;
	}

	public void setSectors(Double sectors) {
		this.sectors = sectors;
	}

	public Double getAvailableRBs() {
		return availableRBs;
	}

	public void setAvailableRBs(Double availableRBs) {
		this.availableRBs = availableRBs;
	}
}
