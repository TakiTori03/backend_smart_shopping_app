package com.hust.smart_Shopping.constants.KeyId;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
public class UserFamilyKey implements Serializable {
    @Column(name = "family_id", nullable = false)
    Long familyId;

    @Column(name = "user_id", nullable = false)
    Long userId;
}
