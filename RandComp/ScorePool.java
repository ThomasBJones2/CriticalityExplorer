package RandComp;

public class ScorePool{
		public static Score absoluteValue(double error, double correct){ 
			return new Score(Math.abs(error - correct), "Absolute_Value"); 
		}

		public static Score absolutePercentValue(double error, double correct){
			return new Score(correct > 0?
			Math.abs(error - correct)/correct:0, 
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
}
