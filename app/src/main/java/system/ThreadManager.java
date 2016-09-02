package system;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by noteMel on 2016-07-03.
 */
public class ThreadManager  {
    private String TAG = "쓰레드 매니져";

    //현재 활동중인 코어의 갯수를 반환.
    //private final int NUMBER_OF_INITIAL_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final int NUMBER_OF_INITIAL_POOL_SIZE = 10;
    private final int NUMBER_OF_MAX_POOL_SIZE = 20;
    private final int KEEP_ALIVE_TIME = 15;



    private final BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue<Runnable>();

    //LinkedBlockingQueue 사용시 MAX POOL size 는 기능을 상실함.
    private ThreadPoolExecutor mThreadPoolExecutor
            = new ThreadPoolExecutor( NUMBER_OF_INITIAL_POOL_SIZE,
            NUMBER_OF_MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            mWorkQueue  );

    private volatile static ThreadManager objectInstance;

    public static ThreadManager getInstance(){
        if(objectInstance == null){
            synchronized (ThreadManager.class) {
                if(objectInstance == null){
                    objectInstance = new ThreadManager();

                }
            }
        }
        return objectInstance;
    }
    public void ShutdownAllThread()
    {
        mThreadPoolExecutor.shutdown();
    }

    //sumit Runnable object to ThreadPool
    public void ActiveThread(Runnable targetThread)
    {

        mThreadPoolExecutor.execute(targetThread);

        //mThreadPoolExecutor.submit();
    }

    public BlockingQueue<Runnable> GetThreadQueue(){
        return mThreadPoolExecutor.getQueue();
    }


}
