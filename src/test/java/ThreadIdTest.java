import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by mtumilowicz on 2018-08-26.
 */
public class ThreadIdTest {

    @Test
    public void ids_array_has_same_values() throws ExecutionException, InterruptedException {
        MyCallable sharedCallableInstance = new MyCallable();

        ExecutorService executorService = Executors.newCachedThreadPool();

        int[] ids = executorService.submit(sharedCallableInstance).get();

        assertThat(ids[0], is(ids[1]));
        assertThat(ids[1], is(ids[2]));
        assertThat(ids[2], is(ids[0]));

        executorService.shutdownNow();
    }
    
    @Test
    public void ids_difference_atLeast3Threads() throws ExecutionException, InterruptedException {
        MyCallable sharedCallableInstance = new MyCallable();

        ExecutorService executorService = Executors.newFixedThreadPool(3);

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

    @Test
    public void ids_same_oneThread() throws ExecutionException, InterruptedException {
        MyCallable sharedCallableInstance = new MyCallable();

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        List<Future<int[]>> futures = executorService.invokeAll(Arrays.asList(
                sharedCallableInstance,
                sharedCallableInstance,
                sharedCallableInstance));


        int[] ids1 = futures.get(0).get();
        int[] ids2 = futures.get(1).get();
        int[] ids3 = futures.get(2).get();

        assertThat(ids1, is(ids2));
        assertThat(ids2, is(ids3));
        assertThat(ids3, is(ids1));

        executorService.shutdownNow();
    }

}