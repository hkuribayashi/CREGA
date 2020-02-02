package br.unifesspa.cre.hetnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BS implements Serializable, Cloneable{

	private static final long serialVersionUID = -4238233917654767955L;

	private Point point;
	
	private Double power;
	
	private Double txGain;
	
	private Double rxGain;
	
	private Double load;
	
	private BSType type;
	
	private Double allocatedRBs;
	
	private Boolean status;
	
	private List<NetworkElement> ues;
	
	public BS(BSType type, Point point, Double power, Double txGain, Double rxGain) {
		super();
		this.type = type;
		this.point = point;
		this.power = power;
		this.txGain = txGain;
		this.rxGain = rxGain;
		this.allocatedRBs = 0.0;
		this.ues = new ArrayList<NetworkElement>();
		this.status = true;
		this.load = 0.0;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Double getPower() {
		return power;
	}

	public void setPower(Double power) {
		this.power = power;
	}

	public Double getTxGain() {
		return txGain;
	}

	public void setTxGain(Double txGain) {
		this.txGain = txGain;
	}

	public Double getRxGain() {
		return rxGain;
	}

	public void setRxGain(Double rxGain) {
		this.rxGain = rxGain;
	}

	public Double getLoad() {
		return load;
	}

	public void setLoad(Double load) {
		this.load = load;
	}

	public BSType getType() {
		return type;
	}

	public void setType(BSType type) {
		this.type = type;
	}

	public Double getAllocatedRBs() {
		return allocatedRBs;
	}

	public void setAllocatedRBs(Double allocatedRBs) {
		this.allocatedRBs = allocatedRBs;
	}

	public List<NetworkElement> getUes() {
		return ues;
	}

	public void setUes(List<NetworkElement> ues) {
		this.ues = ues;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	@Override
	protected Object clone(){
		// TODO Auto-generated method stub
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}