package com.minh1952.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class VerifyTokenRepsonse {
    boolean valid;
}
