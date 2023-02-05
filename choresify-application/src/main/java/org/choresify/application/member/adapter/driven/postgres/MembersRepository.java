package org.choresify.application.member.adapter.driven.postgres;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MembersRepository extends CrudRepository<MemberEntity, Long> {
  Optional<MemberEntity> findByEmailAddress(String emailAddress);
}
