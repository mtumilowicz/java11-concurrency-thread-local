import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mtumilowicz on 2018-08-26.
 */
class ThreadId {
    private static final AtomicInteger nextId = new AtomicInteger(0);
    
    private static final ThreadLocal<Integer> threadId =
            ThreadLocal.withInitial(nextId::getAndIncrement);

    static int get() {
        return threadId.get();
    }
}
