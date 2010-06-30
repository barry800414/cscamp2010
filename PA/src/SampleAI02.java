
/**not complete*/
public class SampleAI02 extends AI
{
	final int infinity = 100000;
	int became_1hp_time = -1; 
	@Override
	public void run()
	{
		AIPlayer me = getPlayer( myId );
		int bullet_num = getNumBullets();
		
		/**after became 1hp, wait 1 second(no damage time), open shield*/
		if( me.life == 1 )
		{
			if( became_1hp_time == -1 )
				became_1hp_time = getTime();
			if( getTime() - became_1hp_time == 2000 )
			{
				//why can't work?
				//System.out.println( "rock's test" );
				useSkill( 1, myId );
				useSkill( 2, myId );
			}
		}
	}
	
	
	/**
	 * count time which one bullet takes to hit the player of the coordinate
	 * not exactly, because not depend on bullet's size
	 */
	double WhenHit( int x, int y, AIBullet bullet )
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
}
