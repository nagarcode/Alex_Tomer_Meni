package bgu.atd.a1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	private final List<Thread> threads;
	private final ConcurrentHashMap<String, Actor> actors;
	private final CountDownLatch shutDownLatch;

	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads){
		threads = new LinkedList<>();
		actors = new ConcurrentHashMap<String, Actor>();
		shutDownLatch = new CountDownLatch(nthreads);
		initializeThreads(nthreads);
	}

	private void initializeThreads(int nthreads) {
		for(int i=0; i<nthreads; i++){
			Thread thread = new Thread(()->{
				while(!Thread.currentThread().isInterrupted()){
					iterateOverQueues();
				}
				System.out.println("Thread interrupted: " + Thread.currentThread().toString());
				shutDownLatch.countDown();
			});
			threads.add(thread);
		}
	}

	private void iterateOverQueues() {
		for(Actor actor : actors.values()) {
//			if (Thread.currentThread().isInterrupted()) {
//				break;
//			} else {
				Lock actorLock = actor.getLock();
				ConcurrentLinkedQueue<Action<?>> actorQueue = actor.getPendingActions();
				if(actorLock.tryLock() && !actorQueue.isEmpty()){
//					try{
						Action<?> action = actorQueue.remove();
						action.handle(this, actor.getId(),actor.getPrivateState());
//					}
//					catch(Exception e){
//						System.out.println("Tried querying an empty queue");
//						System.out.println(e.toString());
//					}
//					finally {
						actorLock.unlock();
//					}
				}
//			}
		}
	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		HashMap<String, PrivateState> acts = new HashMap<>();
		for(Actor act : actors.values()){
			acts.put(act.getId(), act.getPrivateState());
		}
		return acts;
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return actors.get(actorId).getPrivateState();
	}


	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		synchronized (this){
			if(!actors.containsKey(actorId)){
				Actor actor = new Actor(actorId,actorState);
				actors.put(actorId, actor);
			}
			actors.get(actorId).addAction(action);
		}
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException{
		System.out.println("Shutdown initiated");
		for(Thread thread : threads){
			thread.interrupt();
		}
		System.out.println("Sent interrupt to all threads.");
		shutDownLatch.await();
		System.out.println("Shutdown successful.");
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(Thread thread : threads){
			System.out.println("Starting thread: " + thread.getId());
			thread.start();
		}
		System.out.println("ThreadPool started");
	}

}
