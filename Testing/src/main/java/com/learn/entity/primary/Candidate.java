package com.learn.entity.primary;

import com.learn.util.DBConstant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate",catalog = DBConstant.TESTING_DB)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String mobile;
    private String aadhaar;
}
