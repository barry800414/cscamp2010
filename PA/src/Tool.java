public class Tool {
	final static double infinity = 100000;
	
	public static double distance(GameObject a, GameObject b) {
		return distance(a.getLocX(), a.getLocY(), b.getLocX(), b.getLocY());
	}
	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x1-x2, dy = y1-y2;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public static double WhenHit( int x, int y, AIBullet bullet )
	{
		int rad1 = 25; // player's size
		//int rad2 = 5; // bullet's size
		
		/**count point to line's distance*/
		/*
		aX+bY = 0
		X = bullet.LocX + t*bullet.speedX;
		Y = bullet.LocY + t*bullet.speedY;
		*/
		double speed = Math.sqrt( bullet.speedX*bullet.speedX + bullet.speedY*bullet.speedY );
		double ptl = Math.abs( ( bullet.speedY*( x-bullet.locX )-bullet.speedX*( y-bullet.locY ) )/speed );
		
		if( ptl > rad1 ) return infinity; //never hit
		
		double string = Math.sqrt( rad1*rad1 - ptl*ptl );
		double dis = Math.sqrt( (x-bullet.locX)*(x-bullet.locX)+(y-bullet.locY)*(y-bullet.locY));
		double hit_dis = Math.sqrt(dis*dis-ptl*ptl)-string;
		return hit_dis/speed;
	}
	
	public static double getNextPositionX( int dir, double speed, double x_now, int time )
	{
		if( dir == 0 ) return x_now;
		return (dir-1)*;
	}
}