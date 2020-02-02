package br.unifesspa.cre.hetnet;

import br.unifesspa.cre.config.CREEnv;

public class HetNet {

	private static Scenario uniqueInstance;
	
	public static synchronized Scenario getInstance(CREEnv env) {
		if (uniqueInstance == null)
			uniqueInstance = new Scenario(env);
		
		return uniqueInstance;
	}
}