package com.learn.entity.primary;

import com.learn.entity.payroll.User;
import com.learn.util.DBConstant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(catalog = DBConstant.TESTING_DB)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int userId;
}
