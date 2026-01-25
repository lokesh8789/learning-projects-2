package com.r2dbc.demo.entity;

import com.r2dbc.demo.enums.Status;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Table(name = "provider_models")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProviderModel {
    @Id
    @Column("id")
    private Long id;

    @Column("provider")
    private String provider; // OPENAI, ANTHROPIC, DEEPGRAM, etc.

    @Column("model_type")
    private String modelType; // LLM, STT, TTS, REALTIME

    @Column("model_id")
    private String modelId; // gpt-4o, whisper-1, tts-1, realtime-preview-2024-12-17

    @Column("details")
    private Map<String, Object> details;

    @Column("status")
    private Status status;
}
