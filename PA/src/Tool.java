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
		double rad1 = GraphicsEngine.PLAYER_SIZE/2+GraphicsEngine.BULLET_SIZE/2; // player's size
		//int rad2 = 5; // bullet's size
		
		/**count point to line's distance*/
		
		double dx = x-bullet.locX, dy = y-bullet.locY;
		
		double speed = bullet.speed;
		double ptl = Math.abs( ( bullet.speedY*( dx )-bullet.speedX*( dy ) )/speed );
		
		if( ptl > rad1 ) return infinity; //never hit
		
		double s2 = dx*dx+dy*dy;
		if(s2 < rad1*rad1) return 0; // already hit
		if(dx*bullet.speedX+dy*bullet.speedY<0) return Tool.infinity; // never hit if leaving
		
		double hit_dis = Math.sqrt( s2 - ptl*ptl ) - Math.sqrt( rad1*rad1 - ptl*ptl );
		
		return (int)(hit_dis/speed*1000);
	}
	
	/**
	 * enhanced version of old WhenHit, with the consideration of the player's velocity. 
	 */
	public static int WhenHit( AIPlayer p, AIBullet b )
	{
		double rad1 = GraphicsEngine.PLAYER_SIZE/2+GraphicsEngine.BULLET_SIZE/2; // player's size
		//int rad2 = 5; // bullet's size
		
		/**count point to line's distance*/
		
		double vx = b.speedX-p.speedX, vy = b.speedY-p.speedY;
		double dx = p.locX-b.locX, dy = p.locY-b.locY;
		
		double speed = Math.sqrt(vx*vx+vy*vy);
		double ptl = Math.abs( ( vy*( dx )-vx*( dy ) )/speed );
		
		if( ptl > rad1 ) return infinity; //never hit
		
		double s2 = dx*dx+dy*dy;
		if(s2 < rad1*rad1) return 0; // already hit
		if(dx*vx+dy*vy<0) return Tool.infinity; // never hit if leaving
		
		double hit_dis = Math.sqrt( s2 - ptl*ptl ) - Math.sqrt( rad1*rad1 - ptl*ptl );
		
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
		return y_now-Math.sin((dir-1)*Math.PI/4)*speed*time/1000;
	}
	
	public static boolean IsUfoInScreen( double x, double y )
	{
		return (x >= GraphicsEngine.PLAYER_SIZE/2
				&& x <= GameInfo.FIELD_WIDTH-GraphicsEngine.PLAYER_SIZE/2
				&& y >= GraphicsEngine.PLAYER_SIZE/2
				&& y <= GameInfo.FIELD_HEIGHT-GraphicsEngine.PLAYER_SIZE/2);
	}
}
