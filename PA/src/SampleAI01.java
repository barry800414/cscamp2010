
public class SampleAI01 extends AI {
	public void run() {
		AIPlayer myself = getPlayer(myId);
		int bulletsnum = getNumBullets();
		AIBullet bullet ;
		AIBullet nearest_bullet = null;
		double shortest_dis = -1;
		double dis;
		
		for(int i=1;i<=bulletsnum;i++){
			bullet = getBullet(i);
			if(bullet==null){
				System.out.println("Test");
				continue;
			}
				
			if(shortest_dis == -1){
				shortest_dis = Math.sqrt( Math.pow(bullet.locX - myself.locX ,2)+ Math.pow(bullet.locY - myself.locY,2) );
				nearest_bullet = bullet;
			}
			else{
				dis = Math.sqrt( Math.pow(bullet.locX - myself.locX ,2)+ Math.pow(bullet.locY - myself.locY,2) );
				if(dis < shortest_dis ){
					shortest_dis = dis;
					nearest_bullet = bullet;
				}
			}
		}
		
		double dirx,diry,angle;
		if(shortest_dis == -1)
			move(0);
		
		if(nearest_bullet != null){
			dirx = myself.locX - nearest_bullet.locX ;
			diry = myself.locY - nearest_bullet.locY ;
			angle = Math.atan(Math.abs(diry / dirx))/Math.PI *180;
			if(angle < 22.5)
				move(dirx >= 0 ? 1 : 5);
			else if(angle > 67.5)
				move(diry >= 0 ? 3 : 7);
			else{
				if(dirx >= 0){
					if(diry >= 0 )  move(2);
					else  move(8);
				}
				else{
					if(diry >= 0)   move(4);
					else  move(6);
				}
			}
		}
		if(myself.life <= 2 && getSkillQuota(2)>=1)
			useSkill(2,myId);
		
	}
}
