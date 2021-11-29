package com.example.spring_security_practice.repository.queryDSL;

import com.example.spring_security_practice.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.spring_security_practice.domain.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class CommonQueryDSLImpl implements CommonQueryDSL {

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    public CommonQueryDSLImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QRole q1 = new QRole("q1");


    @Transactional
    // 해당 roleName보다 더 낮은 접근권한을 가진 모든 Role을 가져옴
    public List<Role> getRolesForAccount(String roleName){
        return jpaQueryFactory.selectFrom(q1)
                .where(q1.roleNum.goe(
                        jpaQueryFactory.select(q1.roleNum).from(q1).where(q1.roleName.eq(roleName))
                ))
                .fetch();
    }

    @Override
    // 해당 roleName보다 더 높은 접근권한을 가진 모든 Role을 가져옴
    public List<Role> getRolesForResources(String roleName) {
        return jpaQueryFactory.selectFrom(q1)
                .where(q1.roleNum.loe(
                        jpaQueryFactory.select(q1.roleNum).from(q1).where(q1.roleName.eq(roleName))
                ))
                .fetch();
    }

}
