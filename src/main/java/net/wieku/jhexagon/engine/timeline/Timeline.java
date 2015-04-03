package net.wieku.jhexagon.engine.timeline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;

/**
 * @author Sebastian Krajewski on 01.04.15.
 */
public class Timeline <T extends TimelineObject> {


	float currenttime;
	float delaytime;

	class QueueObject {
		Float time;
		T object;

		public QueueObject(Float time, T object) {
			this.time = time;
			this.object = object;
		}
	}

	PriorityQueue<QueueObject> queue = new PriorityQueue<>((o1, o2) -> {
		return o1.time.compareTo(o2.time);
	});

	ArrayList<T> update = new ArrayList<>();


	public Timeline(){

	}

	public void update(float delta){
		currenttime+=delta;
		if(delaytime < currenttime) delaytime = currenttime;

		while(!queue.isEmpty() && queue.peek().time <= currenttime){
			update.add(queue.poll().object);
		}

		for(int i = 0; i < update.size(); ++i){

			T object = update.get(i);

			object.update(delta);

			if(object.toRemove){
				update.remove(object);
				--i;
			}

		}

	}

	public void wait(float seconds){
		delaytime+=seconds;
	}

	public void submit(T object){
		queue.add(new QueueObject(delaytime, object));
	}

	public boolean isAllSpawned(){
		return queue.isEmpty();
	}

	public boolean isEmpty(){
		return isAllSpawned() && update.isEmpty();
	}

	public ArrayList<T> getObjects(){
		return update;
	}

	public void resetAll(){
		queue.clear();
		update.clear();
	}

	public void resetQueue(){
		queue.clear();
	}

	public void resetSpawned(){
		update.clear();
	}
}
