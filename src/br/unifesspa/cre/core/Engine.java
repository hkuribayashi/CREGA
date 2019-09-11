package br.unifesspa.cre.core;

import br.unifesspa.cre.config.CREEnv;
import br.unifesspa.cre.ga.GA;
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
		this.scenario = new Scenario(env);
	}

	public Engine(Scenario scenario) {
		this.scenario = scenario;
		this.env = this.scenario.getEnv();
	}

	public Result execNoBias() {

		this.biasOffset = new Double[20];
		for (int i=0; i<this.biasOffset.length; i++)
			this.biasOffset[i] = 0.0;

		this.scenario.setBias(this.biasOffset);
		this.scenario.evaluation();

		Result r = new Result();

		r.setBias(0.0);
		r.setSumRate(this.scenario.getSumRate());
		r.setMedianRate(this.scenario.getMedianRate());
		r.setRequiredRate(this.scenario.getRequiredRate());
		r.setUesServed(this.scenario.getUesServed());
		r.setServingBSs( this.scenario.getServingBSs() );
		r.setEvaluation( r.getUesServed() + r.getServingBSs() );
		r.setScenario(this.scenario);

		return r;
	}

	public Result execUnifiedBias(Double bias) {

		this.biasOffset = new Double[20];
		for (int i=0; i<this.biasOffset.length; i++)
			this.biasOffset[i] = bias;

		this.scenario.setBias(this.biasOffset);
		this.scenario.evaluation();

		Result r = new Result();

		r.setBias(bias);
		r.setSumRate(this.scenario.getSumRate());
		r.setMedianRate(this.scenario.getMedianRate());
		r.setRequiredRate(this.scenario.getRequiredRate());
		r.setUesServed(this.scenario.getUesServed());
		r.setServingBSs( this.scenario.getServingBSs() );
		r.setEvaluation(r.getUesServed() + r.getServingBSs() );
		r.setScenario(this.scenario);

		return r;
	}

	public Result getGA() {
		GA ga = new GA(this.scenario);
		ga.evolve();
		return ga.getBestIndividual().getResult();
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