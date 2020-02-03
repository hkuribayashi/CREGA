package br.unifesspa.cre.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.unifesspa.cre.config.CREEnv;
import br.unifesspa.cre.model.Result;
import br.unifesspa.cre.util.Util;

public class Individual implements Cloneable, Comparable<Individual>{

	private Double[] chromossome;

	private Double[] booleanChromossome;

	private CREEnv env;

	private Result result;

	private Boolean flag;
	
	public Individual(CREEnv env, Boolean flag) {

		this.env = env;

		double lowerBound = env.getInitialGeneRange();
		double upperBound = env.getFinalGeneRange();

		int chromossomeSize = 25;

		this.chromossome = new Double[chromossomeSize];
		this.booleanChromossome = new Double[chromossomeSize];

		for (int i=0; i<chromossomeSize; i++) {
			this.chromossome[i] = Util.getUniformRealDistribution(lowerBound, upperBound);
			this.booleanChromossome[i] = Math.random();
		}

		this.flag = flag;
	}

	public Individual crossover(Individual otherIndividual) {
		Individual individual = null;
		int index = Util.getUniformIntegerDistribution(0, CrossoverStrategy.values().length-1);
		CrossoverStrategy strategy = CrossoverStrategy.values()[index];
		switch(strategy) {
		case OnePoint: individual = onePointCrossover(otherIndividual); break;
		case TwoPoint: individual = twoPointCrossover(otherIndividual); break;
		case Uniform: individual = uniformCrossover(otherIndividual);   break;
		default: individual = this; break;
		}
		return individual;
	}

	private Individual onePointCrossover(Individual otherIndividual) {

		//Faz crossover de um ponto no cromosso de bias

		int cut = (int) Math.round(Math.random() * this.chromossome.length);

		List<Double> f1 = Arrays.asList(this.getChromossome());
		List<Double> f2 = Arrays.asList(otherIndividual.getChromossome());

		List<Double> s = new ArrayList<Double>();

		if (Math.random() < 0.5) {
			s.addAll(f1.subList(0, cut));
			s.addAll(f2.subList(cut, f2.size()));	
		}else {
			s.addAll(f2.subList(0, cut));
			s.addAll(f1.subList(cut, f1.size()));
		}

		Double[] son = new Double[s.size()];
		son = s.toArray(son);

		Individual individual = (Individual) this.clone();
		individual.setChromossome(son);

		if (this.flag) {

			cut = (int) Math.round(Math.random() * this.chromossome.length);

			f1 = Arrays.asList(this.getBooleanChromossome());
			f2 = Arrays.asList(otherIndividual.getBooleanChromossome());

			s = new ArrayList<Double>();

			if (Math.random() < 0.5) {
				s.addAll(f1.subList(0, cut));
				s.addAll(f2.subList(cut, f2.size()));	
			}else {
				s.addAll(f2.subList(0, cut));
				s.addAll(f1.subList(cut, f1.size()));
			}

			son = new Double[s.size()];
			son = s.toArray(son);

			individual.setBooleanChromossome(son);
		}

		return individual;
	}

	private Individual twoPointCrossover(Individual otherIndividual) {
		//Faz crossover de dois pontos no cromosso de bias

		int cut1 = (int) Math.round(Math.random() * this.chromossome.length);
		int cut2 = (int) Math.round(Math.random() * (this.chromossome.length - cut1) );

		cut2 += cut1;

		List<Double> f1 = Arrays.asList(this.getChromossome());
		List<Double> f2 = Arrays.asList(otherIndividual.getChromossome());

		List<Double> s = new ArrayList<Double>();


		if (Math.random() < 0.5) {
			s.addAll(f1.subList(0, cut1));
			s.addAll(f2.subList(cut1, cut2));
			s.addAll(f1.subList(cut2, f1.size()));	
		}else {
			s.addAll(f2.subList(0, cut1));
			s.addAll(f1.subList(cut1, cut2));
			s.addAll(f2.subList(cut2, f2.size()));
		}

		Double[] son = new Double[s.size()];
		son = s.toArray(son);

		Individual individual = (Individual) this.clone();
		individual.setChromossome(son);

		if (this.flag) {

			cut1 = (int) Math.round(Math.random() * this.booleanChromossome.length);
			cut2 = (int) Math.round(Math.random() * (this.booleanChromossome.length - cut1) );

			cut2 += cut1;

			f1 = Arrays.asList(this.getBooleanChromossome());
			f2 = Arrays.asList(otherIndividual.getBooleanChromossome());

			s = new ArrayList<Double>();

			if (Math.random() < 0.5) {
				s.addAll(f1.subList(0, cut1));
				s.addAll(f2.subList(cut1, cut2));
				s.addAll(f1.subList(cut2, f1.size()));	
			}else {
				s.addAll(f2.subList(0, cut1));
				s.addAll(f1.subList(cut1, cut2));
				s.addAll(f2.subList(cut2, f2.size()));
			}

			son = new Double[s.size()];
			son = s.toArray(son);

			individual.setBooleanChromossome(son);
		}

		return individual;
	}

	private Individual uniformCrossover(Individual otherIndividual) {

		List<Double> f1 = Arrays.asList(this.getChromossome());
		List<Double> f2 = Arrays.asList(otherIndividual.getChromossome());
		List<Double> s = new ArrayList<Double>();

		for (int i=0; i < f1.size(); i++) {
			if (Math.random() < 0.5)
				s.addAll(f1.subList(i, i + 1));
			else s.addAll(f2.subList(i, i + 1));
		}

		Double[] son = new Double[s.size()];
		son = s.toArray(son);
		Individual individual = (Individual) this.clone();
		individual.setChromossome(son);

		if (this.flag) {

			f1 = Arrays.asList(this.getBooleanChromossome());
			f2 = Arrays.asList(otherIndividual.getBooleanChromossome());
			s = new ArrayList<Double>();

			for (int i=0; i < f1.size(); i++) {
				if (Math.random() < 0.5)
					s.addAll(f1.subList(i, i + 1));
				else s.addAll(f2.subList(i, i + 1));
			}

			son = new Double[s.size()];
			son = s.toArray(son);

			individual.setBooleanChromossome(son);
		}

		return individual;
	}

	public void mutation(int currentGereneration) {

		int index = Util.getUniformIntegerDistribution(0, MutationStrategy.values().length-1);
		MutationStrategy strategy = MutationStrategy.values()[index];

		double probability = env.getInitialMutationProbability();

		switch(strategy) {
		case Random: randomMutation(probability); break;
		case NotUniform: notUniformMutation(currentGereneration, probability); break;
		default: break;
		}
	}

	private void randomMutation(double probability) {

		double lower = Collections.min(Arrays.asList(this.chromossome));
		double upper = Collections.max(Arrays.asList(this.chromossome));

		for (int i=0; i<this.chromossome.length; i++)
			if (Math.random() < probability)
				this.chromossome[i] = Util.getUniformRealDistribution(lower, upper);

		if (this.flag) {
			double lowerBoolean = Collections.min(Arrays.asList(this.booleanChromossome));
			double upperBoolean = Collections.max(Arrays.asList(this.booleanChromossome));

			for (int i=0; i<this.booleanChromossome.length; i++)
				if (Math.random() < probability)
					this.booleanChromossome[i] = Util.getUniformRealDistribution(lowerBoolean, upperBoolean);
		}
	}

	private void notUniformMutation(int currentGeneration, double probability) {

		double aux = 0.0;
		int i=0;
		int generationsSize = env.getGenerationSize();
		double lowerBound = env.getInitialGeneRange();
		double upperBound = env.getFinalGeneRange();


		while (i<this.chromossome.length) {

			if (Math.random() < probability) {
				double tau = Math.random();
				if (tau < 0.5) 
					aux = this.chromossome[i] + this.delta(currentGeneration, (upperBound-this.chromossome[i]), generationsSize, 10);
				else aux = this.chromossome[i] - this.delta(currentGeneration, (this.chromossome[i]-lowerBound), generationsSize, 10);

				this.chromossome[i] = aux;
			}
			i++;	
		}

		if (this.flag) {
			generationsSize = env.getGenerationSize();
			double lowerBoundBoolean = 0.0;
			double upperBoundBoolean = 1.0;

			while (i<this.booleanChromossome.length) {

				if (Math.random() < probability) {
					int tau = Util.getUniformIntegerDistribution(0, 1);

					if (tau == 0) {
						aux = this.booleanChromossome[i] + this.delta(currentGeneration, (upperBoundBoolean-this.booleanChromossome[i]), generationsSize, 10);
					}else {
						aux = this.booleanChromossome[i] - this.delta(currentGeneration, (this.booleanChromossome[i]-lowerBoundBoolean), generationsSize, 10);
					}

					this.booleanChromossome[i] = aux;
				}
				i++;	
			}

		}
	}

	private Double delta(int t, double y, double gmax, double b) {
		double r = Math.random();
		double z = Math.pow(1-(t/gmax),b);
		return y * (1 - Math.pow(r, z));
	}

	public Double[] getChromossome() {
		return chromossome;
	}

	public void setChromossome(Double[] chromossome) {
		this.chromossome = chromossome;
	}

	public Double[] getBooleanChromossome() {
		return booleanChromossome;
	}

	public void setBooleanChromossome(Double[] booleanChromossome) {
		this.booleanChromossome = booleanChromossome;
	}

	public CREEnv getEnv() {
		return env;
	}

	public void setEnv(CREEnv env) {
		this.env = env;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(Individual o) {
		if (this.result.getEvaluation().doubleValue() == o.result.getEvaluation().doubleValue() )
			return 0;
		else if (this.result.getEvaluation().doubleValue() > o.result.getEvaluation().doubleValue() )
			return 1;
		else return -1;
	}
}