import java.util.PriorityQueue;
import java.util.Comparator;

public class GameQueue {
	private static Comparator<Event> cmp_event = new Comparator<Event>() {
		public int compare(Event a, Event b) {
			long ta = a.getTime(), tb = b.getTime();
			if(ta < tb) return -1;
			else if(ta > tb) return 1;
			else return 0;
		}
	};
	private PriorityQueue<Event> queue = new PriorityQueue<Event>(1, cmp_event);
	
	public void addEvent(Event ev) {
		queue.add(ev);
	}
	
	/** Get the event with least time without removing, null if nothing left. */
	public Event peekNext() {
		return queue.peek();
	}
	
	/** Get the event with least time and remove it, null if nothing left. */
	public Event getNext() {
		return queue.poll();
	}
	
	/** Utility method to let scheduling effect addition faster */
	public void enqueueAddEffect(final Player player, final Effect effect, final long time) {
		addEvent(new Event() {
			public long getTime() { return time; }
			public void action() { player.addEffect(effect); }
		});
	}
	
	/** Utility method to let scheduling effect removal faster */
	public void enqueueRemoveEffect(final Player player, final Effect effect, final long time) {
		addEvent(new Event() {
			public long getTime() { return time; }
			public void action() { player.removeEffect(effect); }
		});
	}
	
	/** Main for self-test */
	public static void main(String[] args) {
		GameQueue q = new GameQueue();
		Event[] ev = {
				new Event() {
					public long getTime() { return 100; }
					public void action() { System.out.println("A100"); }
				},
				new Event() {
					public long getTime() { return 9000; }
					public void action() { System.out.println("B9000"); }
				},
				new Event() {
					public long getTime() { return -1; }
					public void action() { System.out.println("C-1"); }
				}
		};
		System.out.print("Peek: ");
		q.addEvent(ev[0]);
		q.addEvent(ev[1]);
		q.addEvent(ev[2]);
		q.peekNext().action();
		q.getNext().action();
		q.getNext().action();
		q.getNext().action();
		System.out.println(q.getNext());
	}
}
