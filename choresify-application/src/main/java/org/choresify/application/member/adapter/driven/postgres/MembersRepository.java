package org.choresify.application.member.adapter.driven.postgres;

import java.util.Optional;
import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembersRepository extends CrudRepository<MemberEntity, Long> {
  Optional<MemberEntity> findByEmailAddress(String emailAddress);
}
