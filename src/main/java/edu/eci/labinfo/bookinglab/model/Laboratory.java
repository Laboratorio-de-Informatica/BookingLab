package edu.eci.labinfo.bookinglab.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode

public abstract class Laboratory {
    protected String laboratoryName;
    protected int availableComputers;
    protected boolean isAvailable;
}
