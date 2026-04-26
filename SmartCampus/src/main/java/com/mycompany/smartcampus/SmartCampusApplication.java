/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus;

/**
 *
 * @author LENOVO
 */
import com.mycompany.smartcampus.exceptionmapper.*;
import com.mycompany.smartcampus.filter.LoggingFilter;
import com.mycompany.smartcampus.resources.DiscoveryResource;
import com.mycompany.smartcampus.resources.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new HashSet<>();

        resources.add(DiscoveryResource.class);
        resources.add(RoomResource.class);
        resources.add(SensorResource.class);
        resources.add(SensorReadingResource.class);

        resources.add(RoomNotEmptyExceptionMapper.class);
        resources.add(LinkedResourceNotFoundExceptionMapper.class);
        resources.add(SensorUnavailableExceptionMapper.class);
        resources.add(GenericExceptionMapper.class);

        resources.add(LoggingFilter.class);

        return resources;
    }
}
