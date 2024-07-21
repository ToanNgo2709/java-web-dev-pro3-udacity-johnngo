package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.constant.EmployeeSkill;
import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Owner;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.OwnerRepository;
import com.udacity.jdnd.course3.critter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    PetService petService;

    public Owner saveCustomer(Owner owner){
        return this.ownerRepository.save(owner);
    }

    public void delete(){
        this.ownerRepository.deleteAll();
        this.employeeRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    public Employee saveEmployee(Employee employee){
        return this.employeeRepository.save(employee);
    }


    // finding the employee with the given id, and if it's not already recorded in DB throw proper exception
    public Employee findEmployeeById(Long id){
        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            return employee;
        }
        return optionalEmployee.orElseThrow(() -> new ResourceNotFoundException("Employee with id: "+ id + " not found"));
    }

    public Owner findOwnerById(Long id){
        Optional<Owner> optionalOwner = this.ownerRepository.findById(id);
        if(optionalOwner.isPresent()){
            Owner owner = optionalOwner.get();
            return owner;
        }
        return optionalOwner.orElseThrow(ResourceNotFoundException::new);
    }

    public List<Owner> findAllOwners(){
        return this.ownerRepository.findAll();
    }


    public void updateDaysAvailable(Set<DayOfWeek> daysAvailable, Long id){
        Employee employee = this.findEmployeeById(id);

        employee.setDaysAvailable(daysAvailable);
        this.employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, LocalDate date){
        List<Employee> availableEmployees = new ArrayList<>();
        List<Employee> employees = this.employeeRepository.findEmployeesByDaysAvailable(date.getDayOfWeek());

        for (Employee employee : employees) {
            if (employee.getSkills().containsAll(skills)) {
                availableEmployees.add(employee);
            }
        }
        return availableEmployees;
    }

    public CustomerDTO convertOwnerToCustomerDTO(Owner owner){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(owner, customerDTO);
        List<Pet> pets = owner.getPets();

        if (pets != null) {
            List<Long> petIds = new ArrayList<>();

            for (Pet pet : pets) {
                petIds.add(pet.getId());
            }
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    public Owner convertCustomerDTOToOwner(CustomerDTO customerDTO){
        ModelMapper modelMapper = new ModelMapper();
        Owner owner = modelMapper.map(customerDTO, Owner.class);
        List<Long> petIds = customerDTO.getPetIds();

        if (petIds != null) {
            List<Pet> pets = new ArrayList<Pet>();

            for (Long petId : petIds) {
                pets.add(petService.getPetByPetId(petId));
            }
            owner.setPets(pets);
        }
        return owner;
    }

    public EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    public Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO){
        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        return employee;
    }
}
