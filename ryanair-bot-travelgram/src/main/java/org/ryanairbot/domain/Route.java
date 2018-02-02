package org.ryanairbot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
public class Route implements Serializable {

    private final String from;
    private final String to;
}
