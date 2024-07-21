package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.repository.OwnerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;

    public Pet savePet(Pet pet){
        return petRepository.save(pet);
    }

    // finding the pet with the given id, and if it's not already recorded in DB throw proper exception
    public Pet getPetByPetId(Long petId){
        Optional<Pet> optionalPet = this.petRepository.findById(petId);
        return optionalPet.orElseGet(
                () -> optionalPet.orElseThrow(() -> new ResourceNotFoundException("Pet with id: " + petId + " not found"))
        );
    }

    public List<Pet> getPetsOfAnOwner(Long ownerId){
        return this.petRepository.findPetsByOwnerId(ownerId);

    }

    public List<Pet> findAllPets(){
        return this.petRepository.findAll();
    }

    public PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        // in order for copyProperties to work, properties of the DTO and normal object must match in name
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }

    public Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }
}
