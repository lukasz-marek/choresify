package org.choresify.application.member.adapter.driven.postgres;

import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = ComponentModel.SPRING)
interface MemberEntityMapper {

  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "version", constant = "0L")
  MemberEntity map(NewMember newMember);

  Member map(MemberEntity member);

  MemberEntity map(Member member);
}
