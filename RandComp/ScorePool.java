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
			double out = Math.log((newError.subtract(newCorrect)).abs().doubleValue() + 1.0);
			return new Score(
				out,
				"Absolute_Logarithm_Value"); 
		}


		public static Score absolutePercentValueBigInteger(BigInteger error, BigInteger correct){
			BigDecimal newError = (error == null)?BigDecimal.ZERO:new BigDecimal(error);
			BigDecimal newCorrect = (correct == null)?BigDecimal.ZERO:new BigDecimal(correct);
			BigDecimal out = newError.subtract(newCorrect).
				divide(newCorrect.add(BigDecimal.ONE),90,BigDecimal.ROUND_HALF_UP).abs();
			return new Score(
				out.doubleValue(),
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


		public static Score frobeniusNorm(Matrix error, Matrix correct){
			String name = "Frobenius_Norm";
			double errorOut = correct.frobeniusNorm(error);
			return new Score(errorOut, name);
		}

		public static Score infinityNorm(Matrix error, Matrix correct){
			String name = "Infinity_Norm";
			double errorOut = correct.infinityNorm(error);
			return new Score(errorOut, name);
		}



		public static Score nullScore(){
			return new Score(0,"null_score");
		}

		public static Score nullScore(Exception e){
			return new Score(0,e.toString());
		}


}
