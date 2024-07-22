package service;

import model.State;

/**
 * Normally, the service layer would be used to cleanse, validate, and modify user input before passing it on
 * to the appropriate persistance layer, commonly a Database Access Object. This layered approach allows
 * for developers to swap in and out different implementations of these methods without having to refactor
 * every layer; the server need not care how we cleanse the data, only that the appropriate service layer object
 * has a method with the expected signature. Likewise, the service layer doesn't care how we persist our data,
 * only that we have supplied an object with a method of that signature. This is extremely valuable for rapid iteration
 * and testing.
 * 
 * In this instance, however, I have stopped at the service layer as State is a memory-based data store - there is
 * no persistance layer because we don't want it persisted. As such, instead of a StateAccessObject, we simply store
 * and manage the State in the service layer. Is this the best approach? Probably not, but it works for me and
 * for now - which is all I require for this practice repository.
 */
public class StateService {

    private final State state;

    public StateService() {
        this.state = new State();
    }

    public int getState() {
        return this.state.getStateValue();
    }

    public void stateAdd(int amt) {
        this.state.add(amt);
    }

    public void stateSub(int amt) {
        this.state.sub(amt);
    }
    
}
