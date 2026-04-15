package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    @JoinColumn(name="user_id",unique = true)
    private User user;
    @Column(nullable = false,unique = true)
    private String studentNumber;
    @ManyToOne(optional = false)
    @JoinColumn(name="group_id")
    private Group group;
    public Student(){}
    public Student(User user, Group group, String studentNumber){
        this.user=user;
        this.group=group;
        this.studentNumber=studentNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
