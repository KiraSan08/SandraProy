package com.fiuni.sd.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "students")
public class StudentDomain implements BaseDomain {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "ci")
    private String ci;

    @Getter
    @Setter
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, orphanRemoval = true)
    @Getter
    @Setter
    private List<StudentPerMatterDomain> matters;

    public StudentDomain() {

    }
}