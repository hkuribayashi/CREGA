package br.unifesspa.cre.core;

import br.unifesspa.cre.config.CREEnv;
import br.unifesspa.cre.config.Param;
import br.unifesspa.cre.model.Result;

public class Main {

	public static void main(String[] args) {

		String path = "/Users/hugo/Desktop/CRE/CREV1/";
		if (args.length != 0)
			path = args[0];

		CREEnv env = new CREEnv();

		//Setting general simulations parameters
		env.set(Param.area, 1000000.0); 	   // 1 km^2
		env.set(Param.lambdaFemto, 0.00002);   // 0.00002 Femto/m^2 = 20 Femtos  
		env.set(Param.lambdaUser, 0.0003);     // 0.0003 Users/m^2 = 300 Users 
		env.set(Param.lambdaMacro, 0.000002);  // 0.000002 Macros/m^2 = 2 Macros
		env.set(Param.powerMacro, 46.0);	   // dBm
		env.set(Param.powerSmall, 23.0);	   // dBm
		env.set(Param.noisePower, -174.0);	   // dBm/Hz
		env.set(Param.gainMacro, 15.0);		   // dBi
		env.set(Param.gainSmall, 5.0);		   // dBi
		env.set(Param.nSubcarriers, 12);	   // 12 Sub-carriers per Resource Block
		env.set(Param.nOFDMSymbols, 14);	   // 14 OFDM Symbols per subframe
		env.set(Param.subframeDuration, 1.0);  // 1 ms
		env.set(Param.workingDirectory, path); // Working Directory
		env.set(Param.simulations, 50.0);	   // Number of Simulations

		//Setting GA Parameters
		env.set(Param.initialCrossoverProbability, 0.9);
		env.set(Param.finalCrossoverProbability, 0.7);
		env.set(Param.initialMutationProbability, 0.7);
		env.set(Param.finalMutationProbability, 0.9);
		env.set(Param.populationSize, 80);
		env.set(Param.generationSize, 200);
		env.set(Param.kElitism, 2);
		env.set(Param.initialGeneRange, -10.0);
		env.set(Param.finalGeneRange, 80.0);
		
		Engine e = new Engine(env);
		
		System.out.println("Experiment 01: No Bias");
		System.out.println();
		Result r1 = e.execUnifiedBias(0.0);
		System.out.println(r1);
		System.out.println();
		
		System.out.println("Experiment 02: Bias 10.0 dBm");
		System.out.println();
		Result r2 = e.execUnifiedBias(20.0);
		System.out.println(r2);
		System.out.println();
		
		System.out.println("Experiment 03: Bias 30.0 dBm");
		System.out.println();
		Result r3 = e.execUnifiedBias(30.0);
		System.out.println(r3);
		System.out.println();
		
		System.out.println("Experiment 04: Bias 50.0 dBm");
		System.out.println();
		Result r4 = e.execUnifiedBias(50.0);
		System.out.println(r4);
		System.out.println();
		
		System.out.println("Experiment 05: GA");
		System.out.println();	
		Result r5 = e.getGA();
		System.out.println(r5);
		System.out.println();
		
		System.out.println("Experiment 06: GAOnOff");
		System.out.println();	
		Result r6 = e.getGAOnOff();
		System.out.println(r6);
	}
}