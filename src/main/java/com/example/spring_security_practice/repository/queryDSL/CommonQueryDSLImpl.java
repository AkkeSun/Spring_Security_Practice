package com.example.spring_security_practice.repository.queryDSL;

import com.example.spring_security_practice.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

public class CommonQueryDSLImpl implements CommonQUeryDSL{

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    public CommonQueryDSLImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QRole q1 = new QRole("q1");

    @Transactional
    public List<Role> getRolesForAccount(String roleName){
        return jpaQueryFactory.selectFrom(q1)
                .where(q1.roleNum.loe(
                        jpaQueryFactory.select(q1.roleNum)
                                       .from(q1)
                                       .where(q1.roleName.eq(roleName))
                ))
                .fetch();
    }

}
