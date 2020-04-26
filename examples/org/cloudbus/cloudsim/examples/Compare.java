package org.cloudbus.cloudsim.examples;


import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.jfree.ui.RefineryUtilities;

import customclasses.CloudletCreator3;
import customclasses.DataCenterCreator;
import customclasses.MinminBroker;
import customclasses.VmsCreator;
import customclasses.MaxminBroker;
import customclasses.FcfsBroker;
import customclasses.Setw;
import customclasses.Charts;

public class Compare {
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;

	private static int reqTasks = 100;
	private static int reqVms = 20;
	private static double[][] completionTime = new double[reqTasks][3];
	private static double[][] waitingTime = new double[reqTasks][3];
//	private static int[][] noOfVmsUsed = new int[reqTasks][3];
	private static long lengths[] = new long[reqTasks];
	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {

		Log.printLine("Starting Min-Min...");

        try {
        		//generate the cloudlet lengths dynamically
        		for(int i=0; i<reqTasks; i++)
        			lengths[i] = ThreadLocalRandom.current().nextLong(100000, 900000);
//        		for(int i=reqTasks-1; i<reqTasks; i++)
//        			lengths[i] = ThreadLocalRandom.current().nextLong(1000, 1000*reqTasks);
        		
            	//implement minmin
            	minMin();
            	//implement maxmin
            	maxMin();
            	//implement fcfs
            	fcfs();
            	//compare
            	compare();
            	
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }
	
	//method to implement min-min algorithm
	private static void minMin() {
		// First step: Initialize the CloudSim package. It should be called
    	// before creating any entities.
    	int num_user = 1;   // number of cloud users
    	Calendar calendar = Calendar.getInstance();
    	boolean trace_flag = false;  // mean trace events

    	// Initialize the CloudSim library
    	CloudSim.init(num_user, calendar, trace_flag);
		//starting with minmin
    	Log.printLine("Starting Min-Min...");
    	// Second step: Create Datacenters
    	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
    	@SuppressWarnings("unused")
		Datacenter datacenter0 = createDatacenter("Datacenter_0");

    	//Third step: Create Broker
    	MinminBroker minminBroker = createMinminBroker("Broker_Minmin");
    	int brokerId = minminBroker.getId();

    	//Fourth step: Create one virtual machine
    	vmlist = new VmsCreator().createRequiredVms(reqVms, brokerId);


    	//submit vm list to the broker
    	minminBroker.submitVmList(vmlist);


    	//Fifth step: Create two Cloudlets
    	cloudletList = new CloudletCreator3(lengths).createUserCloudlet(reqTasks, brokerId);
    	
    	//waiting time
    	for(int i=0; i<reqTasks; i++) {
    		waitingTime[i][0] = cloudletList.get(i).getWaitingTime();
    	}

    	//submit cloudlet list to the broker
    	minminBroker.submitCloudletList(cloudletList);
    	

    	//call the scheduling function via the broker
    	minminBroker.scheduleTaskstoVms();

	
    	// Sixth step: Starts the simulation
    	CloudSim.startSimulation();


    	// Final step: Print results when simulation is over
    	List<Cloudlet> newList = minminBroker.getCloudletReceivedList();

    	CloudSim.stopSimulation();

    	printCloudletList(newList);
    	
    	//insert into the completionTime array
    	for(Cloudlet c:newList) {
    		completionTime[c.getCloudletId()][0] = c.getActualCPUTime();
//    		noOfVmsUsed[c.getCloudletId()][0] = c.getAllResourceId().length;
    	}

    	Log.printLine("Min-Min finished!");
	}
	
	//method to implement max-min algorithm
	private static void maxMin() {
		// First step: Initialize the CloudSim package. It should be called
    	// before creating any entities.
    	int num_user = 1;   // number of cloud users
    	Calendar calendar = Calendar.getInstance();
    	boolean trace_flag = false;  // mean trace events

    	// Initialize the CloudSim library
    	CloudSim.init(num_user, calendar, trace_flag);
		Log.printLine("Starting Max-Min...");
		// Second step: Create Datacenters
    	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
    	@SuppressWarnings("unused")
		Datacenter datacenter1 = createDatacenter("Datacenter_1");

    	//Third step: Create Broker
    	MaxminBroker maxminBroker = createMaxminBroker("Broker_Maxmin");
    	int brokerId = maxminBroker.getId();

    	//Fourth step: Create one virtual machine
    	vmlist = new VmsCreator().createRequiredVms(reqVms, brokerId);


    	//submit vm list to the broker
    	maxminBroker.submitVmList(vmlist);


    	//Fifth step: Create two Cloudlets
    	cloudletList = new CloudletCreator3(lengths).createUserCloudlet(reqTasks, brokerId);
    	
    	//waiting time
    	for(int i=0; i<reqTasks; i++) {
    		waitingTime[i][1] = cloudletList.get(i).getWaitingTime();
    	}

    	//submit cloudlet list to the broker
    	maxminBroker.submitCloudletList(cloudletList);
    	

    	//call the scheduling function via the broker
    	maxminBroker.scheduleTaskstoVms();

	
    	// Sixth step: Starts the simulation
    	CloudSim.startSimulation();


    	// Final step: Print results when simulation is over
    	List<Cloudlet> newList = maxminBroker.getCloudletReceivedList();

    	CloudSim.stopSimulation();

    	printCloudletList(newList);
    	
    	//insert into the completionTime array
    	for(Cloudlet c:newList) {
    		completionTime[c.getCloudletId()][1] = c.getActualCPUTime();
//    		noOfVmsUsed[c.getCloudletId()][1] = c.getAllResourceId().length;
    	}

    	Log.printLine("MaxMin finished!");
	}
	
	//fcfs
	private static void fcfs() {
		// First step: Initialize the CloudSim package. It should be called
    	// before creating any entities.
    	int num_user = 1;   // number of cloud users
    	Calendar calendar = Calendar.getInstance();
    	boolean trace_flag = false;  // mean trace events

    	// Initialize the CloudSim library
    	CloudSim.init(num_user, calendar, trace_flag);

    	// Second step: Create Datacenters
    	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
    	@SuppressWarnings("unused")
		Datacenter datacenter2 = createDatacenter("Datacenter_1");

    	//Third step: Create Broker
    	FcfsBroker fcfsBroker = createFcfsBroker("Broker_Fcfs");
    	int brokerId = fcfsBroker.getId();

    	//Fourth step: Create one virtual machine
    	vmlist = new VmsCreator().createRequiredVms(reqVms, brokerId);


    	//submit vm list to the broker
    	fcfsBroker.submitVmList(vmlist);


    	//Fifth step: Create two Cloudlets
    	cloudletList = new CloudletCreator3(lengths).createUserCloudlet(reqTasks, brokerId);
    	
    	//waiting time
    	for(int i=0; i<reqTasks; i++) {
    		waitingTime[i][2] = cloudletList.get(i).getWaitingTime();
    	}

    	//submit cloudlet list to the broker
    	fcfsBroker.submitCloudletList(cloudletList);
    	

    	//call the scheduling function via the broker
    	fcfsBroker.scheduleTaskstoVms();

	
    	// Sixth step: Starts the simulation
    	CloudSim.startSimulation();


    	// Final step: Print results when simulation is over
    	List<Cloudlet> newList = fcfsBroker.getCloudletReceivedList();

    	CloudSim.stopSimulation();

    	printCloudletList(newList);
    	
    	//insert into the completionTime array
    	for(Cloudlet c:newList) {
    		completionTime[c.getCloudletId()][2] = c.getActualCPUTime();
//    		noOfVmsUsed[c.getCloudletId()][2] = c.getAllResourceId().length;
    	}

    	Log.printLine("FCFS finished!");
	}
	
	private static void compare() {
		Log.printLine("-----------Comparison-----------");
		String indent = "    ";
		String output;
		DecimalFormat dft = new DecimalFormat("###.##");
		
		//displaying the time taken
		Log.printLine("Cloudlet Id"+indent+"Cloudlet Length"+indent+"Min-Min"+indent+"  Max-Min"+indent+" FCFS");
		Log.printLine("                                "+"Time Taken"+"   "+"Time Taken"+"   "+"Time Taken"+"   "); 
		Log.printLine("---------------------------------------------------------------------------------------------------");
		for(int i=0; i<reqTasks; i++) {
			Log.print(Setw.setw(Integer.toString(i), 11+indent.length()) + Setw.setw(Long.toString(lengths[i]), 15+indent.length()) );
			for(int j=0; j<3; j++) {
				output = Setw.setw(dft.format(completionTime[i][j]), 13); //+ Setw.setw(Integer.toString(noOfVmsUsed[i][j]), 11);
				Log.print(output);
			}
			Log.print("\n");
		}
		
		//computing the average time taken by all cloudlets
		double averageCpuTime[] = new double[3];
		double averageWaitingTime[] = new double[3];
		double sum1, sum2;
		for(int i=0; i<3; i++) {
			sum1 = 0;
			sum2 = 0;
			for(int j=0; j<reqTasks; j++) {
				sum1 += completionTime[j][i];
				sum2 += waitingTime[j][i];
			}
			averageCpuTime[i] = sum1/reqTasks;
			averageWaitingTime[i] = sum2/reqTasks;
		}
		//cpu time
		Log.printLine("------------------------------------------");
		Log.printLine("Average time taken: ");
		Log.printLine(Setw.setw("Min-Min: ", 9)+dft.format(averageCpuTime[0]));
		Log.printLine(Setw.setw("Max-Min: ", 9)+dft.format(averageCpuTime[1]));
		Log.printLine(Setw.setw("FCFS: ", 9)+dft.format(averageCpuTime[2]));
		
		double[] values = {averageCpuTime[0], averageCpuTime[1], averageCpuTime[2]};
		Charts chart = new Charts("Algorithm Comparison", "Average completion time", values);
		chart.pack( );        
	    RefineryUtilities.centerFrameOnScreen( chart );        
	    chart.setVisible( true ); 
		
//		//waiting time
//		Log.printLine("Average waiting time: ");
//		Log.printLine(Setw.setw("Min-Min: ", 9)+dft.format(averageWaitingTime[0]));
//		Log.printLine(Setw.setw("Max-Min: ", 9)+dft.format(averageWaitingTime[1]));
//		Log.printLine(Setw.setw("FCFS: ", 9)+dft.format(averageWaitingTime[2]));
	}

	private static Datacenter createDatacenter(String name){
		Datacenter datacenter=new DataCenterCreator().createUserDatacenter(name, reqVms);			

        return datacenter;

    }

    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static MinminBroker createMinminBroker(String name){

    	MinminBroker broker = null;
        try {
		broker = new MinminBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return broker;
    }
    
    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static MaxminBroker createMaxminBroker(String name){

    	MaxminBroker broker = null;
        try {
		broker = new MaxminBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return broker;
    }
    
    //to create fcfs broker
    private static FcfsBroker createFcfsBroker(String name){

    	FcfsBroker broker = null;
        try {
		broker = new FcfsBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return broker;
    }

    /**
     * Prints the Cloudlet objects
     * @param list  list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
                Log.print("SUCCESS");

            	Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
                     indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
                         indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }
}
