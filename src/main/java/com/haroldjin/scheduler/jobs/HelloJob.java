package com.haroldjin.scheduler.jobs;

import java.io.File;
import java.io.FileWriter;
public class HelloJob {
	public void sayHello()  {
		try{
			System.out.println("Start writting");
		FileWriter fw= new FileWriter(new File("/Users/harold/tmp/quartz-spring_demo.log"));
		fw.write("hello world");
		fw.close();
		}catch(Exception e){
			System.out.println("error saving file");
		}
	}
}
