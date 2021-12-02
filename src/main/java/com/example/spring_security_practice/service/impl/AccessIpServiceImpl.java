package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.repository.AccessIpRepository;
import com.example.spring_security_practice.service.AccessIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessIpServiceImpl implements AccessIpService {

    @Autowired
    private AccessIpRepository accessIpRepository;

    @Override
    public List<String> getAccessIpList() {
        List<String> ipList = accessIpRepository.findAll().stream().map(ip -> ip.getIpAddress()).collect(Collectors.toList());
        return ipList;
    }
}
