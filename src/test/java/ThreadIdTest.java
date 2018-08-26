import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by mtumilowicz on 2018-08-26.
 */
public class ThreadIdTest {

    @Test
    public void id() throws ExecutionException, InterruptedException {
        MyCallable sharedCallableInstance = new MyCallable();

        ExecutorService executorService = Executors.newCachedThreadPool();

        int[] ids = executorService.invokeAny(Collections.singletonList(sharedCallableInstance));

        assertThat(ids[0], is(ids[1]));
        assertThat(ids[1], is(ids[2]));
        assertThat(ids[2], is(ids[0]));

        executorService.shutdownNow();
    }
    
    @Test
    public void ids_difference() throws ExecutionException, InterruptedException {
        MyCallable sharedCallableInstance = new MyCallable();

        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Future<int[]>> futures = executorService.invokeAll(Arrays.asList(
                sharedCallableInstance,
                sharedCallableInstance,
                sharedCallableInstance));


        int[] ids1 = futures.get(0).get();
        int[] ids2 = futures.get(1).get();
        int[] ids3 = futures.get(2).get();

        assertThat(ids1, not(ids2));
        assertThat(ids2, not(ids3));
        assertThat(ids3, not(ids1));
        
        executorService.shutdownNow();
    }

}