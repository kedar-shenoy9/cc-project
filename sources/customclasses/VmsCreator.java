package customclasses;


import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;;


public class VmsCreator {
	//vmlist creator function
	public ArrayList<Vm> createRequiredVms(int reqVms, int brokerId){
		
		ArrayList<Vm> vmlist = new ArrayList<Vm>();
		
    	//VM description
    	int vmid = 0;
    	//int mips = 1000;
    	int[] mips= new int[reqVms];
    	for(int i=0; i<reqVms; i++) {
    		mips[i] = ThreadLocalRandom.current().nextInt(1, 6) * 100;
    	}
    	long size = 1000; //image size (MB)
    	int ram = 512; //vm memory (MB)
    	long bw = 1000;
    	int pesNumber = 1; //number of cpus
    	String vmm = "Xen"; //VMM name

    	
    	
    	for(vmid=0;vmid<reqVms;vmid++){
    		//add the VMs to the vmList
    		vmlist.add(new Vm(vmid, brokerId, mips[vmid], pesNumber, ram, bw, 
    				size, vmm, new CloudletSchedulerTimeShared()));
    	}

    	System.out.println("VmsCreator function Executed... SUCCESS:)");
		return vmlist;
		
	}
}
