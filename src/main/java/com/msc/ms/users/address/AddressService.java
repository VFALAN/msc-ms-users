package com.msc.ms.users.address;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddressService {
    private final IAddressRepository iAddressRepository;

    public AddressService(final IAddressRepository pIAddressRepository) {
        iAddressRepository = pIAddressRepository;
    }


    public AddressEntity save(AddressEntity pAddressEntity) {
        return this.iAddressRepository.save(pAddressEntity);
    }

}
