package net.serebryansky.max.com.unikoom.test.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Информация о пользователе
 */
@Entity
@Table(name = "\"user\"")
//@Immutable
@Data
public class User {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected Long id;

    /**
     * username
     */
//    @NotNull
//    @Size(min = 3, max = 10)
//    @Column(unique = true, nullable = false)
    protected String username;

    /**
     * ФИО
     */
//    @NotNull
//    @Size(min = 6)
//    @Pattern(regexp = ".* .* .*")
//    @Column(nullable = false)
    protected String name;

    /**
     * email
     */
//    @NotNull
//    @Email
//    @Column(nullable = false)
    protected String email;

    /**
     * Дата рождения
     */
//    @NotNull
//    @Column(nullable = false)
    protected LocalDate birthDate;

    /**
     * Пол
     */
//    @NotNull
//    @Column(nullable = false)
    @Enumerated(STRING)
    protected Sex sex;

    /**
     * Фото
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PHOTO_ID")
    protected Photo photo;
}
