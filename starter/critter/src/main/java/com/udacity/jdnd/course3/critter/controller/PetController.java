package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Owner;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Owner owner = null;

        if (petDTO.getOwnerId() != 0) {
            owner = this.userService.findOwnerById(petDTO.getOwnerId());
        }

        Pet pet = convertPetDTOToPet(petDTO);
        pet.setOwner(owner);
        Pet savedPet = petService.savePet(pet);

        if (owner != null) {
            owner.addPet(savedPet);
        }
        return convertPetToPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<PetDTO> getPets(){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        throw new UnsupportedOperationException();
    }

    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        // in order for copyProperties to work, properties of the DTO and normal object must match in name
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }

    private Pet convertPetDTOToPet(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }
}
