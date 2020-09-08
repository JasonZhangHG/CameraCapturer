package cool.capturer.android.capturer.connector;

public interface SinkConnector<T> {
    int onDataAvailable(T data);
}
