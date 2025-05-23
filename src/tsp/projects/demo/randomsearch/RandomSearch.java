//package tsp.projects.demo.randomsearch;
//
//import tsp.evaluation.Evaluation;
//import tsp.evaluation.Path;
//import tsp.projects.InvalidProjectException;
//import tsp.projects.DemoProject;
//
///**
// * @author Alexandre Blansché
// * Recherche aléatoire
// */
//public class RandomSearch extends DemoProject
//{
//	private int length;
//
//	/**
//	 * Méthode d'évaluation de la solution
//	 * @param evaluation
//	 * @throws InvalidProjectException
//	 */
//	public RandomSearch (Evaluation evaluation) throws InvalidProjectException
//	{
//		super (evaluation);
//		this.addAuthor ("Alexandre Blansché");
//		this.setMethodName ("Random Search");
//	}
//
//	@Override
//	public void initialization ()
//	{
//		this.length = this.problem.getLength ();
//		Path path = new Path (this.length);
//		this.evaluation.evaluate(path);
//	}
//
//	@Override
//	public void loop ()
//	{
//		Path path = new Path (this.length);
//		this.evaluation.evaluate(path);
//	}
//}
