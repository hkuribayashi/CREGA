package br.unifesspa.cre.core;

import br.unifesspa.cre.config.CREEnv;
import br.unifesspa.cre.ga.GA;
import br.unifesspa.cre.hetnet.BS;
import br.unifesspa.cre.hetnet.HetNet;
import br.unifesspa.cre.hetnet.Scenario;
import br.unifesspa.cre.model.Result;

/**
 * 
 * @author hugo
 *
 */
public class Engine{

	private Double[] biasOffset;

	protected CREEnv env;

	protected Scenario scenario;

	public Engine(CREEnv env) {
		this.env = env;
		this.scenario = HetNet.getInstance(env);
	}

	public Result execUnifiedBias(Double bias) {

		this.biasOffset = new Double[25];
		for (int i=0; i<this.biasOffset.length; i++)
			this.biasOffset[i] = bias;

		this.scenario.setBias(this.biasOffset);
		this.scenario.evaluation();

		Result r = this.scenario.getResult();

		r.setBias(bias);
		r.setEvaluation(r.getUesServed() + r.getServingBSs() );
		
		this.scenario.reset();

		return r;
	}

	public Result getGA() {
		this.scenario.setOnOffFlag(false);
		GA ga = new GA(this.scenario, false);
		ga.evolve();
		this.scenario.reset();
		return ga.getBestIndividual().getResult();
	}
	
	public Result getGAOnOff() {
		this.scenario.setOnOffFlag(true);
		GA ga = new GA(this.scenario, true);
		ga.evolve();
		
		for(BS b: this.scenario.getAllBS()){
			if (b.getLoad().equals(0.0)) {
				System.out.println("AQUI");
				b.setStatus(false);
			}
		}
		
		this.scenario.evaluation();
		Result r = (Result) this.scenario.getResult().clone();
		
		this.scenario.reset();
		return r;
	}
	
	public Double[] getBiasOffset() {
		return biasOffset;
	}

	public void setBiasOffset(Double[] biasOffset) {
		this.biasOffset = biasOffset;
	}

	public CREEnv getEnv() {
		return env;
	}

	public void setEnv(CREEnv env) {
		this.env = env;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}
}