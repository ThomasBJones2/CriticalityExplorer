package RandComp;

public interface Experiment <I,E extends Experiment<I,E>> extends AbstractDistance{
	public void experiment(I input);

	public static Experiment emptyObject(){return null;}
	public Score[] scores(E correctObject);
}
