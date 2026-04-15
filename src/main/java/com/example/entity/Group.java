package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="academic_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String groupNumber;
    @ManyToOne(optional = false)
    @JoinColumn(name="specialty_id")
    private Specialty specialty;
    public Group(){}
    public Group(String groupNumber, Specialty specialty){
        this.groupNumber=groupNumber;
        this.specialty=specialty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}
