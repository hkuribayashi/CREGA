package br.unifesspa.cre.hetnet;

import java.io.Serializable;

public class NetworkElement implements Comparable<NetworkElement>, Serializable, Cloneable{

	private static final long serialVersionUID = -670526476345447198L;

	private UE ue;
	
	private BS bs;
	
	private double distance;
	
	private boolean coverageStatus = false;
	
	private double sinr;
	
	private double bandwith;
	
	private double delay;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Boolean getCoverageStatus() {
		return coverageStatus;
	}

	public void setCoverageStatus(Boolean coverageStatus) {
		this.coverageStatus = coverageStatus;
	}

	public Double getSinr() {
		return sinr;
	}

	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	public Double getBandwith() {
		return bandwith;
	}

	public void setBandwith(Double bandwith) {
		this.bandwith = bandwith;
	}

	public Double getDelay() {
		return delay;
	}

	public void setDelay(Double delay) {
		this.delay = delay;
	}

	public UE getUe() {
		return ue;
	}

	public void setUe(UE ue) {
		this.ue = ue;
	}

	public void setSinr(double sinr) {
		this.sinr = sinr;
	}

	public BS getBs() {
		return bs;
	}

	public void setBs(BS bs) {
		this.bs = bs;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(NetworkElement o) {
		if (this.sinr < o.sinr)
			return -1;
		else if (this.sinr > o.sinr)
			return 1;
		else return 0;
	}
}
