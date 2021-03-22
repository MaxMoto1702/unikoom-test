package net.serebryansky.max.com.unikoom.test.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected Long id;
    protected String type;
    protected String name;

    @EqualsAndHashCode.Exclude
    protected byte[] bytes;
}
