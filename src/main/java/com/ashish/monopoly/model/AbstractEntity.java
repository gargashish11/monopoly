package com.ashish.monopoly.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Data
@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class})
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @CreatedDate
	@Temporal(TIMESTAMP)
	protected Date createdDate;

	@LastModifiedDate
	@Temporal(TIMESTAMP)
	protected Date lastModifiedDate;

}
