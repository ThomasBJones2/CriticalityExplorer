package RandComp;

import java.math.BigInteger;
import java.math.BigDecimal;
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
			BigInteger newCorrect = (correct == null)?BigInteger.ZERO:correct;
			double score = newError.subtract(newCorrect).abs().doubleValue();
			return new Score(score, "Absolute_Value"); 
		}

		public static Score logarithmAbsoluteValueBigInteger(BigInteger error, BigInteger correct){ 
			BigInteger newError = (error == null)?BigInteger.ZERO:error;
			BigInteger newCorrect = (correct == null)?BigInteger.ZERO:correct;
			return new Score(
				Math.log((newError.subtract(newCorrect)).abs().doubleValue() + 1.0), 
				"Absolute_Logarithm_Value"); 
		}


		public static Score absolutePercentValueBigInteger(BigInteger error, BigInteger correct){
			double newError = (error == null)?0.0:error.doubleValue();
			double newCorrect = (correct == null)?0.0:correct.doubleValue();
			return new Score(
			Math.abs(newError - newCorrect)/Math.abs(newCorrect + 1.0), 
			"Absolute_Percent_Value"); 
		}


		public static Score manhattanDistance(String error, String correct){
			String name = "Manhattan_Distance";
			String newError = (error == null)?"":error;
			String newCorrect = (correct == null)?"":correct;
			double errorOut = StringDistances.manhattanDistance(newError, newCorrect);
			return new Score(errorOut, name);
		}

		public static Score symbolDistance(String error, String correct){
			String name = "Symbol_Distance";
			String newError = (error == null)?"":error;
			String newCorrect = (correct == null)?"":correct;
			double errorOut = StringDistances.symbolDistance(newError, newCorrect);
			return new Score(errorOut, name);
		}

		public static Score nullScore(){
			return new Score(0,"null_score");
		}

		public static Score nullScore(Exception e){
			return new Score(0,e.toString());
		}

}
