public class ScorePool{
		static Score AbsoluteValue(double error, double correct){ 
			return new Score(Math.abs(error - correct), "Absolute_Value"); 
		}

		static Score AbsolutePercentValue(double error, double correct){
			return new Score(correct > 0?
			Math.abs(error - correct)/correct:0, 
			"Absolute_Percent_Value"); 
		}
}
