package br.unifesspa.cre.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.unifesspa.cre.hetnet.Scenario;
import br.unifesspa.cre.model.Result;

public class GA{

	protected Scenario scenario;

	protected Integer populationSize;

	protected List<Individual> population;

	protected Integer generationsSize;
	
	protected Individual bestIndividual;
	
	protected double[] crossoverProbability;
	
	protected double[] mutationProbability;

	public GA(Scenario scenario, Boolean flag) {
		this.scenario = scenario;
		this.populationSize = this.scenario.getEnv().getPopulationSize();
		this.generationsSize = this.scenario.getEnv().getGenerationSize();

		this.population = new ArrayList<Individual>();
		
		for (int i=0; i<populationSize; i++)
			this.population.add(new Individual(this.scenario.getEnv(), flag));

		this.bestIndividual = new Individual(this.scenario.getEnv(), flag);
		this.evaluateIndividual(this.bestIndividual);
		this.scenario.reset();
		
		double bC = this.scenario.getEnv().getInitialCrossoverProbability();
		double aC = (this.scenario.getEnv().getFinalCrossoverProbability() - bC)/this.generationsSize;
		
		double bM = this.scenario.getEnv().getInitialMutationProbability();
		double aM = (this.scenario.getEnv().getFinalMutationProbability() - bM)/this.generationsSize;
		
		this.crossoverProbability = new double[this.generationsSize];
		this.mutationProbability = new double[this.generationsSize];
		
		for (int i=0; i<crossoverProbability.length; i++) {
			this.crossoverProbability[i] = (aC*i)+bC;
			this.mutationProbability[i] = (aM*i)+bM;
		}
	}

	public void evaluateIndividual(Individual individual) {
		this.scenario.setBias(individual.getChromossome());
		this.scenario.evaluation();
		Result r = this.scenario.getResult();
		individual.setResult( (Result) r.clone() );
	}

	public Double evaluate() {
		Double sum = 0.0;
		int i=0;
		while (i<this.population.size()) {
			this.evaluateIndividual(this.population.get(i));
			sum += this.population.get(i).getResult().getEvaluation();
			i++;
			this.scenario.reset();
		}
		
		Comparator<Individual> c = Collections.reverseOrder(); 
		Collections.sort(this.population, c);
		return sum;
	}

	public void evolve() {
		int currentGeneration = 0;
		int kElitismSize = this.scenario.getEnv().getkElitism();
		List<Individual> kElitism = new ArrayList<Individual>();
				
		while (currentGeneration < this.scenario.getEnv().getGenerationSize()) {
			
			Double sum = this.evaluate();
	
			List<Individual> newPopulation = new ArrayList<Individual>();
			for (int k=0; k<kElitismSize; k++) {
				kElitism.add(this.population.get(k));
				this.population.remove(k);
			}
			
			sum = this.evaluate();
			
			geneticOperators(currentGeneration, sum, newPopulation);
			
			newPopulation.addAll(kElitism);
			this.setPopulation(newPopulation);
			this.evaluate(); 
			
			if (this.getBestIndividual().getResult().getEvaluation() < this.getPopulation().get(0).getResult().getEvaluation()) {
				this.setBestIndividual(this.getPopulation().get(0));
				
				this.evaluateIndividual(this.getBestIndividual());
				
				System.out.println("Generation: "+currentGeneration);
				System.out.println("Solution: "+this.getBestIndividual().getResult().getEvaluation());
				System.out.println("UEs served: "+this.getBestIndividual().getResult().getUesServed());
				System.out.println("Serving BSs: "+this.getBestIndividual().getResult().getServingBSs());
				System.out.println("Sum Rate: "+this.getBestIndividual().getResult().getSumRate());
				System.out.println("Median Rate: "+this.getBestIndividual().getResult().getMedianRate());
				
				this.scenario.debug();
				this.scenario.reset();
			}
			currentGeneration++;
		}
	}

	private void geneticOperators(int currentGeneration, Double sum, List<Individual> newPopulation) {
		double crossoverProbability = this.crossoverProbability[currentGeneration]; 
		double mutationProbability = this.mutationProbability[currentGeneration];
		
		for (int i = 0; i < this.populationSize; i++) {
			
			int f1 = this.roulette(sum);
			int f2 = this.roulette(sum);
			
			while (f1 == f2)
				f2 = this.roulette(sum);
			
			Individual individual = null;
			
			if (Math.random() < crossoverProbability ) {
				individual = this.population.get(f1).crossover(this.population.get(f2));
				if (Math.random() < mutationProbability)
					individual.mutation(currentGeneration);
			} else if (Math.random() < mutationProbability ) {
				individual = this.population.get(f1);
				individual.mutation(currentGeneration);
			}else individual = this.population.get(f1);
			
			newPopulation.add(individual);
		}
	}

	private int roulette(Double evaluationSum) {
		int father = -1;
		Double value = Math.random() * evaluationSum;
		Double sum = 0.0;
		int i = 0;
		while (i < this.population.size() && sum < value) {
			sum += this.population.get(i).getResult().getEvaluation();
			father += 1;
			i += 1;
		}
		return father;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public Integer getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(Integer populationSize) {
		this.populationSize = populationSize;
	}

	public List<Individual> getPopulation() {
		return population;
	}

	public void setPopulation(List<Individual> population) {
		this.population = population;
	}

	public Integer getGenerationsSize() {
		return generationsSize;
	}

	public void setGenerationsSize(Integer generationsSize) {
		this.generationsSize = generationsSize;
	}

	public Individual getBestIndividual() {
		return bestIndividual;
	}

	public void setBestIndividual(Individual bestIndividual) {
		this.bestIndividual = bestIndividual;
	}

	public double[] getCrossoverProbability() {
		return crossoverProbability;
	}

	public void setCrossoverProbability(double[] crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	public double[] getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double[] mutationProbability) {
		this.mutationProbability = mutationProbability;
	}
}