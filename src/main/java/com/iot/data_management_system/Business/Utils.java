package com.iot.data_management_system.Business;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapperImpl;

public class Utils {
	
	public static String[] getNullPropertyNames (Object source) {
	    final BeanWrapperImpl src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    emptyNames.add("id");
	    for(java.beans.PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}

}
