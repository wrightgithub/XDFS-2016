package com.xdfs.client.main;

import com.xdfs.client.action.FsOperator;

import java.io.IOException;

public class BootstrapClient {

    public static void main(String[] args)  {

		try {
			FsOperator.write("test.txt","1234567890");
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
