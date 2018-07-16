package net.poquesoft.appio.presenter;

import net.poquesoft.appio.utils.UniqueIDGenerator;

public class PresenterAsyncTask {

    private OnCompleteListener onCompleteListener;
    private TaskRunner taskRunner;
    private String completedMessage;
    private String id;

    public PresenterAsyncTask(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
        this.id = UniqueIDGenerator.getId();
    }

    void start(){
        taskRunner.run(this);
    };

    void setOnCompleteListener(PresenterAsyncTask.OnCompleteListener onCompleteListener){
        this.onCompleteListener = onCompleteListener;
    };

    public void setCompletedMessage(String completedMessage) {
        this.completedMessage = completedMessage;
    }

    public String getCompletedMessage() {
        return completedMessage;
    }

    public void complete(){
        if (onCompleteListener != null) onCompleteListener.onComplete(this);
    }

    public String getId() {
        return id;
    }

    public interface OnCompleteListener {
        void onComplete(PresenterAsyncTask task);
    }
}
