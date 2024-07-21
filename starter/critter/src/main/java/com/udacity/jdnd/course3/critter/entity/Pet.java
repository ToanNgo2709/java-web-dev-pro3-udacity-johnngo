package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.constant.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_pet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PetType type;

    @Nationalized
    private String name;

    //don't retrieve the owner if we don't need it
//    @ManyToOne(fetch = FetchType.LAZY)//many pets can belong to one owner
//    @JoinColumn(name = "owner_id")  //map the join column in the plant table
//    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)//targetEntity = Owner.class, cascade = CascadeType.ALL,
    @JoinColumn(name = "owner_id")
    private Owner owner;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "schedule_id")
//    private Schedule schedule;

    private LocalDate birthDate;

    private String notes;
}
