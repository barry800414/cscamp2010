
/**not complete*/
public class SampleAI02 extends AI
{
	
	int became_1hp_time = -1; 
	@Override
	public void run()
	{
		AIPlayer me = getPlayer( myId );
		int bullet_num = getNumBullets();
		int hit_time = Tool.infinity;
		
		for( int i = 1 ; i <= bullet_num ; i++ )
		{
			int tmp = Tool.WhenHit(me.locX, me.locY, getBullet(i));
			if( tmp < hit_time )
				hit_time = tmp;
		}
		
		System.out.println( "hit_time = " + hit_time );
		
		/**after became 1hp, wait 1 second(no damage time), open shield*/
		if( me.life == 1 )
		{
			if( became_1hp_time == -1 )
				became_1hp_time = getTime();
			if( getTime() - became_1hp_time == 1000 )
			{
				//why can't work?
				//System.out.println( "rock's test" );
				useSkill( 1, myId );
				//useSkill( 2, myId );
			}
		}
		
		
	}
	
}
