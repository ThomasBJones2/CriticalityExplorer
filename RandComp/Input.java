package RandComp;

import java.util.Random;
import java.util.ArrayList;

public interface Input<I extends Input<I>> extends AbstractLocation{
	public void randomize(Random rand);

	public void copy(I in);

	public static Input emptyObject(){return null;}
	public static Input newInputOfSize(int n){return null;}
}
