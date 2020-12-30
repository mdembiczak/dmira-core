package com.dcmd.dmiracore.controller.dev;

import com.dcmd.dmiracore.model.Role;
import com.dcmd.dmiracore.model.State;
import com.dcmd.dmiracore.model.enums.ERole;
import com.dcmd.dmiracore.model.enums.EState;
import com.dcmd.dmiracore.payload.dev.RoleRequest;
import com.dcmd.dmiracore.payload.dev.StateRequest;
import com.dcmd.dmiracore.repository.RoleRepository;
import com.dcmd.dmiracore.repository.StateRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dev")
public class DevController {
    final StateRepository stateRepository;
    final RoleRepository roleRepository;

    public DevController(StateRepository stateRepository, RoleRepository roleRepository) {
        this.stateRepository = stateRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("state")
    public List<State> addStates(@RequestBody StateRequest request) {
        Set<EState> allValidStates = Set.of(EState.values());
        List<State> validatedStates = request.getStates().stream()
                .filter(state -> allValidStates.contains(EState.valueOf(state)))
                .map(EState::valueOf)
                .map(State::new)
                .collect(Collectors.toList());

        return stateRepository.saveAll(validatedStates);
    }

    @PostMapping("role")
    public List<Role> addRoles(@RequestBody RoleRequest request) {
        Set<ERole> allValidRoles = Set.of(ERole.values());
        List<Role> validatedRoles = request.getRoles().stream()
                .filter(role -> allValidRoles.contains(ERole.valueOf(role)))
                .map(ERole::valueOf)
                .map(Role::new)
                .collect(Collectors.toList());

        return roleRepository.saveAll(validatedRoles);
    }
}