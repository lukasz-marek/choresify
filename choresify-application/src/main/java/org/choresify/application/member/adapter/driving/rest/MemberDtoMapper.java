package org.choresify.application.member.adapter.driving.rest;

import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = ComponentModel.SPRING)
public interface MemberDtoMapper {
  MemberDto map(Member member);

  NewMember map(NewMemberDto newMemberDto);
}
