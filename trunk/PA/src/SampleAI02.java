import java.util.Random;

/**not complete*/
public class SampleAI02 extends AI
{
	Random random = new Random();
	int became_1hp_time = -1; 
	@Override
	public void run()
	{
		AIPlayer me = getPlayer( myId );
		int bullet_num = getNumBullets();
		
		/**decide direction*/
		int dir_num = 0;
		int next_dir[] = new int[10];
		
		int min_hit_num = Tool.infinity;
		for( int dir = 0 ; dir <= 8 ; dir++ )
		{
			double x = Tool.getNextPositionX( dir, me.speed, me.locX, 15 );
			double y = Tool.getNextPositionY( dir, me.speed, me.locY, 15 );
			int hit_num = 0;
			for( int i = 1 ; i <= bullet_num ; i++ )
    		{
    			if( Tool.distance( x, y, getBullet(i).locX, getBullet(i).locY ) < 30 )
    				hit_num++;
    		}
			if( hit_num == min_hit_num )
			{
				next_dir[dir_num] = dir;
				dir_num++;
			}
			if( hit_num < min_hit_num )
			{
				min_hit_num = hit_num;
				next_dir[0] = dir;
				dir_num = 1;
			}
		}
		/*
		int max_hit_time = 0;
		for( int dir = 0 ; dir <= 8 ; dir++ )
		{
			double x = Tool.getNextPositionX( dir, me.speed, me.locX, 10 );
			double y = Tool.getNextPositionY( dir, me.speed, me.locY, 10 );
			int hit_time = Tool.infinity;
			for( int i = 1 ; i <= bullet_num ; i++ )
    		{
    			int tmp = Tool.WhenHit(x, y, getBullet(i));
    			
    			if( tmp < hit_time )
    				hit_time = tmp;
    		}
			if( hit_time == max_hit_time )
			{
				next_dir[dir_num] = dir;
				dir_num++;
			}
			if( hit_time > max_hit_time )
			{
				max_hit_time = hit_time;
				next_dir[0] = dir;
				dir_num = 1;
			}
		}
		*/
		move(next_dir[random.nextInt(dir_num)]);
		
		//move(random.nextInt(9));
		
		/**after became 1hp, wait 1 second(no damage time), open shield*/
		if( me.life == 1 )
		{
			if( became_1hp_time == -1 )
				became_1hp_time = getTime();
			if( getTime() - became_1hp_time == 1000 )
				useSkill( 1, myId );
		}
		
		
	}
	
}
