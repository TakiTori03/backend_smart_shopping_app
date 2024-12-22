package com.hust.smart_Shopping.models;

import com.hust.smart_Shopping.constants.KeyId.UserFamilyKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "user_family")
public class UserFamily {
    @EmbeddedId
    private UserFamilyKey userFamilyKey = new UserFamilyKey();

    @ManyToOne
    @MapsId("familyId")
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @OneToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
