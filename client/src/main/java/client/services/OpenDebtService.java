package client.services;

import client.models.Debt;
import commons.Event;
import commons.Participant;

import java.util.ArrayList;
import java.util.List;

public class OpenDebtService {
    /**
     * Constructor for the service
     */
    public OpenDebtService() {
    }
    /**
     * Creates a list of transactions to settle the open debt within the group
     *
     * @param event the corresponding event
     * @return list of maximum n-1 transactions to settle debt
     */
    public List<Debt> getDebts(Event event){
        List<Participant> list = new ArrayList<>(event.getParticipants());
        double[] debts = new double[list.size()];

        List<Debt> result = new ArrayList<>();
        list.sort((x,y)->{
            if(x.getDebt()<y.getDebt()) return -1;
            else if(x.getDebt()>y.getDebt()) return 1;
            else return 0;
        });
        for(int k=0;k<debts.length;k++){
            debts[k]=list.get(k).getDebt();
        }
        int i=0;
        int j=list.size()-1;
        while(i<j){
            if(Math.abs(list.get(i).getDebt())<0.01){
                i++;

            }
            else if(Math.abs(list.get(j).getDebt())<0.01){
                j--;

            }
            else if(Math.abs(debts[i]+debts[j])<0.01){
                result.add(new Debt(debts[j],list.get(j),list.get(i)));
                debts[i]=0;
                debts[j]=0;
                i++;
                j--;
            }
            else if(Math.abs(debts[i])<debts[j]){
                result.add(new Debt(Math.abs(debts[i]),list.get(j),list.get(i)));
                debts[j]+=debts[i];
                debts[i]=0;
                i++;

            }
            else{
                result.add(new Debt(debts[j],list.get(j),list.get(i)));
                debts[i]+=debts[j];
                debts[j]=0;
                j--;
            }
        }
        return result;
    }
}
