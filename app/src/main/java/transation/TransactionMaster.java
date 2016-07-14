package transation;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import format.TransactionForm;

/**
 * Created by noteMel on 2016-07-05.
 */

//TODO : 파서에서 트랜잭션 마스터로 데이터 넘기기.
    //쓰레드 마스터 코어 갯수 변경하기(책참고)
    //서비스쪽 느려지면 아두이노쪽 코드 변경하기

public class TransactionMaster implements Runnable{
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
        //Log.d(TAG,"큐의 크기 : " +mTransactionQueue.size());
        if(mTransactionQueue.size()>200)
        {
            mTransactionQueue.clear();//메모리 낭비방지를 위해 비워둠
        }
    }

    @Override
    public void run() {

    }
}
