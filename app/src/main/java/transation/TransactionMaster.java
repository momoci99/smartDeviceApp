package transation;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import format.TransactionForm;

/**
 * Created by noteMel on 2016-07-05.
 */

public class TransactionMaster implements Runnable {
    static String TAG = "트랜잭션마스터";
    static Queue<TransactionForm> mTransactionQueue = new ConcurrentLinkedQueue<>();
    /*
    private volatile static TransactionMaster objectInstance;


    public static TransactionMaster getInstance(){
        if(objectInstance == null){
            synchronized (TransactionMaster.class) {
                if(objectInstance == null){
                    objectInstance = new TransactionMaster();

                }
            }
        }
        return objectInstance;
    }*/

    public void offerQueue(TransactionForm transactionFormData)
    {
        if(transactionFormData!=null)
        mTransactionQueue.offer(transactionFormData);
        Log.d(TAG,"큐의 크기 : " +mTransactionQueue.size());
        //Log.d(TAG,mTransactionQueue.poll().getName());

    }

    @Override
    public void run() {

    }
}
