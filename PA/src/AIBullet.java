
public final class AIBullet {
	public int locX, locY, direct, speedX, speedY, owner;
	
	public AIBullet(Bullet bullet) {
		locX = (int)Math.round(bullet.getLocX());
		locY = (int)Math.round(bullet.getLocY());
		direct = (int)Math.round(bullet.getDirectRad() * 180.0 / Math.PI);
		speedX = (int)Math.round(bullet.getSpeed() * bullet.getDirX());
		speedY = (int)Math.round(bullet.getSpeed() * bullet.getDirY());
		owner = bullet.getOwner().getId();
	}
}