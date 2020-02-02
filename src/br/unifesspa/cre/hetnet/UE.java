package br.unifesspa.cre.hetnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UE implements Serializable, Cloneable{

	private static final long serialVersionUID = -5657596059481333345L;

	private Point point;
	
	private ApplicationProfile profile;
	
	private double bitrate;
	
	private double nRB;
	
	private double bitsPerOFDMSymbol;
	
	private List<NetworkElement> bs;

	public UE() {
		super();
	}

	public UE(Point point, ApplicationProfile profile) {
		super();
		this.point = point;
		this.profile = profile;
		this.bitrate = 0.0;
		this.nRB = 0.0;
		this.bitsPerOFDMSymbol = 2.0;
		this.bs = new ArrayList<NetworkElement>();
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public ApplicationProfile getProfile() {
		return profile;
	}

	public void setProfile(ApplicationProfile profile) {
		this.profile = profile;
	}

	public Double getBitrate() {
		return bitrate;
	}

	public void setBitrate(Double bitrate) {
		this.bitrate = bitrate;
	}

	public Double getnRB() {
		return nRB;
	}

	public void setnRB(Double nRB) {
		this.nRB = nRB;
	}

	public Double getBitsPerOFDMSymbol() {
		return bitsPerOFDMSymbol;
	}

	public void setBitsPerOFDMSymbol(Double bitsPerOFDMSymbol) {
		this.bitsPerOFDMSymbol = bitsPerOFDMSymbol;
	}
	
	public List<NetworkElement> getBs() {
		return bs;
	}

	public void setBs(List<NetworkElement> bs) {
		this.bs = bs;
	}

	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
