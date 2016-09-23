import java.util.Random;

public interface Input<I>{
	public void randomize(Random rand);

	public void copy(I in);
}