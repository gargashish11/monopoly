package com.ashish.monopoly.repository;

import java.util.Date;

public interface GameProjection {
    Integer getId();

    String getName();

    Date getLastModifiedDate();
}
