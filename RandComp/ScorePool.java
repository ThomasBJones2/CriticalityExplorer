package RandComp;

import java.math.BigInteger;
import java.math.*;

public class ScorePool{
		public static Score absoluteValue(double error, double correct){ 
			return new Score(Math.abs(error - correct), "Absolute_Value"); 
		}

		public static Score absolutePercentValue(double error, double correct){
			
			return new Score(Math.abs(error - correct)/Math.abs(correct + 1), 
			"Absolute_Percent_Value"); 
		}

		public static Score absoluteValueBigInteger(BigInteger error, BigInteger correct){ 
			BigInteger newError = (error == null)?BigInteger.ZERO:error;
			BigInteger newCorrect = (correct == null)?BigInteger.ZERO:correct);
			double score = error.subtract(correct).abs().doubleValue();
			return new Score(score, "Absolute_Value"); 
		}

		public static Score logarithmAbsoluteValueBigInteger(BigInteger error, BigInteger correct){ 
			BigInteger newError = (error == null)?BigInteger.ZERO:error;
			BigInteger newCorrect = (correct == null)?BigInteger.ZERO:correct);
			return new Score(
				Math.log((error.subtract(correct).add(BigInteger.ONE)).abs().doubleValue()), 
				"Absolute_Logarithm_Value"); 
		}


		public static Score absolutePercentValueBigInteger(BigInteger error, BigInteger correct){
			BigInteger newError = (error == null)?BigInteger.ZERO:error;
			BigInteger newCorrect = (correct == null)?BigInteger.ZERO:correct);
			return new Score(
			(((error.subtract(correct)).abs()).divide(correct.abs().add(BigInteger.ONE))).doubleValue(), 
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
