package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Owner;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.repository.OwnerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    public Schedule saveSchedule(Schedule schedule){
        return this.scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return this.scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByPetId(Long petId){

        return this.scheduleRepository.getScheduleByPetsId(petId);
    }

    // finding the customer with the given id, and if it's not already recorded in DB throw proper exception
    public List<Schedule> getScheduleByOwner(Long ownerId) {
        Optional<Owner> optionalOwner = this.ownerRepository.findById(ownerId);

        if (!optionalOwner.isPresent()) {
            throw new ResourceNotFoundException("No schedule found for the given owner id: "+ ownerId);
        }
        else{
            Owner customer = optionalOwner.get();
            List<Pet> pets = customer.getPets();
            List<Schedule> schedules = new ArrayList<>();

            for (Pet pet : pets) {
                schedules.addAll(scheduleRepository.getScheduleByPetsId(pet.getId()));
            }
            return schedules;
        }
    }

    public List<Schedule> getScheduleByEmployee(Long employeeId) {
        return scheduleRepository.getScheduleByEmployeesId(employeeId);
    }

    public ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        scheduleDTO.setActivities(schedule.getActivities());

        List<Pet> pets = schedule.getPets();
        List<Long> petId = new ArrayList<>();
        for (Pet pet : pets) {
            petId.add(pet.getId());
        }
        scheduleDTO.setPetIds(petId);
        List<Employee> employees = schedule.getEmployees();
        List<Long> employeeId = new ArrayList<>();
        for (Employee employee : employees) {
            employeeId.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeId);
        return scheduleDTO;

    }

    public Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO){
        ModelMapper modelMapper = new ModelMapper();
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

        schedule.setActivities(scheduleDTO.getActivities());
        HashMap<Long, Employee> employeeMap = new HashMap<>();
        for (Long employeeId : scheduleDTO.getEmployeeIds()) {
            Optional<Employee> optionalEmployee = Optional.ofNullable(userService.findEmployeeById(employeeId));
            if (optionalEmployee.isPresent()) {
                employeeMap.put(employeeId, optionalEmployee.get());
            } else {
                throw new ResourceNotFoundException();
            }
        }
        schedule.setEmployees(new ArrayList<Employee>(employeeMap.values()));
        HashMap<Long, Pet> petMap = new HashMap<>();
        for (Long petId : scheduleDTO.getPetIds()) {
            Optional<Pet> optionalPet = Optional.ofNullable(petService.getPetByPetId(petId));
            if (optionalPet.isPresent()) {
                petMap.put(petId, optionalPet.get());
            } else {
                throw new ResourceNotFoundException();
            }
        }
        schedule.setPets(new ArrayList<Pet>(petMap.values()));
        return schedule;
    }
}
