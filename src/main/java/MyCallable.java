import java.util.concurrent.Callable;

/**
 * Created by mtumilowicz on 2018-08-26.
 */
public class MyCallable implements Callable<int[]> {
    @Override
    public int[] call() {
        return new int[]{ThreadId.get(), ThreadId.get(), ThreadId.get()}; // if ThreadId is OK array should have the same values
    }
}
