package com.udacity.jdnd.course3.critter.dto;

import com.udacity.jdnd.course3.critter.constant.EmployeeSkill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a request to find available employees by skills. Does not map
 * to the database directly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {
    private Set<EmployeeSkill> skills;
    private LocalDate date;
}
