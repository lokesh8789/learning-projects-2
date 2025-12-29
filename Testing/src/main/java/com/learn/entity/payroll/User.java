package com.learn.entity.payroll;

import com.learn.util.DBConstant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(catalog = DBConstant.TEST_SECOND_DB)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
