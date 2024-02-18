package com.gbdecastro.netflix.domain.users;

import com.gbdecastro.netflix.application.graphql.users.inputs.UserInput;
import com.gbdecastro.netflix.domain.shared.DomainException;
import com.gbdecastro.netflix.domain.shared.annotation.BaseService;
import com.gbdecastro.netflix.domain.shared.message.MessageContext;
import com.gbdecastro.netflix.domain.shared.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.gbdecastro.netflix.domain.shared.utils.StringUtils.COMMA;

@BaseService
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private MessageContext messageContext;

    public Flux<UserEntity> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public Mono<UserEntity> getById(String id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Mono<UserEntity> create(UserInput input) {
        return this.userRepository.save(mapper.toEntity(input));
    }

    @Override
    public Mono<UserEntity> update(String id, UserInput toUpdate) {
        return getById(id).flatMap(user -> {
            user = mapper.update(user, toUpdate);
            return userRepository.save(user);
        }).switchIfEmpty(Mono.error(new DomainException(404, messageContext.getMessage("user_not_found"))));
    }

}
