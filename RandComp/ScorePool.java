package RandComp;

import java.math.BigInteger;
import java.math.*;

public class ScorePool{
		public static Score absoluteValue(double error, double correct){ 
			return new Score(Math.abs(error - correct), "Absolute_Value"); 
		}

		public static Score absolutePercentValue(double error, double correct){
			
			return new Score(correct > 0?
			Math.abs(error - correct)/correct:0, 
			"Absolute_Percent_Value"); 
		}

		public static Score absoluteValueBigInteger(BigInteger error, BigInteger correct){ 
			if(error != null && correct != null) {
				double score = error.subtract(correct).abs().doubleValue();
				return new Score(score, 
					"Absolute_Value"); 
			} else {
				return new Score(0, "Absolute_Value");
			}
		}

		public static Score logarithmAbsoluteValueBigInteger(BigInteger error, BigInteger correct){ 
			
			if (error != null && correct != null) {
				return new Score(error.subtract(correct).abs().doubleValue() != 0?
					Math.log((error.subtract(correct)).abs().doubleValue()):0, 
					"Absolute_Logarithm_Value"); 
			} else {
				return new Score(0, "Absolute_Logarithm_Value");
			}
		}


		public static Score absolutePercentValueBigInteger(BigInteger error, BigInteger correct){
			return new Score(correct.doubleValue() != 0?
			(((error.subtract(correct)).abs()).divide(correct.abs())).doubleValue():0, 
			"Absolute_Percent_Value"); 
		}


		public static Score manhattanDistance(String error, String correct){
			String name = "Manhattan_Distance";
			double errorOut = StringDistances.manhattanDistance(error, correct);
			return new Score(errorOut, name);
		}

		public static Score symbolDistance(String error, String correct){
			String name = "Symbol_Distance";
			double errorOut = StringDistances.symbolDistance(error, correct);
			return new Score(errorOut, name);
		}

		public static Score nullScore(){
			return new Score(0,"null_score");
		}

		public static Score nullScore(Exception e){
			return new Score(0,e.toString());
		}

}
