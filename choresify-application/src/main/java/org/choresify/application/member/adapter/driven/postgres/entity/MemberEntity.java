package org.choresify.application.member.adapter.driven.postgres.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "member")
public class MemberEntity {
  @ToString.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ToString.Include String nickname;
  @ToString.Include String emailAddress;
}
