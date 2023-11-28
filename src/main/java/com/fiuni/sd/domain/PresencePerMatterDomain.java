package com.fiuni.sd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "presence_per_matter")
public class PresencePerMatterDomain implements BaseDomain {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @Getter
    @Setter
    private StudentDomain student;

    @ManyToOne
    @Getter
    @Setter
    @JoinColumn(name = "presence_id", referencedColumnName = "id")
    private PresenceDomain presence;

    @Getter
    @Setter
    @Column(name = "is_present", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isPresent;

    @Getter
    @Setter
    @Column(name = "notes")
    private String notes;
}
