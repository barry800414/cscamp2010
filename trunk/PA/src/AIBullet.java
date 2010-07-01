
public final class AIBullet {
	public int direct, owner;
	public double locX, locY, speedX, speedY, speed;
	
	public AIBullet(Bullet bullet) {
		locX = bullet.getLocX();
		locY = bullet.getLocY();
		speedX = bullet.getSpeed() * bullet.getDirX();
		speedY = bullet.getSpeed() * bullet.getDirY();
		speed = bullet.getSpeed();
		
		Player bullet_owner = bullet.getOwner();
		owner = (bullet_owner == null) ? 0 : bullet_owner.getId();
	}
}