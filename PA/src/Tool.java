public class Tool {
	public final static int infinity = 100000000;
	
	public static double distance(GameObject a, GameObject b) {
		return distance(a.getLocX(), a.getLocY(), b.getLocX(), b.getLocY());
	}
	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x1-x2, dy = y1-y2;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * count time which one bullet takes to hit the player of the coordinate
	 * not exactly, because not depend on bullet's size
	 */
	public static int WhenHit( double x, double y, AIBullet bullet )
	{
		int rad1 = (int)GraphicsEngine.PLAYER_SIZE/2; // player's size
		//int rad2 = 5; // bullet's size
		
		/**count point to line's distance*/
		/*
		aX+bY = 0
		X = bullet.LocX + t*bullet.speedX;
		Y = bullet.LocY + t*bullet.speedY;
		*/
		double dx = x-bullet.locX, dy = y-bullet.locY;
		
		double speed = Math.sqrt( bullet.speedX*bullet.speedX + bullet.speedY*bullet.speedY );
		double ptl = Math.abs( ( bullet.speedY*( dx )-bullet.speedX*( dy ) )/speed );
		
		if( ptl > rad1 ) return infinity; //never hit
		if(dx*bullet.speedX+dy*bullet.speedY<0) return Tool.infinity; // never hit if leaving
		
		double string = Math.sqrt( rad1*rad1 - ptl*ptl );
		double dis = Math.sqrt( (dx)*(dx)+(dy)*(dy));
		double hit_dis = Math.sqrt(dis*dis-ptl*ptl)-string;
		return (int)(hit_dis/speed*1000);
	}
	
	public static double getNextPositionX( int dir, double speed, double x_now, int time )
	{
		if( dir == 0 ) return x_now;
		return x_now+Math.cos((dir-1)*Math.PI/4)*speed*time/1000;
	}
	
	public static double getNextPositionY( int dir, double speed, double y_now, int time )
	{
		if( dir == 0 ) return y_now;
		return y_now+Math.sin((dir-1)*Math.PI/4)*speed*time/1000;
	}
	
	public static boolean IsUfoInScreen( double x, double y )
	{
		return (x >= GraphicsEngine.PLAYER_SIZE/2
				&& x <= GameInfo.FIELD_WIDTH-GraphicsEngine.PLAYER_SIZE/2
				&& y >= GraphicsEngine.PLAYER_SIZE/2
				&& y <= GameInfo.FIELD_HEIGHT-GraphicsEngine.PLAYER_SIZE/2);
	}
}
