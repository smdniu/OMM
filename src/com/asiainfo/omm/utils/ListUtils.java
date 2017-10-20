package com.asiainfo.omm.utils;

import java.util.ArrayList;
import java.util.List;


public final class ListUtils {
	
	public static <T> List<T> getListFromArray(T... arrays){
		List<T> arrayList = new ArrayList<T>();
		for(T array: arrays){
			arrayList.add(array);
		}
		return arrayList;
	}

}
