package customclasses;

import java.util.ArrayList;
//import java.util.Collections;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

public class MaxminBroker extends DatacenterBroker {
	public MaxminBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	//scheduling function
	private double[] readyTime;
	
	public double scheduleTaskstoVms(){
		int reqTasks= cloudletList.size();
		int reqVms= vmList.size();
		//int k=0;
		
		readyTime = new double[reqVms];
		//initialize the ready time
		for(int i=0; i<reqVms; i++)
			readyTime[i] = 0.0;
		
		ArrayList<Cloudlet> clist = new ArrayList<Cloudlet>();
		ArrayList<Vm> vlist = new ArrayList<Vm>();
		
		for (Cloudlet cloudlet : getCloudletList()) {
    		clist.add(cloudlet);
    		//System.out.println("clist:" +clist.get(k).getCloudletId());
    		//k++;
		}
		//k=0;
		for (Vm vm : getVmList()) {
    		vlist.add(vm);
    		//System.out.println("vlist:" +vlist.get(k).getId());
    		//k++;
		}

		int noCloudlets = clist.size();
		int noVms = vlist.size();
		double completionTime[][] = new double[reqTasks][reqVms];
		double execTime[][] = new double[reqTasks][reqVms];
		double time =0.0;
		
		for(int i=0; i<reqTasks; i++){
			for(int j=0; j<reqVms; j++){
				time = getCompletionTime(clist.get(i), vlist.get(j));
				completionTime[i][j]= time;
				time = getExecTime(clist.get(i), vlist.get(j));
				execTime[i][j]= time;
				
//				System.out.print(execTime[i][j]+" ");
				
			}
//			System.out.println();
			
		}
		
		int maxCloudlet=0;
		int minVm=0;
		double min=-1.0d;
		double maxCompletion;
		double oldReadyTimeOfMinVm;
		double readyTimeDifference;
		
		for(int c=0; c< noCloudlets; c++){
			maxCompletion = Integer.MIN_VALUE;
			for(int i=0;i<noCloudlets;i++){
				for(int j=0;j<noVms;j++){
					if(maxCompletion < completionTime[i][j] && completionTime[i][j] != -1.0){
						maxCloudlet=i;
						maxCompletion = completionTime[i][j];
					}
				}
			}
			
			min = Double.MAX_VALUE;
			for(int j=0; j<vlist.size(); j++){
//				time = getExecTime(clist.get(maxCloudlet), vlist.get(j));
				time = completionTime[maxCloudlet][j];
				if(time < min && time > -1.0){
					minVm=j;
					min=time;
				}
				
			}
			
//			bindCloudletToVm(maxCloudlet, minVm);
			clist.get(maxCloudlet).setVmId(vlist.get(minVm).getId());
			Cloudlet MaxCloudlet = clist.get(maxCloudlet);
//			clist.remove(maxCloudlet);
			
			//update the ready time
			oldReadyTimeOfMinVm = readyTime[minVm];
			readyTime[minVm] = oldReadyTimeOfMinVm + getExecTime(MaxCloudlet, vlist.get(minVm));
			readyTimeDifference = readyTime[minVm]-oldReadyTimeOfMinVm;
			
			//update the 2d array
			for(int i=0; i<clist.size(); i++) {
				if(completionTime[i][minVm] != -1.0) {
					completionTime[i][minVm] += readyTimeDifference;
				}
			}
			
			Log.print("Printing the ready time array "+minVm+" "+maxCloudlet+" ");
			for(int i=0; i<reqVms; i++) {
				Log.print(readyTime[i]+" ");
			}
			Log.print("\n");
			
			for(int i=0; i<vlist.size(); i++){
				completionTime[maxCloudlet][i]=-1.0;
			}
			
		}
		
		return getThroughput();
	}	
	
	private double getThroughput() {
		double maxReadyTime = readyTime[0];
		for(int i=1; i<readyTime.length; i++) {
			if(maxReadyTime > readyTime[i])
				maxReadyTime = readyTime[i];
		}
		double throughput = cloudletList.size() / (maxReadyTime + 0.1);
		return throughput;
	}
	
	private double getCompletionTime(Cloudlet cloudlet, Vm vm){
		double waitingTime = cloudlet.getWaitingTime();
		double execTime = cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
		
		double completionTime = execTime + waitingTime;
		
		return completionTime;
	}
	
	private double getExecTime(Cloudlet cloudlet, Vm vm){
		return cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
	}
}
