package org.choresify.application.household.adapter.driven.postgres.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;

@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "household")
public class HouseholdEntity {
  @ToString.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ToString.Include String name;
  @ToString.Include long version;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "household_members",
      joinColumns = @JoinColumn(name = "household_id"),
      inverseJoinColumns = @JoinColumn(name = "member_id"))
  Set<MemberEntity> members;
}
