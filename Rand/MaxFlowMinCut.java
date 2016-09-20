import java.util.*;

public class MaxFlowMinCut{

	double maxFlowFF(Graph inGraph){
		auxFlow = 1;
		double out = 0;		
		while(auxFlow > 0) {
			int auxFlow = inGraph.findPathValue();		
			out += auxFlow;
			inGraph.deduct(auxFlow);
		}
		return out;
	}
}
