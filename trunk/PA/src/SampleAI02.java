
/**not complete*/
public class SampleAI02 extends AI
{
	
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
				//useSkill( 2, myId );
			}
		}
	}
	
	
	/**
	 * count time which one bullet takes to hit the player of the coordinate
	 * not exactly, because not depend on bullet's size
	 */
	
}
