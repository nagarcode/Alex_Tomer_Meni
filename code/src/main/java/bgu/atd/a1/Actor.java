package bgu.atd.a1;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Actor {
    private String id;
    private PrivateState state;
    private ConcurrentLinkedQueue<Action<?>> pendingActions;
    private Lock lock;

    public Actor(String id, PrivateState state){
        this.id = id;
        this.state = state;
        this.pendingActions = new ConcurrentLinkedQueue<Action<?>>();
        this.lock = new ReentrantLock();
    }

    public Lock getLock(){
        return lock;
    }

    public String getId(){
        return id;
    }

    public PrivateState getPrivateState(){
        return state;
    }

    public ConcurrentLinkedQueue<Action<?>> getPendingActions(){
        return pendingActions;
    }

    public void addAction(Action<?> action){
        pendingActions.add(action);
    }

}
