package br.unifesspa.cre.hetnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import br.unifesspa.cre.config.CREEnv;
import br.unifesspa.cre.model.Result;
import br.unifesspa.cre.util.Util;
import br.unifesspa.cre.view.Topology;

public class Scenario implements Serializable, Cloneable, Runnable{

	private static final long serialVersionUID = -1736505791936110187L;

	private CREEnv env;

	private List<BS> allBS;

	private List<UE> ues;

	private Double[] bias;

	private Double[] onOFF;

	private Result result;

	private Boolean onOffFlag;

	/**
	 * Default Constructor
	 * @param env
	 */
	public Scenario(CREEnv env) {

		/* Parameters for scenario */
		this.env = env;

		/* Generate Macro, User and Femto locations from a Homogeneus Poisson Point Process */
		this.allBS = new ArrayList<BS>();

		List<Point> smallPoints = Util.getHPPP(this.env.getLambdaSmall(), this.env.getArea(), this.env.getHeightSmall());
		for (Point point : smallPoints) {
			BS small = new BS(BSType.Small, point, this.env.getPowerSmall(), this.env.getGainSmall(), 0.0);
			allBS.add(small);
		}

		this.bias = new Double[smallPoints.size()];
		
		this.onOFF = new Double[smallPoints.size()];

		this.ues = new ArrayList<UE>();
		List<Point> uePoints = Util.getHPPP(this.env.getLambdaUser(), this.env.getArea(), this.env.getHeightUser());
		for (Point point : uePoints) {
			UE ue = new UE(point, Util.getApplicationProfile());
			this.ues.add(ue);
		}

		Point a = new Point(100.0, 100.0, this.env.getHeightMacro());
		Point b = new Point(900.0, 900.0, this.env.getHeightMacro());

		this.allBS.add(new BS(BSType.Macro, a, this.env.getPowerMacro(), this.env.getGainMacro(), 0.0));
		this.allBS.add(new BS(BSType.Macro, b, this.env.getPowerMacro(), this.env.getGainMacro(), 0.0));

		this.result = new Result();
		this.getDistance();
		this.onOffFlag = false;
	}
	
	public void reset() {
		
		for (BS b: this.allBS) {
			b.setAllocatedRBs( 0.0 );
			b.setUes( new ArrayList<NetworkElement>() );
			b.setStatus(true);
			b.setLoad( 0.0 );
		}
		
		for (UE u: this.ues) {
			u.setBs( new ArrayList<NetworkElement>() );
			u.setBitrate(0.0);
			u.setnRB(0.0);
			u.setBitsPerOFDMSymbol(2.0);
		}
		
		this.result = new Result();
		
		this.getDistance();
	}

	private synchronized void getDistance() {
		for(UE u: this.ues)
			for (BS b: this.allBS) {
				double d = Util.getDistance(u.getPoint(), b.getPoint());
				NetworkElement n = new NetworkElement();
				n.setDistance(d);
				n.setBs(b);
				n.setUe(u);
				u.getBs().add(n);
				b.getUes().add(n);
			}
	}

	public synchronized void initBias(Double bias) {
		for (int i=0; i<this.bias.length; i++) {
			this.bias[i] = bias;
			this.onOFF[i] = 1.0;
		}
	}

	private synchronized void updateONOFF() {
		for (int j=0; j<this.onOFF.length; j++) {
			if (this.onOFF[j] < 0.5)
				this.allBS.get(j).setStatus(false);
			else this.allBS.get(j).setStatus(true);
		}
	}

	public synchronized void applyBias() {
		for (int j=0; j<this.allBS.size()-2; j++) 
			for (NetworkElement n: this.allBS.get(j).getUes())
				n.setSinr( (n.getSinr() + this.bias[j]) );
	}

	public synchronized void removeBias() {
		for (int j=0; j<this.allBS.size()-2; j++) 
			for (NetworkElement n: this.allBS.get(j).getUes()) 
				n.setSinr( (n.getSinr() - this.bias[j]) );
	}

	public synchronized void evaluation() {
		if (this.onOffFlag)
			this.updateONOFF();
		this.getSINR();
		this.applyBias();
		this.getCoverageMatrix();
		this.removeBias();
		this.getBSLoad();
		this.getResourceBlockAllocation();
		this.getBitRate();
		this.getEvaluationMetrics();
	}

	private synchronized void getSINR() {
		double bw = this.env.getBandwidth() * Math.pow(10.0, 6.0); 		
		double sigma2 = Math.pow(10.0,-3.0) * Math.pow(10.0, this.env.getNoisePower()/10.0);
		double totalThermalNoise = bw * sigma2;

		double sum = 0.0;

		for (UE u: this.ues) 
			for (NetworkElement n: u.getBs()) 
				if (n.getBs().getStatus()) {
					double pr = n.getBs().getPower() - Util.getPathLoss(n.getBs().getType(), n.getDistance(), n.getBs().getTxGain());
					pr =  Math.pow(10.0, -3.0) * Math.pow(10.0,(pr/10.0));

					for (NetworkElement ne: u.getBs()) {
						if(!ne.getBs().equals(n.getBs()) && ne.getBs().getStatus() ) {
							double prK = ne.getBs().getPower() - Util.getPathLoss(ne.getBs().getType(), ne.getDistance(), ne.getBs().getTxGain());
							prK =  Math.pow(10.0, -3.0) * Math.pow(10.0,(prK/10.0));
							sum += prK;
						}
					}

					sum += totalThermalNoise;
					n.setSinr( (10.0 * Math.log10(pr/sum)));
				}

	}

	private synchronized void getCoverageMatrix() {
		for (UE u: this.ues)
			Collections.max(u.getBs()).setCoverageStatus(true);
	}

	private synchronized void getBSLoad() {
		for (BS b: this.getAllBS()) {
			double counter = 0.0;
			for (NetworkElement n: b.getUes())
				if (n.getCoverageStatus())
					counter++;
			b.setLoad(counter);
		}
	}

	private synchronized void getResourceBlockAllocation() {
		for (BS b: this.allBS) {
			for (NetworkElement n: b.getUes()) 
				if (n.getCoverageStatus()) {

					double totalAvailableRBs = n.getBs().getType().getAvailableRBs() * n.getBs().getType().getSectors();

					if (Math.floor(totalAvailableRBs/n.getBs().getLoad()) == 0.0) {

						if (n.getBs().getAllocatedRBs() < totalAvailableRBs) {
							n.getUe().setnRB( 1.0 );
							n.getUe().setBitsPerOFDMSymbol( MCS.getEfficiency(n.getSinr()) );
							n.getBs().setAllocatedRBs( n.getBs().getAllocatedRBs() + 1 );
						}

					}else {
						n.getUe().setnRB( Math.floor(totalAvailableRBs/n.getBs().getLoad()) );
						n.getUe().setBitsPerOFDMSymbol( MCS.getEfficiency(n.getSinr()) );
					}
				}	
		}
	}

	private synchronized void getBitRate() {
		double bitrate = (this.env.getnSubcarriers() * this.env.getnOFDMSymbols() * this.env.getSubframeDuration() * 1000.0);
		for (UE u: this.ues) 
			u.setBitrate((bitrate * u.getnRB() * u.getBitsPerOFDMSymbol())/1000000.0);
	}

	private synchronized void getEvaluationMetrics() {
		double sumRate = 0.0;
		List<Double> bitrate = new ArrayList<Double>();
		for (UE u : this.ues) {
			bitrate.add(u.getBitrate());
			sumRate += u.getBitrate();

			ApplicationProfile profile = u.getProfile();
			double rRate = (profile.getBandwidth() * profile.getCompressionFactor());
			this.result.setRequiredRate( this.result.getRequiredRate() + rRate);
			if (u.getBitrate() >= rRate)
				this.result.setUesServed( this.result.getUesServed() + 1 );

		}
		this.result.setSumRate(sumRate);
		Collections.sort(bitrate);
		this.result.setMedianRate( Util.getMedian(bitrate) );

		double counter = 0.0;
		for(BS bs: this.allBS) {
			if (bs.getLoad() != 0)
				counter++;
		}
		this.result.setServingBSs(counter);
		this.result.setEvaluation( 2.0*this.result.getUesServed() - this.result.getServingBSs() );
	}

	public synchronized void paitTopology() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, 1000, 1000);
		window.getContentPane().add(new Topology(this));
		window.setVisible(true);
	}

	@Override
	public synchronized Object clone()  {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}

	}

	public synchronized void debug() {
		/*
		System.out.println("[DEBUG]: SINR");
		for (UE u: this.ues) {
			for (NetworkElement n: u.getBs())
				System.out.print(n.getSinr() + " ");
			System.out.println();
		}
		System.out.println();
		 */

		System.out.println("[DEBUG]: Bias");
		System.out.print("[  ");
		for (int i=0; i<this.bias.length; i++)
			System.out.print(this.bias[i]+"  ");
		System.out.print("]");
		System.out.println();

		System.out.println("[DEBUG]: BS Load");
		System.out.print("[  ");
		for (BS b: this.allBS)
			System.out.print(b.getLoad()+ "  ");
		System.out.print("]");
		System.out.println();
		
		System.out.println("[DEBUG]: Status");
		System.out.print("[  ");
		for (BS b: this.allBS)
			System.out.print(b.getStatus()+ "  ");
		System.out.print("]");
		System.out.println();

		System.out.println();
	}

	public CREEnv getEnv() {
		return env;
	}

	public void setEnv(CREEnv env) {
		this.env = env;
	}

	public List<BS> getAllBS() {
		return allBS;
	}

	public void setAllBS(List<BS> allBS) {
		this.allBS = allBS;
	}

	public Double[] getBias() {
		return bias;
	}

	public void setBias(Double[] bias) {
		this.bias = bias;
		this.result = new Result();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double[] getOnOFF() {
		return onOFF;
	}

	public void setOnOFF(Double[] onOFF) {
		this.onOFF = onOFF;
	}

	public Boolean getOnOffFlag() {
		return onOffFlag;
	}

	public void setOnOffFlag(Boolean onOffFlag) {
		this.onOffFlag = onOffFlag;
	}

	public List<UE> getUes() {
		return ues;
	}

	public void setUes(List<UE> ues) {
		this.ues = ues;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public void run() {
		this.evaluation();

	}
}