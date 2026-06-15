package com.lms.student_service.mapper;

import com.lms.student_service.dto.address.AddressRequestDTO;
import com.lms.student_service.dto.address.AddressResponseDTO;
import com.lms.student_service.dto.address.AddressUpdateDTO;
import com.lms.student_service.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponseDTO toDto(Address address);

    List<AddressResponseDTO> toDtoList(List<Address> addressList);

    @Mapping(target = "student",ignore = true)
    Address  toEntity(AddressRequestDTO requestDTO);

    @Mapping(target = "student",ignore = true)
    Address  toEntityUp(AddressUpdateDTO updateDTO);



}
