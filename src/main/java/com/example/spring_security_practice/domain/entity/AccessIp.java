package com.example.spring_security_practice.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessIp {

    @Id
    @GeneratedValue
    @Column(name = "IP_ID")
    private Long id;

    private String ipAddress;
}