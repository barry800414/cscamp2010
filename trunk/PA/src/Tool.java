
public class Tool {
	public static double distance(GameObject a, GameObject b) {
		return distance(a.getLocX(), a.getLocY(), b.getLocX(), b.getLocY());
	}
	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x1-x2, dy = y1-y2;
		return Math.sqrt(dx*dx + dy*dy);
	}
}
