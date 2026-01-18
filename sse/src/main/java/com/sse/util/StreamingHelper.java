package com.sse.util;

import com.sse.exceptions.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamingHelper {

    private static SseEvent parseSseEvent(BufferedReader reader) {
        try {
            SseEvent event = new SseEvent();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("id:")) {
                    event.setId(line.substring(3).trim());
                } else if (line.startsWith("event:")) {
                    event.setEvent(line.substring(6).trim());
                } else if (line.startsWith("data:")) {
                    String data = line.substring(5).trim();
                    event.setData(event.getData() != null ? event.getData() + "\n" + data : data);
                } else if (line.startsWith("retry:")) {
                    event.setRetry(Duration.ofMillis(Long.parseLong(line.substring(6).trim())));
                } else if (line.startsWith(":")) {
                    event.setComment(line.substring(1).trim());
                } else if (line.isEmpty() && (event.getData() != null || event.getComment() != null)) {
                    return event;
                }
            }
            return null;
        } catch (Exception e) {
            log.info("Error reading SSE stream", e);
            return null;
        }
    }

    private static void throwError(ClientHttpResponse response) {
        try {
            if (response.getStatusCode().isError()) {
                InputStream inputStream = response.getBody();
                String data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                inputStream.close();
                response.close();
                throw new BusinessException(data, HttpStatus.valueOf(response.getStatusCode().value()));
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.info("Exception Occurred While Getting SSE Data", e);
            throw new IllegalArgumentException(e);
        }
    }

    public static Stream<SseEvent> getSseStream(ClientHttpResponse response, boolean autoClose) {
        try {
            throwError(response);
            return getSseStream(response.getBody(), response::close, autoClose);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            return Stream.empty();
        }
    }

    public static Stream<SseEvent> getSseStream(InputStream inputStream, Runnable closeResource, boolean autoClose) {
        try {
            return autoClose ? sseStreamAutoClose(inputStream, closeResource) : sseStream(inputStream, closeResource);
        } catch (Exception e) {
            return Stream.empty();
        }
    }

    private static Stream<SseEvent> sseStream(InputStream inputStream, Runnable closeResource) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return Stream.generate(() -> parseSseEvent(reader))
                .takeWhile(Objects::nonNull)
                .onClose(() -> {
                    log.info("Closing SSE stream");
                    try {
                        reader.close();
                        closeResource.run();
                    } catch (Exception e) {
                        log.info("Error closing SSE stream", e);
                    }
                });
    }

    private static Stream<SseEvent> sseStreamAutoClose(InputStream inputStream, Runnable closeResource) {
        return Stream.of(Long.MIN_VALUE)
                .flatMap(i -> sseStream(inputStream, closeResource));
    }

    public static Stream<String> getStream(ClientHttpResponse response, boolean autoClose) {
        try {
            throwError(response);
            return getStream(response.getBody(), response::close, autoClose);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            return Stream.empty();
        }
    }

    public static Stream<String> getStream(InputStream inputStream, Runnable closeResource, boolean autoClose) {
        try {
            return autoClose ? streamAutoClose(inputStream, closeResource) : stream(inputStream, closeResource);
        } catch (Exception e) {
            return Stream.empty();
        }
    }

    private static Stream<String> stream(InputStream inputStream, Runnable closeResource) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines()
                .onClose(() -> {
                    log.info("Closing streaming response");
                    try {
                        reader.close();
                        closeResource.run();
                    } catch (Exception e) {
                        log.info("Error closing streaming response", e);
                    }
                });
    }

    private static Stream<String> streamAutoClose(InputStream inputStream, Runnable closeResource) {
        return Stream.of(Long.MIN_VALUE)
                .flatMap(i -> stream(inputStream, closeResource));
    }


}

