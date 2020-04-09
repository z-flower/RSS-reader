package com.view;

import com.service.UpdateThread;

public class start {

	public static void main(String[] args) {
		
		JMainFrame j=new JMainFrame();
	    UpdateThread u=new UpdateThread();
		u.run();
		
	}

}
