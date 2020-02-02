package br.unifesspa.cre.model;

import java.io.Serializable;

public class Result implements Serializable, Comparable<Result>, Cloneable{

	private static final long serialVersionUID = -8876078984426952757L;

	private Double bias;

	private Double sumRate;
	
	private Double medianRate;
	
	private Double evaluation;
	
	private Double requiredRate;
	
	private Double uesServed;
	
	private Double servingBSs;
	
	private Double[] solution;

	public Result() {
		this.bias = 0.0;
		this.sumRate = 0.0;
		this.medianRate = 0.0;
		this.evaluation = 0.0;
		this.requiredRate = 0.0;
		this.uesServed = 0.0;
		this.servingBSs = 0.0;
		this.solution = null;
	}

	public Result(Integer id, Double bias, Double sumRate, Double medianRate, Double alpha, Double beta) {
		this.bias = bias;
		this.sumRate = sumRate;
		this.medianRate = medianRate;
		this.requiredRate = 0.0;
		this.uesServed = 0.0;
	}

	public Double getSumRate() {
		return sumRate;
	}

	public void setSumRate(Double sumRate) {
		this.sumRate = sumRate;
	}

	public Double getMedianRate() {
		return medianRate;
	}

	public void setMedianRate(Double medianRate) {
		this.medianRate = medianRate;
	}

	public Double getBias() {
		return bias;
	}

	public void setBias(Double bias) {
		this.bias = bias;
	}

	public Double getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Double evaluation) {
		this.evaluation = evaluation;
	}

	public Double getRequiredRate() {
		return requiredRate;
	}

	public void setRequiredRate(Double requiredRate) {
		this.requiredRate = requiredRate;
	}

	public Double getUesServed() {
		return uesServed;
	}

	public void setUesServed(Double uesServed) {
		this.uesServed = uesServed;
	}

	public Double getServingBSs() {
		return servingBSs;
	}

	public void setServingBSs(Double servingBSs) {
		this.servingBSs = servingBSs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double[] getSolution() {
		return solution;
	}

	public void setSolution(Double[] solution) {
		this.solution = solution;
	}

	@Override
	public String toString() {
		return "Result [bias=" + bias + ", sumRate=" + sumRate + ", medianRate=" + medianRate + ", evaluation="
				+ evaluation + ", requiredRate=" + requiredRate + ", uesServed=" + uesServed + ", servingBSs="
				+ servingBSs +"]";
	}

	@Override
	public int compareTo(Result o) {
		if (this.evaluation == o.evaluation) 
			return 0;
		else if (this.evaluation > o.evaluation)
				return 1;
		else return -1;
	}

	@Override
	public Object clone() {
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