package com.sse.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SseEvent {
    private String id;
    private String event;
    private String data;
    private String comment;
    private Duration retry;

    @Override
    public String toString() {
        return ("SseEvent [id = " + this.id + ", event=" + this.event + ", retry=" +
                this.retry + ", comment=" + this.comment + ", data=" + this.data + ']');
    }

    public boolean isDone() {
        return data != null && data.trim().equalsIgnoreCase("[DONE]");
    }

    public boolean isNotDone() {
        return !isDone();
    }
}
