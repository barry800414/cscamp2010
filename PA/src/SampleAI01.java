import javax.swing.JFrame;

public class SampleAI01 extends AI {
	private Game game;
	private Keyboard kb;
	private boolean ready;
	
	public SampleAI01(Game game) {
		this.game = game;
		init();
	}

	/** Do a late init, due to the main frame may not have been created on instance creation. */
	private boolean init() {
		if(!ready && game != null) {
			GraphicsEngine ge = this.game.getGraphicsEngine();
			if(ge != null) {
				JFrame main_scr = ge.getMainFrame();
				if(main_scr != null) {
					this.kb = new Keyboard();
					main_scr.addKeyListener(kb);
					main_scr.addFocusListener(kb);
					
					ready = true;
				}
			}
		}
		return ready;
	}
	
	public void run() {
		//System.out.println("AIHuman: run()");
		
		if(!ready && !init()) return;
		
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
