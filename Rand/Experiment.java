public interface Experiment <I,E extends Experiment<I,E>> extends AbstractDistance{
	public void experiment(I input);

	public Score[] scores(E correctObject);
}
