package com.du.dobab.service;

import com.du.dobab.domain.Party;
import com.du.dobab.dto.request.PartySave;
import com.du.dobab.exception.PartyNotFound;
import com.du.dobab.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PartyService {

    private final PartyRepository partyRepository;

    public void save(PartySave partySave) {
        partyRepository.save(partySave.toEntity());
    }

    public void delete(Long id) {
        Party savedParty = partyRepository.findById(id).orElseThrow(PartyNotFound::new);
        partyRepository.deleteById(savedParty.getId());
    }
}
