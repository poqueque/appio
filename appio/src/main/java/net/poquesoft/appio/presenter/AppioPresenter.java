package net.poquesoft.appio.presenter;

import android.util.Log;

import net.poquesoft.appio.view.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edi on 18/06/18.
 */

public class AppioPresenter implements BasePresenter, PresenterAsyncTask.OnCompleteListener {

    public static final String ALL_TASKS_COMPLETED = "allTasksCompleted";
    private static final String TASK_COMPLETED = "taskCompleted";
    private static final String TAG = "AppioPresenter";
    public BaseView view;

    public AppioPresenter(BaseView view) {
        attach(view);
    }

    private List<PresenterAsyncTask> enqueuedTasks = new ArrayList<>();
    private List<PresenterAsyncTask> runningTasks = new ArrayList<>();
    private List<PresenterAsyncTask> completedTasks = new ArrayList<>();

    protected void enqueueTask(PresenterAsyncTask t){
        enqueuedTasks.add(t);
        Log.d(TAG,"[QUEUE] Added "+t.getId());
        logStatus();
    }

    protected void startTasks(){
        startNextTask();
    }

    private void startNextTask() {
        if (enqueuedTasks.size() > 0){
            PresenterAsyncTask task = enqueuedTasks.get(0);
            enqueuedTasks.remove(task);
            runningTasks.add(task);
            task.setOnCompleteListener(this);
            task.start();
            Log.d(TAG,"[QUEUE] Running "+task.getId());
            logStatus();
        }
    }

    @Override
    public void onComplete(PresenterAsyncTask t) {
        view.onReceiveData(TASK_COMPLETED,t.getCompletedMessage());
        Log.d(TAG,"[QUEUE] Completed "+t.getId());
        runningTasks.remove(t);
        completedTasks.add(t);
        if (enqueuedTasks.size() > 0)
            startNextTask();
        if (runningTasks.size() == 0 && enqueuedTasks.size() == 0)
            view.onReceiveData(ALL_TASKS_COMPLETED,ALL_TASKS_COMPLETED);
        logStatus();
    }


    @Override
    public void attach(BaseView v) {
        this.view = v;
    }

    @Override
    public void detach() {
    }

    private synchronized void logStatus() {
        Log.d(TAG,"[QUEUE] Tasks E:"+enqueuedTasks.size()+" R:"+runningTasks.size()+" C:"+completedTasks.size());
    }
}
